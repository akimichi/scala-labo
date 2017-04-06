import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


class ArraySpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Arrayで行列 matrix を表現する") {
    val matrix:Array[Array[Int]] = Array.ofDim[Int](2,2)
    matrix(0)(1) = 1
    matrix(1)(0) = 2
    matrix(1)(1) = 3
    it("要素をとりだす"){
      matrix(0)(1) should equal(1)      //res4: Int = 1
      matrix(1)(0) should equal(2)
    }
    it("次元をとりだす"){
      matrix.size should equal(2)
      matrix(0).size should equal(2)
    }
  }
}

