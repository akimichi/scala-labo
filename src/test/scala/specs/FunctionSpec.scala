import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * 関数に関するテスト
 */

class FunctionSpec extends FunSpec with ShouldMatchers  with BeforeAndAfterAll {
  describe("PartialFunction"){

    val onlyEven:PartialFunction[Int,Int] = {
      case elem:Int if elem % 2 == 0 => elem
    }
    val onlyLessThan10:PartialFunction[Int,Int] = {
      case elem:Int if elem < 10 => elem
    }
    val onlyMoreThan10:PartialFunction[Int,Int] = {
      case elem:Int if elem >= 10 => elem
    }
    it("collectと partial function を組みあわせて使う"){
      List(1,2,3,4,5,6,7,8,9,10,11).collect(onlyEven) should equal(List(2,4,6,8,10))
      List(1,2,3,4,5,6,7,8,9,10,11).collect(onlyEven orElse onlyMoreThan10) should equal(List(2,4,6,8,10,11))
    }
    it("map,filterと組み合わせて partial function を使う"){
      val pf:PartialFunction[String,Option[String]] = {
        case null => None
        case "" => None
        case s => Some(s)
      }
      val list = Seq( "foo",null,"bar","","","baz")
      list.filter{ pf.isDefinedAt }.map{ pf }.flatten should equal{
        List("foo", "bar", "baz")
      }
    }
    it("state transition"){
      trait stm {
        type Transition = PartialFunction[State,State]
        case class State(name:String)
        def transit(transition:Transition) = transition
      }

      object STM extends stm {
        val start = State("start")
        val intermediate = State("intermediate")
        val end = State("end")
        val init:Transition = transit {
          case State("start") => intermediate
        }
        val subsequent:Transition = transit {
          case State("intermediate") => end
        }
      }
      import STM._
      init(start) should equal(intermediate)
      (init andThen subsequent)(start) should equal(end)
      
    }
    // for {
    //   item <- List(1,2,3,4,5,6,7,8,9,10,11)
    // } yield {
    //   (onlyEven andThen onlyLessThan10)(item)
    // } should equal(1)
    
  }
}
