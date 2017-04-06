import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import org.scalacheck.{Gen}
import org.scalacheck.Prop.forAll


/*
 * Monad
 * 
 * 
 */ 
class MonadCheck extends FunSpec with ShouldMatchers with GeneratorDrivenPropertyChecks {

  trait Monad[M[_]] {
    def unit[A] : A => M[A]
    def fmap[A,B] : (A => B) => (M[A] => M[B])
    def join[A] : M[M[A]] => M[A]
      
    def id[A](x:A):A = x
    def apply[A](x : A) : M[A] = unit(x)
    def map[A,B](f: A=>B)(m : M[A]) : M[B] = fmap(f)(m)
    def flatMap[A,B](f: A=>M[B])(m : M[A]) : M[B] = join(fmap(f)(m))
  }
  /*
  describe("StateMonad"){
    trait StateMonad extends Monad[Pair[_,_]] {
      def run[A,S](initial:S) : Pair[A,S]
      def unit[A,S] : S => Pair[A,S] = (s:S) => run(s)
      def fmap[A,B,S] : (A => B) => (Pair[A,S] => Pair[B,S])
      def join[A,S] : Pair[A,Pair[A,S]] => Pair[A,S]
    }
  }
  */ 
  describe("IdentityMonad"){
    case class Identity[A](value:A) {
      def map[B](f:(A) => B) = Identity(f(value))
      def flatMap[B](f:(A) => Identity[B]) = f(value)
    }
    
    object IdentityMonad extends Monad[Option] {
      def unit[A] : A => Option[A] = (x:A) => Option[A](x)
      def fmap[A,B] : (A => B) => (Option[A] => Option[B]) = (f: A => B) => {
        (oa:Option[A]) => Option(f(oa.get))
      }
      def join[A] : Option[Option[A]] => Option[A] = (ooa:Option[Option[A]]) => {
        ooa.get
      }
    }
    describe("list内包を確認する") {
      it("[t | x <- u]"){
        forAll(Gen.posNum[Int]) { (i: Int) =>
          val u = IdentityMonad(i)
          val t = (x: Int) => scala.math.sqrt(x)
          IdentityMonad.map(t)(u) should equal {
            for(i <- u) yield scala.math.sqrt(i)
          }
        }
      }
    }
  }
  describe("ListMonad"){
    object ListMonad extends Monad[List] {
      def unit[A] : A => List[A] = (x:A) => List(x)
      def fmap[A,B] : (A => B) => (List[A] => List[B]) = (f: A => B) => {
        (xs:List[A]) => xs.map{f(_)}
      }
      def join[A] : List[List[A]] => List[A] = (xss:List[List[A]]) => {
        xss.flatMap{ xs:List[A] => xs}
      }
      def foldr[A,B](f:A => B => B, init:B, list:List[A]) : B = {
        list match {
          case Nil => init
          case x::xs => {
            f(x)(foldr(f,init,xs))
          }
        }
      }
      // def foldr[A,B] : (A => B => B) => B => List[A] => B = {(a => b)
      // }
    }
    describe("list内包を確認する") {
      it("[sqrt x | x <- [1,2,3...]]"){
        val sqrt = (x: Int) => scala.math.sqrt(x)
        forAll(Gen.containerOf[List,Int](Gen.choose(1,1000))) { xs => {
          ListMonad.map(sqrt)(xs) should equal {
            for(x <- xs) yield scala.math.sqrt(x)
          }
        }} 
        }
      it("[(x,y) | x <- [1,2], y <- [3,4]]"){
        ListMonad.join(ListMonad.map{x:Int => ListMonad.map{y:Int => (x,y)}(List(3,4))}(List(1,2))) should equal {
          List((1,3), (1,4), (2,3), (2,4))
        }
      }
      it("foldrでappendを実装する"){
        ListMonad.foldr({x:Int => {xs:List[Int] => x::xs}}, List(1,2,3), List(4,5,6)) should equal{
          List(4,5,6,1,2,3)
        }
        
      }
    }
  }
}


