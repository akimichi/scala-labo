import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * 継承の基本的な用法を示す
 */
class InheritanceSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("object用いて") {
    it("traitからobjectを派生する") {
      trait Base {
        val content:String
      }
      object Sub extends Base {
        val content:String = "content"
      }
      Sub.isInstanceOf[Base] should equal(true)
      Sub.content should equal("content")
    }
    it("traitからclassを派生する") {
      trait Base {
        val content:String
      }
      class Sub(val content:String) extends Base

      val sub = new Sub("content")
      sub.isInstanceOf[Base] should equal(true)
      sub.content should equal("content")
    }
  }
}

