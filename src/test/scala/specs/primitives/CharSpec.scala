import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * 文字の基本的な方法を示す
 * 
 */
class CharSpec extends FunSpec with ShouldMatchers {
  describe("Charにおいて") {
    it("toInt"){
      '1'.toInt - 48 should equal(1)
      '3'.toInt - 48 should equal(3)
    }
  }
}
