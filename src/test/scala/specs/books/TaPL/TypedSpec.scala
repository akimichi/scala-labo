import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }

/*
 * Types and Programming Languages
 * 
 */ 
class TypedSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll with TypedLambda {
  import evaluator._
  describe("typed lambda") {
    it("terms"){
      Var("a").name should equal("a")
    }
    it("eval"){
      eval(App(Succ(),Num(1)),emptyEnv) should equal(2)
    }
  }
}

    

