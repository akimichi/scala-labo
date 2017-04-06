import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * for構文の基本的な用法を示す
 */
class ForSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("forを用いて") {
    it("内包表現 comprehension を使う") {
      val factors : Int => Seq[Int] = (n:Int) => {
        val range = List.range(1,n + 1)
        for {
          x <- range
          if n % x == 0
        } yield x
      }
      factors(10) should equal(List(1,2,5,10))
      factors(7) should equal(List(1,7))

      val isPrime : Int => Boolean = (n:Int) => {
        factors(n) == List(1,n)
      }
      isPrime(15) should equal(false)
      isPrime(7) should equal(true)

      val primes : Int => Seq[Int] = (n:Int) => {
        val range = List.range(2,n + 1)
        for {
          x <- range
          if isPrime(x)
        } yield x
      }
      primes(40) should equal(List(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37))
    }
    it("forでOption値をとりだす") {
      for{
        the_option <- Some("option")
      } {
        the_option should equal("option")
      }
      
      val result = for{
        the_option <- Some("option")
      } yield {
        the_option
      }
      result should equal(Some("option"))
      
      val another_result = for{
        the_option <- None
      } yield {
        the_option
      }
      another_result.orElse(Some("option")) should equal(Some("option"))

      val yet_another_result = for{
        the_option <- None
      } yield {
        the_option
      }
      yet_another_result.getOrElse("option") should equal("option")
      
      // .getOrElse("none") match {
      //   case the_option => {
      //     the_option match {
      //       case result =>  result should equal("")
      //     }
      //   }
      // }
    }
    
    it("to 範囲を指定して反復する") {
      val list = List(1,2,3,4,5,6,7,8,9,10)
      val sublist = for(i <- 3 to 5) yield {
        list(i)
      }
      sublist.toList should equal(List(4,5,6))
    }
    it("to 範囲を指定して反復する際に、対象リストの範囲を超えてアクセスした場合は、例外 IndexOutOfBoundsException となる") {
      evaluating {
        val list = List(1,2,3,4,5,6,7,8,9,10)
        val sublist2 = for(i <- 3 to 20) yield {
          list(i)
        }
      } should produce [java.lang.IndexOutOfBoundsException] 
    }
    it("untilで範囲を指定して反復する") {
      val list = List(1,2,3,4,5,6,7,8,9,10)
      val sublist = for(i <- 3 until 5) yield {
        list(i)
      }
      sublist.toList should equal(List(4,5))
    }
  }
}

