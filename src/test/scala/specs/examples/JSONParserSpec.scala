import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * JSON処理の基本的な用法を示す
 */
class JSONParserSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("parser combinator を用いて") {

    /*
     JSON ::= object EOF
     object ::= "{" members? "}"
     members ::= pair ("," pair)*
     pair ::= string ":" value 
     value ::= string | number | object
     string ::= "\"" char* "\"";
     char ::= [^"\\] | "\\" ["\\];
     number ::= ("-")? ([1-9] [0-9]* | "0")
     
     */
    object test {
      import scala.util.parsing.combinator._
      import scala.util.parsing.input._
      object JSONParser extends Parsers with RegexParsers{
        abstract sealed class JSONValue
        case class JSONObject(members: List[(String, JSONValue)]) extends JSONValue
        case class JSONNumber(value: Int) extends JSONValue
        case class JSONString(value: String) extends JSONValue
        
        override type Elem = Char

        lazy val number = "[0-9]+".r
        /*
        val EOF = elem("EOF", _ == CharSequenceReader.EofCh)
        def cset(cs: Char*): Parser[Char] = elem("", cs.contains(_))
        def crange(from: Char, to: Char): Parser[Char] = elem("", e => from <= e && e <= to)
        lazy val JSON: Parser[JSONObject] = objecz <~ EOF
        lazy val objecz : Parser[JSONObject] = (elem('{') ~> opt(members) <~ elem('}')) ^^ { case m => JSONObject(m.getOrElse(Nil)) }
        lazy val members: Parser[List[(String, JSONValue)]] = pair ~ rep(elem(',') ~> pair) ^^ { case a ~ b => a :: b }
        lazy val pair: Parser[(String, JSONValue)] = (string <~ elem(':')) ~ value ^^ { case a ~ b => (a, b) }
        lazy val value: Parser[JSONValue] = string ^^ { case x => JSONString(x) } | number | objecz
        lazy val string: Parser[String] = elem('"') ~> rep(char) <~ elem('"') ^^ (_.mkString(""))
        lazy val any = elem("ANY", _ != CharSequenceReader.EofCh)
        lazy val char: Parser[Char] = (not(cset('"','\\')) ~> any) | elem('\\') ~> cset('"', '\\')
        lazy val number: Parser[JSONNumber] = 
          opt(elem('-')) ~ ((crange('1','9') ~ rep(crange('0','9'))) ^^ { 
            case a ~ b => (a + b.mkString("")) 
          }
                            | 
                            elem('0') ^^ (_ + "")) ^^ { 
                              case Some(_) ~ v => JSONNumber(("-" + v).toInt)
                              case _ ~ v => JSONNumber(v.toInt)
                            }
        */
      }
    }
    it("JSONをパースする"){
      import test._
      val parser = JSONParser
      parser.parseAll(parser.number,"12").successful should equal(true)
      // parser.parseAll(parser.number,"12") match {
      //   case Success(v, _) => println(v)
      //   case e => println(e)
      // }
    }
  }
}
