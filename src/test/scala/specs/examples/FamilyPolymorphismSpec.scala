import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Family Polymorphism の例
 * * c.f. "Matching Objects With Patterns",p.19
 */
class FamilyPolymorphismSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("グラフ構造の例") {
    /**
     * c.f. http://www.familie-kneissl.org/Members/martin/blog/family-polymorphism-in-scala
     */ 
    abstract class Graph {
      type Node <: AbstractNode
      type Edge <: AbstractEdge
      
      def mkNode() : Node
      def connect(n1: Node, n2: Node) : Edge
      
      abstract class AbstractEdge(val n1: Node, val n2: Node) 
      
      trait AbstractNode {
        def touches(edge: Edge): Boolean = edge.n1 == this || edge.n2 == this
      }
    }
    class BasicGraph extends Graph {
      type Node = BasicNode
      type Edge = BasicEdge
      protected class BasicNode extends AbstractNode
      protected class BasicEdge(n1:Node, n2:Node) extends AbstractEdge(n1, n2)
      
      def mkNode() = new BasicNode
      def connect(n1: Node, n2: Node) : BasicEdge = new BasicEdge(n1, n2)
    }
    class OnOffGraph extends Graph {
      type Node = OnOffNode
      type Edge = OnOffEdge
      protected class OnOffNode extends AbstractNode {
        override def touches(edge: Edge): Boolean = edge.enabled && super.touches(edge)
      }
      protected class OnOffEdge(n1:Node, n2:Node, var enabled: Boolean) extends AbstractEdge(n1, n2)
      def mkNode() = new OnOffNode
      def connect(n1: Node, n2: Node) : OnOffEdge = new OnOffEdge(n1, n2, true)
    }
    it("BasicGraphでテストする"){
      val g = new BasicGraph
      val n1 = g.mkNode()
      val n2 = g.mkNode()
      val e = g.connect(n1,n2)
      assert(n1 touches e)
      assert(n2 touches e)
    }
  }
}
