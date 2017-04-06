import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import org.scalacheck.{Gen}
import org.scalacheck.Prop.forAll
// import org.scalacheck.{Prop, Properties}


/*
 *
 * 関手（functor） F は2つの圏の構造を保持するマッピング（structure-preserving mapping）である
 * 
 */ 
class FunctorCheck extends FunSpec with ShouldMatchers with GeneratorDrivenPropertyChecks {

  trait Functor[T[_]] {
    def unit[A] : A => T[A]
    def fmap[A,B] : (A => B) => (T[A] => T[B])
      
    def id[A](x:A):A = x
    def apply[A](x : A) : T[A] = unit(x)
    def map[A,B](f: A=>B)(x : T[A]) : T[B] = fmap(f)(x)
    
  }
  object ListFunctor extends Functor[List] {
    def unit[A] : A => List[A] = (x:A) => List(x)
    def fmap[A,B] : (A => B) => (List[A] => List[B]) = (f: A => B) => {
      (xs:List[A]) => xs.map{f(_)}
    }
    def join[A] : List[List[A]] => List[A] = (xss:List[List[A]]) => {
      xss.flatMap{ xs:List[A] => xs}
    }
  }
  describe("list内包を確認する") {
    info("c.f. Comprehending Monads,p.5")
    it("[sqrt x | x <- [1,2,3...]]"){
      val sqrt = (x: Int) => scala.math.sqrt(x)
      forAll(Gen.containerOf[List,Int](Gen.choose(1,1000))) { xs => {
        ListFunctor.map(sqrt)(xs) should equal {
          for(x <- xs) yield scala.math.sqrt(x)
        }
      }} 
    }
    it("[(x,y) | x <- [1,2], y <- [3,4]]"){
      ListFunctor.join(ListFunctor.map{x:Int => ListFunctor.map{y:Int => (x,y)}(List(3,4))}(List(1,2))) should equal {
        List((1,3), (1,4), (2,3), (2,4))
      }
    }
  }
  describe("Functorの規則を確認する") {
    it("preserve identity"){
      def id[T] : T => T = (a: T) => a
      // val stringID = (s: String) => s
      // val stringListID = (xs: List[String]) => xs
      forAll { (xs: List[String]) =>
        ListFunctor.map(id[String])(xs) should equal {
          id[List[String]](xs)
        }
      } 
      // forAll { (xs: List[String]) =>
      //   ListFunctor.map(stringID)(xs) should equal {
      //     stringListID(xs)
      //   }
      // } 
      // forAll { (xs: List[String]) =>
      //   ListFunctor.map(stringID)(xs) should equal{
      //     ListFunctor.id(xs)
      //   }
      // } 
    }
    it("preserve composition: F(g . f) == F(g) . F(f)"){
      val f:Int => String = (i: Int) => i.toString
      val g:String => Int = (s: String) => s.length
      forAll { (xs: List[Int]) =>
        ListFunctor.map(g compose f)(xs) should equal {
          (ListFunctor.map(g) _ compose ListFunctor.map(f) _ )(xs)
        }
      }
    }
    it("map f . unit == unit . f"){
      val f : Int => String = (i: Int) => i.toString
      forAll { (x: Int) =>
        (ListFunctor.fmap(f) compose ListFunctor.unit[Int])(x) should equal {
          (ListFunctor.unit[String] compose f)(x)
        }
      }
    }
    it("map f . join == join . map(map f)") {
      val f : Int => String = (i: Int) => i.toString
      forAll { (xss: List[List[Int]]) =>
        (ListFunctor.fmap(f) compose ListFunctor.join[Int])(xss) should equal {
          (ListFunctor.join[String] compose ListFunctor.fmap(ListFunctor.fmap(f)))(xss)
        }
      }
    }
    it("join . unit == id") {
      forAll { (xs: List[Int]) =>
        (ListFunctor.join[Int] compose ListFunctor.unit[List[Int]])(xs) should equal {
          ListFunctor.id(xs)
        }
      }
    }
    it("join . map unit == id") {
      forAll { (xs: List[Int]) =>
        (ListFunctor.join[Int] compose ListFunctor.fmap[Int, List[Int]](ListFunctor.unit[Int]))(xs) should equal {
          ListFunctor.id(xs)
        }
      }
    }
    it("join . join == join . map join") {
      forAll { (xsss: List[List[List[Int]]]) =>
        (ListFunctor.join[Int] compose ListFunctor.join[List[Int]])(xsss) should equal {
          (ListFunctor.join[Int] compose ListFunctor.fmap[List[List[Int]], List[Int]](ListFunctor.join[Int]))(xsss)
        }
      }
    }
  }
}


