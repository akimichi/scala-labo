import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import org.scalacheck.{Gen}
import org.scalacheck.Prop.forAll


/*
 * Monoid
 * c.f. "Scala by Example",p.113
 * 
 */

trait monoid {
  
  abstract class SemiGroup[A] {
    def mappend(x: A, y: A): A
  }

  // MonoidはSemiGroup に unit 要素を加えたサブクラス
  trait Monoid[A] extends SemiGroup[A] {
    def mzero: A
  }
  object Monoid {
    // モノイドの実装
    implicit object StringMonoid extends Monoid[String] {
      def mappend(x: String, y: String): String = x.concat(y)
      def mzero: String = ""
    }
    
    implicit object IntMonoid extends Monoid[Int] {
      def mappend(x: Int, y: Int): Int = x + y
      def mzero: Int = 0
    }
      
    trait FuncMonoid[A,B] extends Monoid[A => B] {
      def mappend[C] = {(x:B=>C) => {(y:A=>B) =>
        x.compose[A](y)
      }}
      def mzero[C] = {a:C => a}
    }
  }
}


class MonoidCheck extends FunSpec with ShouldMatchers with GeneratorDrivenPropertyChecks with monoid {
  describe("monoid") {
    it("mappend"){
      def sum(list:List[Int])(implicit intMonoid:Monoid[Int]):Int = {
        list.foldRight(intMonoid.mzero)(intMonoid.mappend)
      }
      sum(List(1,2,3,4,5)) should equal(15)
    }
  }
}



