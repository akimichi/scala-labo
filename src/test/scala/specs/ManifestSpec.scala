import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Manifestの基本的な用法を示す
 */
class ManifestSpec extends FunSpec with ShouldMatchers {
  describe("ClassManifest") {
    it("Arrayでは ClassManifest が不可欠である") {
      def first[T : ClassManifest](array:Array[T]):T = array(0)
      first(Array(1,2,3)) should equal(1)
	}
  }
}
