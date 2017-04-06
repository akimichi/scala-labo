import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

class TrySpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {

  describe("try-catch") {
    
    
    /*
    import scalax.io.Resource
    it("try-catch-finally"){
      val source:Source = try {
        throw new Exception()
      } catch {
        case e: IllegalArgumentException => println("illegal arg. exception");
        case e: IllegalStateException    => println("illegal state exception");
        case e: IOException              => println("IO exception");
        source
      } finally {
        source.close()
        // println("this code is always executed");
      }
    }
    */
  }
}
