import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import org.scalacheck.{Gen}
import org.scalacheck.Prop.forAll
// import org.specs2.mutable._
// import org.scalacheck._
// import org.scalacheck.Prop.forAll
// import org.scalacheck.{Prop, Properties}

class CategoryCheck extends FunSpec with ShouldMatchers with GeneratorDrivenPropertyChecks {
  object Category {
    def id[A]: A => A = a => a
    def compose[A, B, C](g: B => C, f: A => B): A => C =
      g compose f // This is Function.compose, not a recursive call!
  }
  
  import Category._
  
  val f = (i: Int) => i.toString
  val g = (s: String) => s.length
  val h = (i: Int) => i * i

  describe("カテゴリーにおいて、射は結合律 associativity を満す") {
    it("h . (g . f) == (h . g) . f"){
      forAll { (i: Int) =>
        compose(h, compose(g, f))(i) should equal{
          compose(compose(h, g), f)(i)
        }
      }
    }
  }
  describe("カテゴリーにおいて、同一律 identity law を満す") {
    it("f . id == id . f"){
      forAll { (i: Int) =>
        compose(f, id[Int])(i) should equal {
          compose(id[String], f)(i)
        }
      }
    }
  }
}
/*
  property("satisfy associativity") = Prop forAll { (i: Int) =>
    compose(h, compose(g, f))(i) == compose(compose(h, g), f)(i)
  }

  property("satisfy identity") = Prop forAll { (i: Int) =>
    compose(f, id[Int])(i) == compose(id[String], f)(i)
  } 
}
*/
