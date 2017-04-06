import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


import org.kiama


/**
 * Kiamaライブラリ の Rewriter の基本的な用法を示す
 */
// class RewriterSpec extends FunSpec with ShouldMatchers {
//   import org.scalatest.matchers.ShouldMatchers.{equal => equalTo}
  
//   object json {
//     import org.kiama.attribution.Attributable
//     // import org.kiama.rewriting.Rewriter._
    
//     sealed trait JsonValue extends Attributable
//     case class JsonObject(entries:Map[String,JsonValue]) extends JsonValue
//     case class JsonArray(entries:Seq[JsonValue]) extends JsonValue
//     case class JsonString(value:String) extends JsonValue
//     case class JsonNumber(value:BigDecimal) extends JsonValue
//     case class JsonBoolean(value:Boolean) extends JsonValue
//     case object JsonNull extends JsonValue
//   }
  
//   describe("Rewriter により") {
//     import org.kiama.rewriting.Rewriter._
//     import org.kiama.attribution.Attributable
//     import scala.util.parsing.input.Positional
    
//     object ast {
//       trait Expr extends Attributable with Positional
//       case class Constant(x: Int) extends Expr
//       case class Add(e1: Expr, e2: Expr) extends Expr
//     }
    
//     it("rule でASTを変換できる"){
//       import ast._
//       val root = Add(Constant(3), Constant(2))
//       object rewriter {
//         val rewritePlus5 = everywhere (rule {
//           case Constant(x) => Constant(x+5)
//         })
//         def plus5(expr:Expr):Expr = rewrite (rewritePlus5) (expr)
//         val incall = alltd (rule { case i : Int => i + 1 })
//         val incfirst = oncetd (rule { case i : Int => i + 1 })
//         val incodd = sometd (rule { case i : Int if i % 2 == 1 => i + 1 })
//       }
//       import rewriter._
//       plus5(Constant(5)) should equalTo(Constant(10))
//       (incall)(List(1,2,3)) should equalTo(Some(List(2,3,4)))
//     }
//   }
//   describe("JSONのASTを") {
//     describe("attrメソッドを用いて MongoDBObject に変換する") {
//       import json._
//       import org.kiama
//       import org.kiama.attribution.Attribution._
//       import org.kiama.attribution.Attribution.attr
//       import com.mongodb.casbah.Imports._

//       object transformer {
//         lazy val term: JsonValue => Any = {
//           attr {
//             case JsonString(string) => string
//             case JsonNumber(value) => value
//           }
//         }
//         lazy val transform: JsonValue => Map[String,Any] = {
//           attr {
//             case JsonObject(entries) => {
//               for {
//                 (key, value)  <- entries
//               } yield {
//                 (key, value match {
//                   case json_object @ JsonObject(entries) => transform(json_object)
//                   case otherwise => term(otherwise)
//                 })
//               }
//             }
//           }
//         }
//       }
//       it("transformを試す"){
//         import transformer._
        
//         val jstring = JsonString("string")
//         val jtrue = JsonBoolean(true)
//         val jnull = JsonNull
//         val jobject = JsonObject(Map("name" ->  JsonString("foo"),
//                                      "age" -> JsonNumber(11.0)))
        
//         term(jstring) should equalTo("string")
//         transform(jobject) should equalTo(Map("name" ->  "foo",
//                                               "age" -> 11.0))
//       }
//     }
//     it("attrメソッドを用いて文字列に変換する") {
//       import org.kiama
//       import org.kiama.attribution.Attribution._
//       import org.kiama.attribution.Attribution.attr
      
//       import json._
      
//       lazy val transform:JsonValue => String = {
//         attr {
//           case json_null @ JsonNull => "null"
//           case json_boolean @ JsonBoolean(value) =>
//             value match {
//               case true => "true"
//               case false => "false"
//             }
//           case json_number @ JsonNumber(value) => value.toString
//           case json_string @ JsonString(value) => value
//           case json_array @ JsonArray(entries) => {
//             val serializedEntries = entries map{(entry) => transform(entry)}
//             "[ " + (serializedEntries mkString ", ") + " ]"
//           }
//           case json_object @ JsonObject(entries) => {
//             val serializedEntries = for((key,value) <- entries) yield {
//               key + ":" + transform(value)
//             }
//             "{ " + (serializedEntries mkString ", ") + " }"
//           }
//         }
//       }
      
//       val jstring = JsonString("string")
//       val jtrue = JsonBoolean(true)
//       val jnull = JsonNull
//       transform(jstring) should equal("string")
//       transform(jtrue) should equal("true")
//       transform(jnull) should equal("null")
//       val jarray = JsonArray(List(jstring,jtrue,jnull))
//       transform(jarray) should equal("[ string, true, null ]")
//       val jobject = JsonObject(Map("key" -> jarray))
//       transform(jobject) should equal("{ key:[ string, true, null ] }")
//     }
//   }
// }
