import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * Combinatorの基本的な用法を示す
 */


class CombinatorSpec extends FunSpec with ShouldMatchers {
  /**
   * <b>Fixed Point Combinator is:</b>
   * Y = λf.(λx.f (x x)) (λx.f (x x))
   * 
   * <b>Proof of correctness:</b>
   * Y g  = (λf . (λx . f (x x)) (λx . f (x x))) g    (by definition of Y)
   * = (λx . g (x x)) (λx . g (x x))                  (β-reduction of λf: applied main function to g)
   * = (λy . g (y y)) (λx . g (x x))                  (α-conversion: renamed bound variable)
   * = g ((λx . g (x x)) (λx . g (x x)))              (β-reduction of λy: applied left function to right function)
   * = g (Y g)                                        (by second equality) [1]
   */
  describe("fixed point combinator"){
    def fix[A,B](f: (A=>B)=>(A=>B)): A=>B = {
      f(fix(f))(_)
    }
    val fact = fix[Int,Int](fact => a => if(a<=0) 1 else fact(a-1) * a)
    fact(10) should equal(3628800)
  }
  describe("kestrel combinatorについて"){
    //    * c.f. "Scala in Action",sec.10.3.2
    object test {
      implicit def kestrel[A](instance:A) = new {
        def tap(sideEffect: A => Unit):A ={
          import instance._
	      sideEffect(instance)
	      instance
        }
      }
    }
    it("tapメソッドで副作用をカプセル化できる"){
      import test._
      case class Person(val name:String, var address:Option[String] = None)
      Person("name").tap(person => {
        person.address = Some("address")
      }).address should equal(Some("address"))
    }
  }
}

