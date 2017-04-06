import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Iterableの基本的な用法を示す
 */
class IterableSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  def fixture = new {
    val list = (1 to 100).toList
    val vector = Vector(1,2,3,4,5)
  }
  describe("Iterableのメソッドを使う") {
    val iterable:Iterable[Int] = fixture.list
    iterable.takeRight(1) should equal(List(100))
    iterable.dropRight(99) should equal(List(1))
    iterable.reduceLeft{(accum,item) => accum + item} should equal(5050)
  }
  
}
