import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import org.scalacheck.{Gen}
import org.scalacheck.Prop.forAll

/*
 * 継続モナド continuation monad
 * CMonad[A] := (A => Answer) => Answer)
 * callcc :: ((A => M B) => M A) => M A)
 *
 */

trait continuationMonad {
  type Answer = Value
  type Name = String
  
  case class CMonad[A](in: (A => Answer) => Answer) {
    def bind[B](k: A => CMonad[B]): CMonad[B]          = CMonad[B](c => in (a => k(a) in c))
    def map[B](f: A => B): CMonad[B]        = bind(x => unitM(f(x)))
    def flatMap[B](f: A => CMonad[B]): CMonad[B] = bind(f)
  }

  def unitM[A](a: A) = CMonad[A](c => c(a))

  def id[A] = (x: A) => x
  def showM(m: CMonad[Value]): String = (m in id).toString()

  def callCC[A](h: (A => CMonad[A]) => CMonad[A]) : CMonad[A] = CMonad[A](c => h(a => CMonad[A](d => c(a))) in c)

  trait Term
  case class Var(x: Name) extends Term
  case class Con(n: Int) extends Term
  case class Add(l: Term, r: Term) extends Term
  case class Lam(x: Name, body: Term) extends Term
  case class App(fun: Term, arg: Term) extends Term
  case class Ccc(x: Name, t: Term) extends Term

  trait Value
  case object Wrong extends Value {
    override def toString() = "wrong"
  }
  case class Num(n: Int) extends Value {
    override def toString() = n.toString()
  }
  case class Fun(f: Value => CMonad[Value]) extends Value {
    override def toString() = "<function>"
  }

  type Environment = List[Pair[Name, Value]];

  def lookup(x: Name, e: Environment): CMonad[Value] = e match {
    case List() => unitM(Wrong)
    case Pair(y, b) :: e1 => if (x == y) unitM(b) else lookup(x, e1)
  }

  def add(a: Value, b: Value): CMonad[Value] = Pair(a, b) match {
    case Pair(Num(m), Num(n)) => unitM(Num(m + n))
    case _ => unitM(Wrong)
  }

  def apply(a: Value, b: Value): CMonad[Value] = a match {
    case Fun(k) => k(b)
    case _ => unitM(Wrong)
  }

  def interp(t: Term, e: Environment): CMonad[Value] = t match {
    case Var(x) => lookup(x, e)
    case Con(n) => unitM(Num(n))
    case Add(l, r) =>
      for {
        a <- interp(l, e)
		b <- interp(r, e)
		c <- add(a, b)
      } yield c
    case Lam(x, t) => unitM(Fun(a => interp(t, Pair(x, a) :: e)))
    case App(f, t) =>
      for {
        a <- interp(f, e)
        b <- interp(t, e)
        c <- apply(a, b)
      } yield c
    case Ccc(x, t) => callCC(k => interp(t, Pair(x, Fun(k)) :: e))
  }

  def test(t: Term): String = showM(interp(t, List()))

}

class ContinuationMonadCheck extends FunSpec with ShouldMatchers with GeneratorDrivenPropertyChecks with continuationMonad {
  describe("interpreter"){
    val term0 = App(Lam("x", Add(Var("x"), Var("x"))), Add(Con(10), Con(11)))
    val term1 = App(Con(1), Con(2))
    val term2 = Add(Con(1), Ccc("k", Add(Con(2), App(Var("k"), Con(4)))))
    
    test(term0) should equal("42")
    test(term2) should equal("5")
  }
  describe("Monad則"){
    /*
     * left unit: 単位元律
     *   (unitM a) 'bindM' k =  k a
     *    unitM(a) flatMap k => k(a)
     *    for {
     *      b <- unitM(a)
     *    } yield k(b)
     *    => k(a)
     * right unit: 同一律
     *   m 'bindM' unitM =  m
     *   m flatMap unitM => m
     *   for {
     *     a <- m
     *   } yield unitM(a)
     *   => m
     * associativity: 結合律
     *   m 'bindM' (\a -> ((k a) 'bindM' h)) = (m 'bindM' (\a -> k a)) 'bindM' h
     *   m flatMap k flatMap h               => m flatMap { a => k(a) flatMap h}
     *   for {
     *     a <- m
     *     b <- k(a)
     *   } yield h(b)
     *   =>
     *   val b = for {
     *     a <- m
     *   } yield k(a)
     *   b flatMap h
     */
    it("left unit: 単位元律"){
      val k : Num => CMonad[Num] = (n: Num) => unitM(n)
      forAll { n:Int => {
        val a = Num(n)
        ((unitM(a) flatMap k) in id) should equal {
          k(a) in id
        }
      }}
    }
    it("right unit: 同一律"){
      forAll { n:Int => {
        val m = unitM(Num(n))
        ((m flatMap unitM) in id) should equal {
          m in id
        }
      }}
    }
    it("associativity: 結合律"){
      forAll(Gen.posNum[Int]) { n:Int => {
        val m : CMonad[Num] = unitM(Num(n))
        val k : Num => CMonad[Num] = (n: Num) => unitM(n)
        val h : Num => CMonad[Num] = (n: Num) => unitM(n)
        // val h : Fun => CMonad[Fun] = (fun => unitM(Fun(a => unitM(Num(n)))))
        // val h : Fun => CMonad[Fun] = (n => unitM(Fun(a => n)))
        ((m flatMap k flatMap h) in id) should equal {
          (m flatMap { a => k(a) flatMap h}) in id
        }
      }}
    }
  }
}
