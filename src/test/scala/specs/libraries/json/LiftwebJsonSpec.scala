import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * JSON処理の基本的な用法を示す
 */
// class LiftwebJsonSpec extends FunSpec with ShouldMatchers with scala_labo.helpers {
//   describe("liftweb-jsonを用いて") {
//     import net.liftweb.json._ 
//     import net.liftweb.json.Extraction._
//     import net.liftweb.json.Serialization.{read, write}
//     import net.liftweb.json.JsonAST.{JNothing}
    
//     describe("JSONをパースする") {
//       val ast = parse(""" { "numbers" : [1, 2, 3, 4] } """)
//       it("parseして JObject に変換する"){
//         ast.isInstanceOf[JObject] should equal(true)
//       }
//       it("valuesメソッドで JObject を Map に変換する"){
//         ast.values should equal(Map("numbers" -> List(1,2,3,4)))
//       }
//     }
//     describe("パースした結果からパス記法を用いて値をとりだす") {
//       it("単純な構造の例"){
//         val ast = parse(""" { "numbers" : [1, 2, 3, 4] } """)
//         JArray(List(JInt(1), JInt(2), JInt(3), JInt(4)))
//         compact(render(ast \ "numbers")) should equal ("[1,2,3,4]")
//       }
//       it("Config情報の例"){
//         val config = """{"services": {        
//           "terminology@ec2": {
//             "configs": {
//               "mongodb": {
//                 "server_host": "mongodb.dyndns.org",
//         "server_port": 27017,
//         "dbname": "terminology_service"}}
//           },
//         "cloudehr": {
//           "configs": {
//             "contoller": {
//               "server_host": "openehr.dyndns.org",
//         "server_port": 9000},
//         "redis": {
//           "server_host": "ebisu",
//         "server_port": 6379,
//         "timeout": 2000}}}}}"""
//         val config_ast = parse(config)
//         compact(render(config_ast \ "services" \ "cloudehr" \ "configs" \ "contoller")) should equal ("""{"server_host":"openehr.dyndns.org","server_port":9000}""")
//         val field_ast = parse(compact(render((config_ast \ "services" \ "cloudehr" \ "configs" \ "contoller"))))
//         compact(render(field_ast \ "server_host")) should equal("\"openehr.dyndns.org\"")
//       }
//       it("organisation.jsonを処理する"){
//         val organisation_ast = parse(fixture.jsons.organisation)
//         compact(render(organisation_ast \ "details" \ "items" \ "at0007" \ "name")) should equal ("\"Organisation detail name\"")
//       }
//     }
//     describe("src/test/resouces/zips.json のファイルを用いて"){
      
//       it("JSON形式のファイルをparseでList[Map[String,Any]]に変換する"){
//         /**
//          * {"city": "ACMAR", "loc": [-86.51557, 33.584132], "pop": 6055, "state": "AL", "_id": "35004"}
//          * {"city": "ADAMSVILLE", "loc": [-86.959727, 33.588437], "pop": 10616, "state": "AL", "_id": "35005"}
//          * ...
//          */ 
//         import scala.io._
        
//         val source = try {
//           Source.fromFile("src/test/resources/zips.json","UTF-8")
//         } catch {
//           case ex => throw ex
//         }
//         var records:List[Map[String, Any]] = List.empty
//         source.getLines.foreach {line:String => {
//           val record = parse(line).values.asInstanceOf[Map[String, Any]]
//           records ::= record
//         }}

//         source.close()
//         records(0).get("city") should equal(Some("THAYNE"))          
//       }
//     }
//   }
// }

