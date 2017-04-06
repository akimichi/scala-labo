import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * Design Pattern の基本的な用法を示す
 *
 */

/** Magnet Patern
 *  c.f. http://spray.io/blog/2012-12-13-the-magnet-pattern/
 *  c.f. http://yuroyoro.hatenablog.com/entry/2013/01/23/192244
 * 
 *
 */ 
class MagnetPatternSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Magnet Patternで引数の値に応じて結果型が変わるメソッドを定義できる") {
    trait Foo {
      type Result
      def bar:Result
    }
    val stringFoo = new Foo {
      type Result = String
      def bar:Result = "foo"
    }
    
    val intFoo = new Foo {
      type Result = Int
      def bar:Result = 99
    }
    // 引数のFooトレイトのResult型に応じて結果型が変わる
    it("stringFoo.barは文字列を返す"){
      stringFoo.bar should equal("foo")
    }
    it("intFoo.barはIntを返す"){
      intFoo.bar should equal(99)
    }
  }
}

/**
 * Strategy パターンは、アルゴリズムを実行時に選択することができるデザインパターンである
 * Scalaでは高階関数を渡すことで簡単に実現できる
 * 
 */ 
class StrategyPatternSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("無名関数でStrategy Pattern を準備する") {
    object test {
      def query(target:List[Int], filterStrategy:Int => Boolean, sortStrategy: (Int,Int) => Boolean): List[Int] = {
        target.filter(filterStrategy).sortWith(sortStrategy)
      }
    }
    it("無名関数をわたして実行する"){
      import test._
      val data = (1 to 100).toList
      val filterStrategy = (x:Int) => {x % 13 == 0}
      val sortStrategy = (x:Int, y:Int) => x > y
      query(data, filterStrategy, sortStrategy) should equal(
        List(91, 78, 65, 52, 39, 26, 13)
      )
    }
  }
  describe("クラスを用いて Strategy Pattern を準備する") {
    object test {
      trait Strategy {
        def run(target:List[Int]): List[Int]
      }
      class FilterStrategy(val strategy:Int => Boolean) extends Strategy {
        def run(target:List[Int]): List[Int] = target.filter(strategy)
      }
      class SortStrategy(val strategy:(Int,Int) => Boolean) extends Strategy {
        def run(target:List[Int]): List[Int] = target.sortWith(strategy)
      }
      
      def query(target:List[Int], filterStrategy:FilterStrategy, sortStrategy:SortStrategy): List[Int] = {
        sortStrategy.run(filterStrategy.run(target))
      }
    }
    it("戦略インスタンスをわたして実行する"){
      import test._
      val data = (1 to 100).toList
      val filterStrategy = new FilterStrategy((x:Int) => {x % 13 == 0})
      val sortStrategy = new SortStrategy((x:Int, y:Int) => x > y)
      query(data, filterStrategy, sortStrategy) should equal(
        List(91, 78, 65, 52, 39, 26, 13)
      )
    }
  }
}
