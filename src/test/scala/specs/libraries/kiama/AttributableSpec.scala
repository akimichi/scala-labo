import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


import org.kiama


/**
 * Kiamaライブラリ の Attributable の基本的な用法を示す
 */
class AttributableSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("木構造の例") {
    import org.kiama._
    import org.kiama.attribution.Attributable
    import org.kiama.attribution.Attribution._
    
    object test {
      
      abstract class Tree(val path:String) extends Attributable {
        // def delayed[T](a: => T) = a
      }
      case class Fork(override val path:String,left:Tree,right:Tree) extends Tree(path)
      case class Node(override val path:String) extends Tree(path) {
        def isNode:Boolean = true
      }
    }
    it("pathで検索する") {
      import test._
      
      trait DriverInterface[T>:Tree] {
        val target_path:String
        val find: Tree => Option[T]
      }
      // ここで下限境界 lower bound を使うのがミソ 
      class Driver[T>:Tree](val target_path:String) extends DriverInterface[T] {
        val find:Tree => Option[T] =
          attr {
            case t @ Fork(the_path, l,r) => {
              if(t.path == target_path)
                Some(t)                
              else
                find(t.left) match {
                  case Some(the_node) => Some(the_node)
                  case None => find(t.right)
                }
            }
            case t @ Node(the_path) => {
              if(the_path == target_path)
                Some(Node(the_path))
              else
                None
            }
          }
      }
      val tree = Fork("root", Node("root/1"), Fork("root/2", Node("root/2/node/1"),Node("root/2/node/2")))
      initTree(tree) // これが必要
      val driver = new Driver("root")
      tree.isRoot should equal(true)
      driver.find(tree) should equal(Some(tree))
      (new Driver("root/2/node/1")).find(tree) should equal(Some(Node("root/2/node/1")))
      val result = (new Driver("root/2/node/1")).find(tree)
      // result.get.delayed(result.get.isNode _) should equal(false)
      // (new Driver("root/2/node/2")).find(tree).get.isNode should equal(false)
    }
  }
  describe("Attributable を用いて") {
    import org.kiama._
    import org.kiama.attribution.Attributable
    import org.kiama.attribution.Attribution._

    object test {
      abstract class Tree extends Attributable
      case class Fork(left:Tree,right:Tree) extends Tree
      case class Leaf(value:Int) extends Tree
    }
    it("repminを計算する") {
      import test._

      trait Repmin {
        val repmin : Tree => Tree
        val locmin:Tree => Int
        val globmin:Tree => Int
      }
      object Repmin extends Repmin {
        val locmin:Tree => Int =
          attr {
            case Fork(l,r) => locmin(l) min locmin(r)
            case Leaf(v) => v
          }
        val repmin:Tree => Tree =
          attr {
            case Fork(l,r) => Fork(repmin(l),repmin(r))
            case t:Leaf => Leaf(globmin(t))
          }
        val globmin:Tree => Int =
          attr {
            case t if t.isRoot => locmin(t)
            case t => globmin(t.parent[Tree])
          }
      }
      val tree = Fork(Leaf(3),Fork(Leaf(1),Leaf(10)))
      initTree(tree) // これが必要
      tree.isRoot should equal(true)
      Repmin.repmin(tree) should equal(Fork(Leaf(1),Fork(Leaf(1),Leaf(1))))
      Repmin.locmin(tree) should equal(1)
    }
  }
}

