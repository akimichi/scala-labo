import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * オブジェクト指向の基本的な用法を示す
 */


class OOPSpec extends FunSpec with ShouldMatchers with scala_labo.helpers {
  describe("無名クラス anonymous class について"){
    val anonymous = new {
      val data = 1
    }
    anonymous should be (anInstanceOf[Any])
    anonymous should be (anInstanceOf[AnyRef])
    anonymous should be (anInstanceOf[Object])
  }
  describe("traitの初期化について"){
    trait Propery {
      val name:String
    }
    it("基底traitの初期化は scala 2.9では問題ない"){
      val x = new Propery { override val name = "HI" }
      x.name should equal("HI")
    }
    it("基底traitの抽象メンバを case class で具体化する"){
      case class ConcreteProperty(name:String) extends Propery
      val property = ConcreteProperty("name")
      property.name should equal("name")
    }
    it("基底traitの抽象メンバを class で具体化する"){
      class ConcreteProperty(_name:String) extends Propery { val name = _name}
      val property = new ConcreteProperty("name")
      property.name should equal("name")
    }
  }
  describe("classのgetter,setterについて"){
    class Test(val immutable:String, var mutable:String)
    it("classにおいて、immutableなメンバにも mutable なメンバにもデフォルトでgetter,setterが付与される"){
      val test = new Test("immutable","mutable")
      test.immutable should equal("immutable")
      test.mutable should equal("mutable")
    }
    case class CaseClass(val immutable:String, var mutable:Option[String] = None)
    class SubClass(override val immutable:String) extends CaseClass(immutable)
    it("case classから継承されたclassについても、immutableなメンバにも mutable なメンバにもデフォルトでgetter,setterが付与される"){
      val subclass = new SubClass("immutable")
      subclass.mutable = Some("mutable")
      subclass.mutable should equal(Some("mutable"))
    }
  }
  describe("equalsの再定義"){
    it("equalsを再定義する"){
        class Item(val name: String, val price: Double, val color:String){
          final override def equals(other: Any) = {
            val that = other.asInstanceOf[Item]
            if (that == null) false
            else name == that.name && price == that.price
          }
        }
      val an_item = new Item("an item",10.0, "Red")
      val another_item = new Item("an item",10.0, "Black")
      an_item.equals(another_item) should equal(true)
      
      val item01 = new Item("an item",10.0, "Red")
      val item02 = new Item("an item",10.0, "Red")
      import scala.collection.mutable.HashSet
      HashSet(item01).contains(item02) should equal(false)
    }
    it("equalsを再定義した場合は、hashCodeも再定義する必要がある"){
      class Item(val name: String, val price: Double, val color:String){
        final override def equals(other: Any) = {
          val that = other.asInstanceOf[Item]
          if (that == null) false
          else name == that.name && price == that.price
        }
        final override def hashCode = 13 * name.hashCode + 17 * price.hashCode
      }
      val item01 = new Item("an item",10.0, "Red")
      val item02 = new Item("an item",10.0, "Red")
      import scala.collection.mutable.HashSet
      HashSet(item01).contains(item02) should equal(true)
    }
  }
}

