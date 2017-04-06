import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Traversable の基本的な用法を示す
 */
class TraversableSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  def fixture = new {
    val list = (1 to 100).toList
    val vector = Vector(1,2,3,4,5)
  }
  describe("MyTraversable"){
    abstract class MyTraversable[+M[_],T](implicit ev:M[_] <:< Traversable[T]) extends Traversable[M[_]] {
      def foreach[U](f: T => U) : U        
      // def foreach[U](f: T => U) : U
    }
  }
  
  describe("evidence parameter を使う(c.f. Scala in Depth,p.157)") {
    object test {
      def peek[ElemType,ColType](col:ColType)(implicit ev:ColType <:< Traversable[ElemType]):Pair[ElemType,ColType] = (col.head,col)
      def peek_wrong[ElemType,ColType <: Traversable[ElemType]](col:ColType) = (col.head,col)
    }
    it("evidence parameter は型推論を補助する"){
      import test._
      peek(fixture.list) should equal(Pair(1,fixture.list))
      /*
       * 下記の呼出しはコンパイルエラーとなる
       peek_wrong(fixture.list) should equal(Pair(1,fixture.list))
       */ 
    }
  }
  
  describe("Traversableのfilterを用いて処理を合成する(c.f. Scala in Action,sec.5)") {
    object test {
      def filter[T](criteria: T => Boolean)(col: Traversable[T]) = col.filter(criteria)
      def even: Int => Boolean = _ % 2 == 0
      def odd: Int => Boolean = _ % 2 == 1
      def not: Boolean => Boolean = !_
      def map[T,U](f:T => U)(col:Traversable[T]) = col.map(f)
      def double: Int => Int = _ * 2
    }
    it("filterを使う"){
      import test._
      filter({i:Int => i == 99})(fixture.list) should equal(List(99))
      filter({i:Int => i == 3})(fixture.vector) should equal(Vector(3))
    }
    it("andThen で filterを合成する"){
      import test._
      val evenFilter = filter(even) _
      info("偶数を2倍するので、偶数の列になる")
      (evenFilter andThen map(double))(fixture.list) forall(even) should equal(true)
    }
    it("compose で filterを合成する"){
      import test._
      val evenFilter = filter(even) _
      info("compose は andThen と適用の順序が逆になる")
      (map(double) _ compose evenFilter)(fixture.list) forall(even) should equal(true)
    }
  }
  
}
