import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * その他のScalaの基本的な用法を示す
 */

class MiscSpec extends FunSpec with ShouldMatchers {
  describe("Nothing"){
    class SubNothing[Nothing]
  }
  // describe("package object を操る"){
  //   it("内部で定義された変数を変更する"){
  //     import scala_labo.package_object._
  //     get_mutable should equal("mutable")      
  //     scala_labo.package_object.mutable = "changed"
  //     scala_labo.package_object.mutable should equal("changed")
  //     get_mutable should equal("changed")
  //   }
  // }
  describe("Enumerationを使う"){
    object Gender extends Enumeration {
      type Gender = Value
      val Male,Female = Value
    }

    it("定義された列挙型 Gender を使う"){
      import Gender._
      Gender.Male.toString should equal("Male")
      Gender.withName("Female") should equal(Female)
    }
  }
  describe("breakableでloopを抜ける"){
    import scala.util.control.Breaks

    val breaks = new Breaks()
    import breaks.{break,breakable}
    breakable {
      for(i <- 1 to 100) {
        if(i > 5)
          break
      }
    }
  }
}

