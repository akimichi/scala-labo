import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }



/**
 * Parserライブラリ の基本的な用法を示す
 */
class ParserSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Combinator Parserを用いて") {
    describe("PackratParsersをもとに openSMILESパーサを作成する") {
      import scala.util.parsing.combinator.PackratParsers
      import scala.util.parsing.combinator.RegexParsers
      import scala.util.parsing.input.CharSequenceReader
      /*
       * ATOMS:
       * atom ::= bracket_atom | aliphatic_organic | aromatic_organic | '*'
       * 
       * ORGANIC SUBSET ATOMS:
       * aliphatic_organic ::= 'B' | 'C' | 'N' | 'O' | 'S' | 'P' | 'F' | 'Cl' | 'Br' | 'I'
       * aromatic_organic ::= 'b' | 'c' | 'n' | 'o' | 's' | 'p'
       * 
       * BRACKET ATOMS:
       * bracket_atom ::= '[' isotope? symbol chiral? hcount? charge? class? ']'
       * symbol ::= element_symbols | aromatic_symbols | '*'
       * isotope ::= NUMBER
       * element_symbols ::= 'H' | 'He' | 'Li' | 'Be' | 'B' | 'C' | 'N' | 'O' | 'F' | 'Ne' | 'Na' | 'Mg' | 'Al' | 'Si' | 'P' | 'S' | 'Cl' | 'Ar' | 'K' | 'Ca' | 'Sc' | 'Ti' | 'V' | 'Cr' | 'Mn' | 'Fe' | 'Co' | 'Ni' | 'Cu' | 'Zn' | 'Ga' | 'Ge' | 'As' | 'Se' | 'Br' | 'Kr' | 'Rb' | 'Sr' | 'Y' | 'Zr' | 'Nb' | 'Mo' | 'Tc' | 'Ru' | 'Rh' | 'Pd' | 'Ag' | 'Cd' | 'In' | 'Sn' | 'Sb' | 'Te' | 'I' | 'Xe' | 'Cs' | 'Ba' | 'Hf' | 'Ta' | 'W' | 'Re' | 'Os' | 'Ir' | 'Pt' | 'Au' | 'Hg' | 'Tl' | 'Pb' | 'Bi' | 'Po' | 'At' | 'Rn' | 'Fr' | 'Ra' | 'Rf' | 'Db' | 'Sg' | 'Bh' | 'Hs' | 'Mt' | 'Ds' | 'Rg' | 'Cn' | 'Fl' | 'Lv' | 'La' | 'Ce' | 'Pr' | 'Nd' | 'Pm' | 'Sm' | 'Eu' | 'Gd' | 'Tb' | 'Dy' | 'Ho' | 'Er' | 'Tm' | 'Yb' | 'Lu' | 'Ac' | 'Th' | 'Pa' | 'U' | 'Np' | 'Pu' | 'Am' | 'Cm' | 'Bk' | 'Cf' | 'Es' | 'Fm' | 'Md' | 'No' | 'Lr'
       * aromatic_symbols ::= 'b' | 'c' | 'n' | 'o' | 'p' | 's' | 'se' | 'as'
       * 
       * CHIRALITY:
       * chiral ::= '@' | '@@' | '@TH1' | '@TH2' | '@AL1' | '@AL2' | '@SP1' | '@SP2' | '@SP3' | '@TB1' | '@TB2' | '@TB3' | … | '@TB20' | '@OH1' | '@OH2' | '@OH3' | … | '@OH30' | '@TB' DIGIT DIGIT | '@OH' DIGIT DIGIT
       * HYDROGENS:
       * hcount ::= 'H' | 'H' DIGIT
       * 
       * CHARGES:
       * charge ::= '-' | '-' DIGIT? DIGIT | '+' | '+' DIGIT? DIGIT | '--' deprecated | '++' deprecated
       * 
       * ATOM CLASS:
       * class ::= ':' NUMBER
       * 
       * BONDS AND CHAINS:
       * bond ::= '-' | '=' | '#' | '$' | ':' | '/' | '\'
       * ringbond ::= bond? DIGIT | bond? '%' DIGIT DIGIT
       * branched_atom ::= atom ringbond* branch*
       * branch ::= '(' chain ')' | '(' bond chain ')' | '(' dot chain ')'
       * chain ::= branched_atom | chain branched_atom | chain bond branched_atom | chain dot branched_atom
       * dot ::= '.'
       * 
       * SMILES STRINGS:
       * smiles ::= terminator | chain terminator
       * terminator ::= SPACE | TAB | LINEFEED | CARRIAGE_RETURN | END_OF_STRING
       */
      trait OpenSMILESParser extends RegexParsers with PackratParsers {
        lazy val atom:Parser[Any] = bracket_atom | aliphatic_organic | aromatic_organic | "*"
        lazy val aliphatic_organic:Parser[Any] = "B|C|N|O|S|P|F|Cl|Br|I".r
        lazy val aromatic_organic:Parser[Any] = "b|c|n|o|s|p".r
        lazy val isotope : Parser[Any] = number
        lazy val bracket_atom : Parser[Any] = "["~opt(isotope)~symbol~opt(chiral)~opt(hcount)~opt(charge)~opt(atom_class)~"]"
        lazy val symbol : Parser[Any] = element_symbols | aromatic_symbols | "*".r
        lazy val element_symbols:Parser[Any] = "H|He|Li|Be|B|C|N|O|F|Ne|Na|Mg|Al|Si|P|S|Cl|Ar|K|Ca|Sc|Ti|V|Cr|Mn|Fe|Co|Ni|Cu|Zn|Ga|Ge|As|Se|Br|Kr|Rb|Sr|Y|Zr|Nb|Mo|Tc|Ru|Rh|Pd|Ag|Cd|In|Sn|Sb|Te|I|Xe|Cs|Ba|Hf|Ta|W|Re|Os|Ir|Pt|Au|Hg|Tl|Pb|Bi|Po|At|Rn|Fr|Ra|Rf|Db|Sg|Bh|Hs|Mt|Ds|Rg|Cn|Fl|Lv|La|Ce|Pr|Nd|Pm|Sm|Eu|Gd|Tb|Dy|Ho|Er|Tm|Yb|Lu|Ac|Th|Pa|U|Np|Pu|Am|Cm|Bk|Cf|Es|Fm|Md|No|Lr".r
        lazy val aromatic_symbols:Parser[Any] = "b|c|n|o|p|s|se|as".r
        lazy val chiral:Parser[Any] = "@|@@|@TH1|@TH2|@AL1|@AL2|@SP1|@SP2|@SP3|@TB1|@TB2|@TB3|@TB4|@TB5|@TB6|@TB7|@TB8|@TB9|@TB10|@TB20|@OH1|@OH2|@OH3|@OH30".r | "@TB"~digit~digit | "@OH"~digit~digit
        lazy val hcount:Parser[Any] = "H" | "H"~digit
        lazy val charge:Parser[Any] = "-" | "-"~opt(digit)~digit | "+" | "+"~opt(digit)~digit
        lazy val atom_class:Parser[Any] = ":"~number
        lazy val bond:Parser[Any] = "-" | "=" | "#" | "$" | ":" | "/" | "\\"
        lazy val ringbond:Parser[Any] = opt(bond)~digit | opt(bond)~"%"~digit~digit
        lazy val branched_atom:Parser[Any] = atom~rep(ringbond)~rep(branch)
        lazy val branch:Parser[Any] = "("~chain~")" | "("~bond~chain~")" | "("~dot~chain~")"
        lazy val chain:Parser[Any] = branched_atom | chain~branched_atom | chain~bond~branched_atom | chain~dot~branched_atom
        lazy val dot:Parser[Any] = "."
        lazy val number : Parser[Any] = "[0-9]+".r
        lazy val digit : Parser[Any] = "[0-9]+".r
        
        def parseInput(in: String) = value(new PackratReader(new CharSequenceReader(in)))
      }
      it("OpenSMILESParser"){
        object OpenSMILESParser extends OpenSMILESParser
        OpenSMILESParser.parseAll(OpenSMILESParser.aliphatic_organic, "B") match {
          case OpenSMILESParser.Success(result, remaining) => {
            result should equal("B")
            remaining.atEnd should equal(true)
          }
          case failure : OpenSMILESParser.NoSuccess => fail()
        }
      }
    }
    describe("PackratParsersをもとにASTを構築するJSONパーサを作成する") {
      import scala.util.parsing.combinator.PackratParsers
      import scala.util.parsing.combinator.RegexParsers
      import scala.util.parsing.input.CharSequenceReader

      object test {
        sealed abstract class AstNode
        case class ObjectNode(members: List[MemberNode]) extends AstNode
        case class MemberNode(key: String, value: AstNode) extends AstNode
        case class ArrayNode(elements: List[AstNode]) extends AstNode
        case class StringNode(text: String) extends AstNode
        case class NumberNode(value: Double) extends AstNode
        case object True extends AstNode
        case object False extends AstNode
        case object Null extends AstNode
        
        trait JSONParser extends RegexParsers with PackratParsers {
          lazy val value :PackratParser[AstNode] = obj |
          arr |
          stringLiteral |
          number |
          "null" ^^ (x => Null) |
          "true" ^^ (x => True) |
          "false" ^^ (x => False)
          
          lazy val obj   :PackratParser[ObjectNode] = "{"~>repsep(member, ",")<~"}" ^^ (ObjectNode(_))
          lazy val arr   :PackratParser[ArrayNode] = "["~>repsep(value, ",")<~"]" ^^ (ArrayNode(_)) // { case ~repsep~":"~value => ArrayNode(_)}
          lazy val member:PackratParser[MemberNode] = stringLiteral~":"~value ^^ { case key~":"~value => MemberNode(key.text, value) }
          lazy val number : Parser[NumberNode] = "[0-9]+".r  ^^ { case s => NumberNode(s.toDouble) }
          lazy val stringLiteral : Parser[StringNode] = "\".*\"".r  ^^ { case s => StringNode(s) } 
          def parseInput(in: String) = value(new PackratReader(new CharSequenceReader(in)))
        }
      }
      it("JSONパーサでASTを構築する"){
        import test._
        object JSONParser extends JSONParser
        JSONParser.parseAll(JSONParser.value, "1") match {
          case JSONParser.Success(result, remaining) => {
            result should equal(NumberNode(1.0))
            remaining.atEnd should equal(true)
          }
          case failure : JSONParser.NoSuccess => fail() // scala.sys.error(failure.msg)
        }
        JSONParser.parseAll(JSONParser.value, """"abc"""") match {
          case JSONParser.Success(result, remaining) => {
            result should equal(StringNode(""""abc""""))
            remaining.atEnd should equal(true)
          }
          case failure : JSONParser.NoSuccess => fail() // scala.sys.error(failure.msg)
        }
        JSONParser.parseAll(JSONParser.value, """{"key": true}""") match {
          case JSONParser.Success(result, remaining) => {
            result should equal(ObjectNode(List(MemberNode(""""key"""",True))))
            remaining.atEnd should equal(true)
          }
          case failure : JSONParser.NoSuccess => fail() // scala.sys.error(failure.msg)
        }
      }
    }
    describe("PackratParsersをもとにJSONパーサを構築する") {
      import scala.util.parsing.combinator.PackratParsers
      import scala.util.parsing.combinator.RegexParsers
      import scala.util.parsing.input.CharSequenceReader
      
      trait JSONParser extends RegexParsers with PackratParsers {
        lazy val value :PackratParser[Any] = obj | arr | stringLiteral | number | "null" | "true" | "false"
        lazy val obj   :PackratParser[Any] = "{"~repsep(member, ",")~"}"
          
        lazy val arr   :PackratParser[Any] = "["~repsep(value, ",")~"]"
          
        lazy val member:PackratParser[Any] = stringLiteral~":"~value
        lazy val number : Parser[Any] = "[0-9]+".r
        lazy val stringLiteral : Parser[Any] = "\".*\"".r
        def parseInput(in: String) = value(new PackratReader(new CharSequenceReader(in)))
      }          
      it("JSONパーサを使う"){
        object JSONParser extends JSONParser
        JSONParser.parseAll(JSONParser.value, "1") match {
          case JSONParser.Success(result, remaining) => {
            result should equal("1")
            remaining.atEnd should equal(true)
          }
          case failure : JSONParser.NoSuccess => fail() // scala.sys.error(failure.msg)
        }
        JSONParser.parseAll(JSONParser.value, """{"key":1}""") match {
          case JSONParser.Success(result, remaining) => {
            remaining.atEnd should equal(true)
          }
          case failure : JSONParser.NoSuccess => fail() // scala.sys.error(failure.msg)
        }
      }
    }
    describe("PackratParsersをもとに四則演算パーサを構築する") {
      import scala.util.parsing.combinator.PackratParsers
      import scala.util.parsing.combinator.RegexParsers
      import scala.util.parsing.input.CharSequenceReader
      
      trait ArithParser extends RegexParsers with PackratParsers {
        lazy val term: PackratParser[Any] = (
          term ~ "+" ~ fact |
          term ~ "-" ~ fact |
          fact
        )
        lazy val fact: PackratParser[Any] = (
          fact ~ "*" ~ digit |
          fact ~ "/" ~ digit |
          digit
        )
        lazy val digit : Parser[Any] = "[0-9]+".r
        def parseInput(in: String) = term(new PackratReader(new CharSequenceReader(in)))
      }          
      it("Arithパーサを使う"){
        object ArithParser extends ArithParser
        ArithParser.parseAll(ArithParser.term, "1") match {
          case ArithParser.Success(result, remaining) => {
            result should equal("1")
            remaining.atEnd should equal(true)
          }
          case failure : ArithParser.NoSuccess => fail() // scala.sys.error(failure.msg)
        }
        ArithParser.parseAll(ArithParser.term, "1+3") match {
          case ArithParser.Success(result, remaining) => {
            remaining.atEnd should equal(true)
          }
          case failure : ArithParser.NoSuccess => fail() // scala.sys.error(failure.msg)
        }
        // ArithParser.parseInput("1") match {
        //   case ArithParser.Success(result, _) => {
        //     println(result)
        //     assert(true)
        //   }
        //   case ArithParser.Failure(msg, n) => {
        //     println(msg)
        //     fail(msg)
        //   }
        //   case ArithParser.Error(msg, n) => fail(msg)
        // }
      }
    }

    describe("JavaTokenParsersをもとにJSONパーサを構築する") {
      import scala.util.parsing.combinator.JavaTokenParsers
      import scala.util.parsing.combinator._

      object test {
        object JSONParser extends JavaTokenParsers {
          
          def value : Parser[Any] = obj | arr | stringLiteral | floatingPointNumber | "null" | "true" | "false"
          def obj   : Parser[Any] = "{"~repsep(member, ",")~"}"
          
          def arr   : Parser[Any] = "["~repsep(value, ",")~"]"
          
          def member: Parser[Any] = stringLiteral~":"~value
        }
      }
      
      it("JSONパーサを使う"){
        import test._
        JSONParser.parseAll(JSONParser.value, "}{") match {
          case JSONParser.Success(result, _) => fail()
          case failure : JSONParser.NoSuccess => assert(true) // scala.sys.error(failure.msg)
        }
        JSONParser.parseAll(JSONParser.value, """{"key":1}""") match {
          case JSONParser.Success(result, remaining) => {
            info("CharSequenceReader#atEnd で入力文字列の末尾まで到達したかを知ることができる。")
            remaining.atEnd should equal(true)
          }
          // case failure : JSONParser.NoSuccess => fail()
          case JSONParser.Failure(msg, n) => fail()
          case JSONParser.Error(msg, n) => fail()
        }
      }
    }

  }
}
