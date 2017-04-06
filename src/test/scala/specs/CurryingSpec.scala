import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Curryingの基本的な用法を示す
 */
class CurryingSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Introduction to Functional Programming using Haskell,p.12") {
    val twice:(Int => Int) => (Int => Int) = { f:(Int => Int) =>
      f compose f
    }
    val square: Int => Int = { x:Int =>
      x * x
    }
    twice(square)(2) should equal(16)
    val quad: Int => Int = twice(square)
    quad(2) should equal(16)
  }
  describe("カリー化関数とメソッド") {
    object test {
      val normal_function = (arg1:String,arg2:String) => "%s:%d".format(arg1,arg2.length)
      val curried_function = (arg1:String) => (arg2:String) => "%s:%d".format(arg1,arg2.length)
      def curried_method(arg1:String)(arg2:String):String = "%s:%d".format(arg1,arg2.length)
    }
    it("curried を使って通常の関数をカリー化する") {
      import test._
      val curried_function = normal_function.curried
      curried_function("arg1")("arg2") should equal("arg1:4")
    }
    it("カリー化関数 curried_function を使う") {
      import test._
      val delayed_function = curried_function("arg1")
      delayed_function("arg1") should equal("arg1:4")
    }
    it("カリー化メソッド curried_method を使う") {
      import test._
      val delayed_method = curried_method("arg1") _ // <-- カリー化メソッドを関数に変更する場合は _ が必要
      delayed_method("arg1") should equal("arg1:4")
    }
  }
  describe("curry化を用いた関数抽象(Couseraのコース 2.3の例)") {
    object test {
      def sum(func:Int => Int)(a:Int, b:Int):Int =
        if(a > b)
          0
        else
          func(a) + sum(func)(a + 1,b)
      def cube(x:Int):Int = x * x * x
    }
    it("curry化sum を使う") {
      import test._
      sum(cube)(1,10) should equal(3025)
      sum(x => x)(1,10) should equal(55)
    }
  }
  describe("curry化を用いた mapReduce抽象(Couseraのコース 2.3の例)") {
    object test {
      def mapReduce(map: Int => Int, reduce:(Int,Int) => Int, zero:Int)(a:Int,b:Int): Int = {
        if(a > b) zero
        else reduce(map(a), mapReduce(map, reduce, zero)(a+1,b))
      }
      def product(map:Int => Int)(a:Int, b:Int):Int = mapReduce(map, (x,y) => x*y, 1)(a,b)
    }
    it("mapReduceを実行する") {
      import test._
      product(x => x*x)(3,4) should equal(144)
    }
  }
}
