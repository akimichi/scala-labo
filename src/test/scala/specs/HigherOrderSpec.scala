import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Higher order function  の基本的な用法を示す
 */
class HigherOrderSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("副作用を高階関数 higher-order function で処理する") {
    class Model(val field:String) {
      var mutable:Option[String] = None
    }
    object Model {
      def apply(field:String)(sideEffect:Model => Unit = {instance => {} }):Model = {
        val instance = new Model(field)
        sideEffect(instance)
        instance
      }
    }
    val instance = Model("field"){instance =>
      instance.mutable = Some("mutable")
    }
    instance.field should equal("field")
    instance.mutable should equal(Some("mutable"))
  }
  describe("高階関数 higher-order function") {
    val times10:Int => Int =
      (i:Int) => {
        i * 10
      }
    times10(10) should equal(100)
    it("filterを定義する"){
      def filter(target:Seq[Int])(filterBy:(Int) => Boolean):Seq[Int] = {
        for {
          elem:Int <- target
          if filterBy(elem)
        } yield {
          elem
        }
      }
      filter(List(1,2,3,4,5)){elem =>
        elem % 2 == 0
      } should equal(List(2,4))
      val odd:Int => Boolean = {(elem) =>
        elem % 2 != 0
      }
      filter(List(1,2,3,4,5))(odd) should equal(List(1,3,5))
    }
    it("Y Combinator"){
      /*
       * Y = λf.((λx.f (x x)) (λx.f (x x)))
       */
      
      /*
       * fact = λf.λn.if (n = 0) 1 (n * f(n - 1))
       */ 
      
    }
  }
  
}


