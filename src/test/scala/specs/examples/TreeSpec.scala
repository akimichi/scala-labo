import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


import org.kiama


/**
 * Tree の基本的な用法を示す
 */
class TreeSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Treeを用いて") {
    object test {
      sealed trait Tree[+A] { self =>
        val path:String
      }
      case class Leaf[A](path:String, value: A) extends Tree[A]
      case class Fork[A](path:String, left: Tree[A], right: Tree[A]) extends Tree[A]
    }
  }
}

