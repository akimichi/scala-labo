import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Symbolの基本的な方法を示す
 * 
 */
class SymbolSpec extends FunSpec with ShouldMatchers {
  describe("Symbolにおいて") {
    it("Symbol"){
      'abc should equal(Symbol("abc"))
    }
  }
}
