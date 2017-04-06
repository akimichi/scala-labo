import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Option型の基本的な用法を示す
 */
class OptionSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Optionを用いて") {
    it("get"){
      val option:Option[String] = Some("the_value")
      option.get should equal("the_value")
      option.getOrElse("") should equal("the_value")
      
      val none = None
      none.getOrElse("nil") should equal("nil")
    }
    it("nullをOption型に変換する") {
      Option(null).isInstanceOf[Option[_]] should equal(true)
    }
    it("Seq.flattenでNoneを消す"){
      val seq = Seq(Some(1),None,Some(3),None)
      seq.flatten should equal(List(1,3))
    }
  }
}
    

