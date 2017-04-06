import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * メソッドに関するテスト
 */

class MethodSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("可変長引数"){
    object test {
      def accepts_strings(strs:String*):String = strs.foldLeft(""){(a:String,b:String) => a + b}
    }
    it("可変長引数を呼びだす"){
      import test._
      accepts_strings("ab","cde","efg") should equal("abcdeefg")
      
    }
    it("Listを渡す"){
      import test._
      val arg = List("ab","cde","efg")
      accepts_strings(arg: _*) should equal("abcdeefg")
    }
  }
}
