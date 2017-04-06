package wadler

object zero {
  /** 
   * monad. 
   */
  case class M[A](answer:A) {
    def bindM[B](k: A => M[B]):M[B]     = M[B](k(answer).answer)
    def map[B](f: A => B): M[B]        = bindM(x => unitM(f(x)))
    def flatMap[B](f: A => M[B]): M[B] = bindM(f)
  }
  def unitM[A](a: A):M[A] = M[A](a)

  type Name = String
  type Answer = Value

  sealed trait Term
  case class Var(x: Name) extends Term
  case class Con(n: Int) extends Term
  case class Add(l: Term, r: Term) extends Term
  case class Lam(x: Name, body: Term) extends Term
  case class App(fun: Term, arg: Term) extends Term

  trait Value
  case object Wrong extends Value {
   override def toString() = "<wrong>"
  }
  case class Num(n: Int) extends Value {
    override def toString() = n.toString()
  }
  case class Fun(f: Value => M[Value]) extends Value {
    override def toString() = "<function>"
  }

  type Environment = List[Pair[Name, Value]]

  def showval(value: Value): String = value.toString()
  
  def interp(t: Term, e: Environment): M[Value] = t match {
    case Var(x) => lookup(x, e)
    case Con(n) => unitM(Num(n))
    case Add(l, r) => {
      for {
        a <- interp(l, e)
		b <- interp(r, e)
		c <- add(a, b)
      } yield c
    }
    case Lam(x, body) => unitM(Fun(a => interp(body, Pair(x, a) :: e)))
    case App(f, t) => {
      for {
        a <- interp(f, e)
		b <- interp(t, e)
		c <- apply(a, b)
      } yield c
    }
  }

  def lookup(x: Name, e: Environment): M[Value] = e match {
    case List() => unitM(Wrong)
    case Pair(y, b) :: e1 => if (x == y) unitM(b) else lookup(x, e1)
  }


  def add(a: Value, b: Value): M[Value] = Pair(a, b) match {
    case Pair(Num(m), Num(n)) => unitM(Num(m + n))
    case _ => unitM(Wrong)
  }
  
  def apply(a: Value, b: Value): M[Value] = a match {
    case Fun(k) => k(b)
    case _ => unitM(Wrong)
  }
  

  def id[A] = (x: A) => x
  def showM(m: M[Value]): String = id(m.answer).toString()
  def test(t: Term): String = showM(interp(t, List()))
  

}
