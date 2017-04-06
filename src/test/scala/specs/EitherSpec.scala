import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Either型の基本的な用法を示す
 */
class EitherSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Eitherを用いて") {
    it("getOrElse"){
      val either:Either[String,Boolean] = Left("the_value")
      // either.orElse("") should equal("the_value")
    }
  }
}
    

