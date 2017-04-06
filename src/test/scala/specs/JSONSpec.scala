
/**
 *
 * scala / src / library / scala / util / parsing / json / JSON.scala
 *
 */

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * JSONの基本的な方法を示す
 * 
 */
class JSONSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("scala.util.parsing.json.JSONを利用して") {
    import scala.util.parsing.json._

    it("JSONをパースする"){
      JSON.parseFull("[12]") should equal(Some(List(12.0)))
      JSON.parseFull("""{"key":true}""") should equal(Some(Map("key" -> true)))
    }
  }
}

