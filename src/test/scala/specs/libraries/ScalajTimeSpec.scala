import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


import org.scala_tools.time.Imports._


/**
 * Scalaj-timeライブラリ の基本的な用法を示す
 * c.f. https://github.com/jorgeortiz85/scala-time
 */
class ScalajTimeSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("ScalajTimeを用いて") {
    it("DateTimeを求める") {
      val now = DateTime.now
      assert(DateTime.nextMonth < DateTime.now + 2.months)
    }
  }
}

        


