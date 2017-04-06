import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * SelfTypeの基本的な用法を示す
 */
class SelfTypeSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("SelfTypeを用いて") {
    trait Dependent {
      val instance:String
    }
    trait Component { self: Dependent =>
      /**
       * selftype として Dependent が指定されているので、変数 instance があるものとして、 Component は自身の計算を構築できる
       */
      def getDependentInstance = instance
      val strictly_dependent_upon = instance // 無名クラスの場合は注入されない
      lazy val lazily_dependent_upon = instance // 評価時に値が注入される
    }
    describe("無名クラスを用いて依存する trait を利用する") {
      it("スーパークラスの初期化で scala.UninitializedFieldError をきたす") {
        evaluating {
          val component = new Component with Dependent {
            val instance = "this is an instance within Dependent"
          }
        } should produce [scala.UninitializedFieldError] 
	  }
      it("依存対象の変数を遅延評価に変更することで scala.UninitializedFieldError の問題を回避し、依存性を適切に注入できる ") {
        val component = new Component with Dependent {
          lazy val instance = "this is an instance within Dependent"
        }
        component.getDependentInstance should equal("this is an instance within Dependent")
        /* 依存性の注入は即時評価の val では不可能であり、遅延評価 lazy val を使う必要がある */
        component.strictly_dependent_upon should equal("this is an instance within Dependent")
        component.lazily_dependent_upon should equal("this is an instance within Dependent")
	  }
    }
    it("クラスによって、依存する trait を利用する") {
      class ComponentImpl(val instance:String) extends Component with Dependent
      val component = new ComponentImpl("this is an instance within Dependent")
      component.getDependentInstance should equal("this is an instance within Dependent")
      /* 無名クラスの場合とは異なり、正格評価であっても依存性が注入されている */
      component.strictly_dependent_upon should equal("this is an instance within Dependent")
      component.lazily_dependent_upon should equal("this is an instance within Dependent")
      
      /** コンパイルエラー: value getDependentInstance is not a member of Component
      class Component extends Component with Dependent {
        val instance = "this is an instance within Dependent"
      }
      */
      /** コンパイルエラー:
       class ComponentImpl(val instance:String) extends Component

       self-type ComponentImpl does not conform to Component's selftype Component with Dependent
       [error]       class ComponentImpl(val instance:String) extends Component
       [error]                                                        ^
      */
	}
  }
  describe("Scalable Component Abstractions の例において(失敗例)") {

    /**
    [error]  found   : BaseNode.this.type (with underlying type Graph.this.BaseNode)
    [error]  required: Graph.this.Node
    [error]         def connectWith(n:Node): Edge = new Edge(this,n)
                                                             ^    
    abstract class Graph {
      type Node <: BaseNode
      class BaseNode {
        def connectWith(n:Node): Edge = new Edge(this,n)
      }
      class Edge(from:Node, to:Node) {
        def source() = from
        def target() = to
      }
    }
    */
    trait Graph {
      type Node
      trait NodeInterface {
        def connectWith(n:Node): Edge
      }
      class BaseNode extends NodeInterface { self: Node =>
        def connectWith(n:Node): Edge = new Edge(this,n)
      }
      class Edge(from:Node, to:Node) {
        def source() = from
        def target() = to
      }
    }
    // it("BaseNodeもNodeとして振舞える") {
    //   class ConcreteGraph extends Graph {
    //     type Node <: BaseNode
    //   }
      
    //   val graph = new ConcreteGraph {}
    //   import graph._
    //   val node1 = new BaseNode {}
    //   val node2 = new BaseNode {}
    //   val edge = node1.connectWith(nodw2)
    // }
  }
  describe("traitの一方参照") {
    trait helpers {
      val fixture = new {
        val data = 1
      }
    }
    trait anotherHelpers {
      val helpersInstance = new helpers {}
      val fixture = new {
        val data = helpersInstance.fixture.data
      }
    }
    class Test extends anotherHelpers  {
      def data = fixture.data
    }
    val test = new Test
    test.data should equal(1)
  }
}


