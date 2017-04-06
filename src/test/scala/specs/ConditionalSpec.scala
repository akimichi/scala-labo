import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * 条件分岐の基本的な用法を示す
 */


class ConditionalSpec extends FunSpec with ShouldMatchers {
  describe("ifについて"){
    it("even"){
	  def even(number:Int): Boolean = {
		if(number % 2 == 0)
		  true
		else
		  false
	  }
	  even(2) should equal(true)
	  even(3) should equal(false)
    }
  }
  
}

