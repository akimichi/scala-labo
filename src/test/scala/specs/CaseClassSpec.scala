import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * case class の基本的な用法を示す
 */
class CaseClassSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("case classを用いて") {
    it("varを持つ case class を継承する") {
      case class BaseExample(val immutable_field:String, var mutable_field:Option[String] = None)
      class Example(override val immutable_field:String) extends BaseExample(immutable_field)
      
      val example = new Example("immutable_field")
      example.immutable_field should equal("immutable_field")
      example.mutable_field = Some("mutable_field")
      example.mutable_field should equal(Some("mutable_field"))
    }
    describe("case class の companion object"){
      object test {
        case class CaseClass(member:String)
        object CaseClass {
          implicit val default = CaseClass("the default CaseClass")
        }
        def use(arg:String)(implicit default:CaseClass) = default.member
      }
      test.use("dummy") should equal("the default CaseClass")
      
    }
    describe("Scala for the Impatient,p.190") {
      case class Example(val immutable_field:String, var mutable_field:String)

      it("初期化パラメータによってメンバーにアクセスできる") { // Scala for the Impatient,p.190
        val example = Example("immutable_field", "mutable_field")
        example.immutable_field should equal("immutable_field")
        example.mutable_field should equal("mutable_field")
        example.mutable_field = "mutable_field can be updated"
        example.mutable_field should equal("mutable_field can be updated")
      }

      it("copyメソッドで一部のvalメンバを変更する") { // Scala for the Impatient,p.190
        val example = Example("immutable_field", "mutable_field")
        val copied_example = example.copy(mutable_field = "mutable_field can be updated")
        copied_example.mutable_field should equal("mutable_field can be updated")
        val copied_example_for_immutable = example.copy(immutable_field = "immutable_field ALSO can be updated")
        copied_example_for_immutable.immutable_field should equal("immutable_field ALSO can be updated")
      }
    }
    describe("case classについて"){
      case class CaseClass(val member:String)
      it("copyメソッドでインスタンスを生成する"){
        val an_instance = CaseClass("member")
        val another_instance = an_instance.copy(member = "another")
        another_instance.member should equal("another")
      }
      it(""){
        trait Trait {}
        val an_instance = new CaseClass("member") with Trait
      }
    }
  }
}

