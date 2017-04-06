import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * collectionの基本的な用法を示す
 */
class CollectionSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("ノードパスを表現する項 NodeCell"){
    /*
     * ノードパスを表現する項 NodeCell
     * Seq((RmAttribute(rmAttrName), RichNodeId),(RmAttribute(rmTypeName), RichNodeId),(RmAttribute(rmTypeName), RmTypeTerm))
     * 末端が必ずRM型 RmTypeTerm となる
     * e.x.
     *   NodePathTerm :=
     *   Seq(NodeCell(RmAttribute("data"), NodeId("at0001")),
     *       NodeCell(RmAttribute("events"), NodeId("at0002")),
     *       NodeCell(RmAttribute("data"), NodeId("at0003")),
     *       NodeCell(RmAttribute("items"), NodeId("at0300")),
     *       EndNodeCell(RmAttribute("value"), RmTypeTerm("DvQuantity")))
     * 
     */
    object test {
      case class NodeCell()
      trait NodePathTerm[+T] {
        def head: T
        def tail: NodePathTerm[T]
        def isEmpty: Boolean
        def ::[U >: T](item: U): NodePathTerm[U]
        // def ::[NodeCell >: T](item: NodeCell): NodePathTerm[NodeCell]
        def map[U](f: T => U): NodePathTerm[U] = if (isEmpty) EndNodeCell()
                                                 else f(head) :: tail.map(f)
      }
      case class NodePathTermImpl[T](val head: T, val tail: NodePathTerm[T]) extends NodePathTerm[T] {
        def isEmpty: Boolean = false
        def ::[U >: T](item: U): NodePathTerm[U] = NodePathTermImpl(item, this)
      }

      case class EndNodeCell() extends NodePathTerm[Nothing] {
        def isEmpty: Boolean = true
        def ::[U>:Nothing](item: U): NodePathTerm[U] = NodePathTermImpl(item, this)
        def head: Nothing = throw new NoSuchElementException("no head in empty list")
        def tail: NodePathTerm[Nothing] = throw new NoSuchElementException("no tail in empty list")
      }
      
    }
    it("NodePathTerm"){
      import test._
      val cell_path_term: NodePathTerm[NodeCell] =  NodeCell() :: NodeCell() :: NodeCell() :: EndNodeCell()
      cell_path_term.head should equal(NodeCell())
      // val string_path_term =  "ABC" :: "XYZ" :: "123" :: EndNodeCell()
      // string_path_term.head should equal("ABC")
    }
  }
    describe("自前のListを定義する"){
      object test {
        trait MyList[+T] {
          def isEmpty: Boolean
          def head: T
          def tail: MyList[T]
          def ::[U>:T](item: U): MyList[U]
          def map[U](f: T => U): MyList[U] = if (isEmpty) MyNil
                                             else f(head) :: tail.map(f)
          /* うまくいかない
          def reduceLeft[U >: T](op: (U, U) => U): T = {
            def fold(init: U)(op: (U, U) => U): U = this match { 
              case MyNil => init
              case _ => {
                (tail.fold(op(init,head)))(op)
              }
            }
            this match { 
              case MyNil => sys.error("MyNil.reduceLeft") 
              case list:MyList[T] => {
                (list.tail.fold(list.head))(op)
                // (tail.fold(head))(op)
              }
            }
          }
          def fold[U >: T](init: U)(op: (U, U) => U): U = this match { 
            case MyNil => init
            case _ => {
              (tail.fold(op(init,head)))(op)
            }
          }
          */ 

        }
        // object MyList {
        //   def apply[T](head:T,tail:MyList[T]):MyList[T] = MyListImpl(head,tail)
        //   def unapply[T](list:MyList[T]): Option[MyList[T]] = Some(MyListImpl(list.head,list.tail))
        // }
        case class MyListImpl[T](val head: T, val tail: MyList[T]) extends MyList[T] {
          def ::[U>:T](item: U): MyList[U] = new MyListImpl(item, this)
          def isEmpty = false
        }
        case object MyNil extends MyList[Nothing] {
          def ::[U>:Nothing](item: U): MyList[U] = MyListImpl(item, MyNil)
          
          override def isEmpty = true
          def head: Nothing = throw new NoSuchElementException("no head in empty list")
          def tail: MyList[Nothing] = throw new NoSuchElementException("no tail in empty list")
        }
      }
      it("MyListを使う"){
        import test._
        val my_string_list =  "ABC" :: "XYZ" :: "123" :: MyNil
        my_string_list.head should equal("ABC")
        my_string_list.tail should equal("XYZ" :: "123" :: MyNil)
        val my_int_list =  1 :: 2 :: 3 :: MyNil
        my_int_list.head should equal(1)
        my_int_list.map{_.toString} should equal{
          "1" :: "2" :: "3" :: MyNil
        }
      }
    }
  describe("存在型でコレクションの型を保存する"){
    def mkFromSeq[T<:Seq[_]](source:T):T = source
    mkFromSeq(List(1,2,3)) should equal(List(1,2,3))
    mkFromSeq(List(1,2,3)).head should equal(1)
  }

}

