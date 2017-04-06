import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Higher-Kinded type の基本的な用法を示す
 * c.f. 
 */
class HigherKindedSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  
  describe("Higer Kindedを用いて") {
    describe("Iterableを作る"){
      /*
       * c.f. "An Introduction to Generics of a Higher Kind in Scala"
       */
      trait Iterable[Elem,Container[_]] {
        def filter(p:Elem => Boolean):Container[Elem]
        def map[T](f :Elem => T) : Container[T]
        def flatMap[T](f :Elem => Container[T]) : Container[T]
      }
      case class IterableList[Elem](val elems:List[Elem]) extends Iterable[Elem,List] {
        def filter(p:Elem => Boolean):List[Elem] = elems.filter(p)
        def map[T](f :Elem => T) : List[T] = elems.map(f)
        def flatMap[T](f :Elem => List[T]) : List[T] = elems.flatMap(f)
      }
      it("IterableList[Int]で実体化する"){
        val int_list:IterableList[Int] = IterableList[Int](List(1,2,3))
        int_list.filter{item => (item % 2) == 0} should equal(List(2))
      }
    }
    describe("汎用コンテナ型を作る"){
      /*
       * c.f. http://twitter.github.com/scala_school/advanced-types.html
       */ 
      trait Container[M[_]] {
        def put[A](x: A): M[A]
        def get[A](m: M[A]): A
      }
      it("List型"){
        val list = new Container[List] {
          def put[A](x: A) = List(x)
          def get[A](m: List[A]) = m.head
        }
        list.put("hey") should equal(List("hey"))
        list.put(123) should equal(List(123))
      }
      
      it("Vector型"){
        val vector = new Container[Vector] {
          def put[A](x: A) = Vector(x)
          def get[A](m: Vector[A]) = m(0)
        }
        vector.put("hey") should equal(Vector("hey"))
        vector.put(123) should equal(Vector(123))
      }
    }
  }
}
