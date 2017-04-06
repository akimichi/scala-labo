import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * Pattern Match の基本的な用法を示す
 */


class PatternMatchSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("例外MatchError"){
    val ch:Char = 'x'
    intercept[scala.MatchError]{
      val sign = ch match {
        case '+' => 1
        case '-' => -1
      }
    }
  }
}


