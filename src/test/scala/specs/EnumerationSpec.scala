import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * Enumeration の基本的な用法を示す
 */

class EnumerationSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
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
  describe("Enumerationを合成で使う"){
    object test {
      object Gender extends Enumeration {
        type Gender = Value
        val Male,Female = Value
      }
      import Gender._
      class Human(val gender:Gender)
    }
    it("定義された列挙型 Gender をHumanで使う"){
      import test._
      import Gender._
      val man = new Human(Male)
      man.gender should equal(Male)
    }
  }
  /*
  describe("Enumerationを合成と継承で使う"){
    object test {
      object Gender extends Enumeration {
        type Gender = Value
        val Male,Female = Value
      }
      import Gender._
      object NewGender extends test.Gender {
        type Gender = Value
        val Unknown = Value
      }
      
      class Human(val gender:Gender)
      import NewGender._
      class NewHuman(val gender:NewGender) extends Human(gender)
    }
    it("定義された列挙型 Gender を NewHumanで使う"){
      import test._
      import Gender._
      import NewGender._
      val man = new NewHuman(Male)
      man.gender should equal(Male)
    }
  }
  */
}

