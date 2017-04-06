import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * CONCEPTパターンの基本的な用法を示す
 */
class CONCEPTPatternSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Typeclassを用いて") {
    trait ETag[A] {
      val source:A
      def calculate(calculator:ETagCalculator[A]):String = {
        calculator.calc(source)
      }
    }

    trait ETagCalculator[A] {
      def calc(source:A):String
    }

    object ETagCalculatorForString extends ETagCalculator[String] {
      def calc(source:String):String = source
    }
    object ETagCalculatorForPair extends ETagCalculator[Pair[String,String]] {
      def calc(source:Pair[String,String]):String = {
        source._1 + source._2
      }
    }
    
    it("ETagCalculatorForStringを使って String からETagを計算する") {
      case class ETagForString[String](val source:String) extends ETag[String]
      ETagForString("string").calculate(ETagCalculatorForString) should equal("string")
    }
    it("ETagCalculatorForPairを使って Pair[String,String] からETagを計算する") {
      case class ETagForPair(val source:Pair[String,String]) extends ETag[Pair[String,String]]
      ETagForPair(Pair("string1","string2")).calculate(ETagCalculatorForPair) should equal("string1string2")
    }
  }
}

