import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * object の基本的な用法を示す
 */
class ObjectSpec extends FunSpec with ShouldMatchers with scala_labo.helpers {
  describe("objectによるシングルトン singleton について"){
    object test {
      var member:String = "initial"
    }
    it("object内の変数を変更する"){
      import test._
      test.member should equal("initial")
      test.member = "changed"
      test.member should equal("changed")
    }
    
  }
  describe("companion object"){
    it("同じ名前の class と object があるとき、その object を companion object という") {
      class Account {
        val id = Account.newUniqueNumber() // class Account内からアクセス可能だが、スコープ内ではない。
        private var balance = 0.0
        def deposit(amount: Double) { balance += amount }
      }
      
      object Account { // The companion object
        def apply() = new Account
        private var lastNumber = 0
        def newUniqueNumber() = { lastNumber += 1; lastNumber }
      }
      val account01 = Account()
      val account02 = Account()
      account01.id should equal(1)
      account02.id should equal(2)
    }
    it("case class の companion object"){
      object test {
        case class CaseClass(member:String)
        object CaseClass {
          implicit val default = CaseClass("the default CaseClass")
        }
        def use(arg:String)(implicit default:CaseClass) = default.member
      }
      test.use("dummy") should equal("the default CaseClass")
      
    }

    it("trait の companion object"){
      object test {
        trait CaseClass {
          val member:String
        }
        object CaseClass {
          implicit val default = new CaseClass {
            val member = "the default CaseClass"
          }
        }
        def use(arg:String)(implicit default:CaseClass) = default.member
      }
      test.use("dummy") should equal("the default CaseClass")
      test.use("dummy")(new test.CaseClass { val member = "overrided"}) should equal("overrided")
    }
  }
}
