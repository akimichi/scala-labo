import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * trait の基本的な用法を示す
 */
class TraitSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("traitを用いて") {
    describe("コンパニオンオブジェクト") {
      trait Namespace {
        trait Trait {
          val member:Int
        }
        // traitを同じ名称のobjectを作ると、それがコンパニオンオブジェクトである
        object Trait {
          def apply(member:Int):Trait = new Trait {
            val member:Int = member
          }
        }
        
      }
    }
    describe("入れ子のコンパニオンオブジェクト") {
      trait Trait {
        val member:Int
        
        object Trait {
          def apply(member:Int):Trait = new Trait {
            val member:Int = member
          }
        }
        
      }
    }
    it("抽象型メンバー abstract type を利用する") {
      object choices {
        trait Base {
          type O
          type A<:O
          type B<:O
        }
        trait Alt extends Base {
          val choose:A=>B=>O
        }
      }
      import choices._
      trait ConcreteAlt extends Alt {
        type O = Any
        type A = Int
        type B = Double
        override val choose:A=>B=>O = (a => b => a)
      }
      val choice = new ConcreteAlt {}
      choice.choose(1)(1.0) should equal(1)

      trait SubAlt extends Base {
        type C<:O        
        val choose:A=>B=>C=>O
      }
      trait SubConcreteAlt extends SubAlt {
        type O = Any
        type A = Int
        type B = Double
        type C = String
        override val choose:A=>B=>C=>O = (a => b => c => c)
      }
      val subchoice = new SubConcreteAlt {}
      subchoice.choose(1)(1.0)("1") should equal("1")
      
    }
    /**
     * Class linerization については、"Scalable Component Abstractions" に詳しい
     */ 
    it("traitの線形化を検証する") {
      trait T0 {
        def method:Int = 0
      }
      trait T1 extends T0 {
        override def method:Int = 1
      }
      trait T2 extends T0 {
        override def method:Int = 2
        def supercall:Int = super.method
      }
      trait Mixed extends T1 with T2

      val mixed = new Mixed {}
      mixed.method should equal(2)
      mixed.supercall should equal(1)
    }
    
    // (horstmann12:_scala_impat): Scala; traitのレイヤー化,p.116
    it("traitからobjectを派生したコンパニオンオブジェクト") {
      trait Base {
        val content:String
      }
      object Base extends Base {
        val content:String = "content"
      }
      Base.isInstanceOf[Base] should equal(true)
      Base.content should equal("content")
      val overridedContend = new Base {
        val content = "overrided content"
      }
      overridedContend.content should equal("overrided content")
    }
    it("レイヤー化されたtraitにおいて、パス依存型を用いて内部の trait にアクセスする") {
      trait Component {
        trait Item {
          val content:String
        }
      }
      object ConcreteComponent extends Component {
        val item:Component#Item = new Item  {
          override val content = "content in item"
        }
      }
      ConcreteComponent.item.content should equal("content in item")
    }
    it("レイヤー化されたtraitから object を構築する") {
      trait Component {
        val item:Item
        trait Item {
          val content:String
        }
      }
      object ConcreteComponent extends Component {
        val item = new Item  {
          override val content = "content in item"
        }
      }
      ConcreteComponent.item.content should equal("content in item")
    }
  }
}

