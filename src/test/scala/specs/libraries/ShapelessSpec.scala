import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Shapelessライブラリの基本的な用法を示す
 * c.f. 
 */
// class ShapelessSpec extends FunSpec with ShouldMatchers {
//   import shapeless._
//   import Lens._
//   import Nat._

//   def typed[T](t : => T) {}

//   // A pair of ordinary case classes ...
//   case class Address(street : String, city : String, postcode : String)
//   case class Person(name : String, age : Int, address : Address)
  
//   // One line of boilerplate per case class ...
//   implicit val addressIso = Iso.hlist(Address.apply _, Address.unapply _)
//   implicit val personIso = Iso.hlist(Person.apply _, Person.unapply _)
  
//   // Some lenses over Person/Address ...
//   val nameLens = Lens[Person] >> _0
//   val ageLens = Lens[Person] >> _1
//   val addressLens = Lens[Person] >> _2
//   val streetLens = Lens[Person] >> _2 >> _0
//   val cityLens = Lens[Person] >> _2 >> _1
//   val postcodeLens = Lens[Person] >> _2 >> _2

//   // Starting value
//   val person = Person("Joe Grey", 37, Address("Southover Street", "Brighton", "BN2 9UA"))
//   // Update a field
//   val person2 = ageLens.set(person)(38)
//   person2.age should equal(38)
  
//   // Transform a field
//   val person3 = ageLens.modify(person2)(_ + 1)
//   person3.age should equal(39)
// }

