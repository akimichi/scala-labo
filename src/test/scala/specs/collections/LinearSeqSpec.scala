import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * LinearSeq の基本的な用法を示す
 */
class LinearSeqSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  import scala.collection.immutable.LinearSeq
  
  def fixture = new {
    val list = (1 to 100).toList
    val vector = Vector(1,2,3,4,5)
  }
  describe("LinearSeq を用いて traverse する(c.f.Scala in Depth,p.187)") {
    object test {
      sealed trait Tree[+A]
      case object NilTree extends Tree[Nothing]
      case class Fork[+A](value:A, left:Tree[A], right:Tree[A]) extends Tree[A]
      case class Leaf[+A](value:A) extends Tree[A]

      trait Walker[A,T] {
        def traverse[U](t:T)(f:A => U):Unit
      }

      object TreeWalker extends Walker[Int,Tree[Int]] {
        type A = Int
        type T = Tree[A]
        type U = A
        var _result:LinearSeq[Any] = LinearSeq()
        def result = _result
        def traverse[U](tree:T)(f:A => U):Unit = {
          @annotation.tailrec
          def traverseHelper(current:T, next:LinearSeq[T]):Unit = {
            current match {
              case Fork(value,left,right) => {
                _result = f(value) +: _result
                traverseHelper(left, right +: next)
              }
              case Leaf(value) if next.nonEmpty => {
                _result = f(value) +: _result
                traverseHelper(next.head, next.tail)
              }
              case Leaf(value) => {
                _result = f(value) +: _result
              }
              case NilTree if next.nonEmpty => {
                traverseHelper(next.head,next.tail)
              }
              case NilTree => ()
            }
          }
          traverseHelper(tree,LinearSeq())
        }
      }
    }
    it("結果を蓄積しながら二分木構造をtraverse する"){
      import test._
      val tree = Fork(1,
                      Fork(2,
                           Leaf(3),
                           Leaf(4)),
                      Fork(5,
                           Fork(6,
                                Leaf(7),
                                Leaf(8)),
                           Leaf(9)))
      
      TreeWalker.traverse(tree){ item =>
        // println(item)
        item
      }
      TreeWalker.result should equal(List(9,8,7,6,5,4,3,2,1))
    }
  }
}

