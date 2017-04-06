import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


class SetSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Setに対して") {
    import scala.collection.immutable.Set
    val set:Set[String] = Set("A","B","C","D")
    it("Set#contains"){
      set.contains("A") should equal(true)
    }
    it("Set#+"){
      (set + "E").contains("E") should equal(true)
    }
  }
  describe("SortedSetに対して") {
    import scala.collection.immutable.SortedSet
    val sorted_set = SortedSet(1,3,4,2)
    sorted_set should equal(Set(1,2,3,4))
    sorted_set.size should equal(4)
    it("SortedSet で foldLeft を使う"){
      sorted_set.foldLeft(0){(accum,elem) => accum + elem} should equal(10)
    }
    it("SortedSet で zip を使う"){
      SortedSet(1,3,4,2).zip(SortedSet(1,3,4,2,5)) should equal(SortedSet((1,1),(2,2),(3,3),(4,4)))
    }
  }
}


