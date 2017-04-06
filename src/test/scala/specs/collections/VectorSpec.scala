import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


class VectorSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Vector") {
    it("Vector.range で連続値を生成する"){
      val vec = Vector.range(0,10)
      vec should equal( Vector(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))
    }

    it("Vectorはランダムアクセス可能なシーケンスである"){
      val vector = Vector.range(0,1e7.toInt)
      vector(1e7.toInt -1) should equal(1e7.toInt -1)
    }
  }
}



