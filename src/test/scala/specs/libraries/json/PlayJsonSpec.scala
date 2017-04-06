import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * play json をもちいたJSON処理の基本的な用法を示す
 */
// class PlayJsonSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
//   import play.api.libs.json._

//   describe("JSONをパースする"){
//     val jsonString = """{"user" : {"name" : "bob"}}"""
//     val json = Json.parse(jsonString)
    
//     val maybeName = (json \ "user" \ "name").asOpt[String]
//     maybeName.get should equal("bob")
//     json.asInstanceOf[JsObject].fields.find { pair =>
//       pair match {
//         case ("user",value) => true
//       }
//     } should equal(Some(("user",JsObject(Seq(("name",JsString("bob")))))))
//     // val emails = (json \ "user" \\ "emails").map(_.as[String])
//   }
// }
