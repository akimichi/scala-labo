import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Typeclass の基本的な用法を示す
 */
class TypeclassSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Effective Scala による複数の型変数を用いた Typeclass は、") {
    /*
     * 型パラメータ T で型付けされた typeclass 
     */ 
    trait Encodable[T] {
      def encode(t:T):String
      def decode(buf:String): T
    }
    /*
     * companion object において typeclass の暗黙変換を定義するのが定石である。
     * 
     */
    object Encodable {
      info("context bound")
      def encode[T: Encodable](t:T) = implicitly[Encodable[T]].encode(t)
      /* Int型の型インスタンス */
      implicit object IntEncodable extends Encodable[Int] {
        def encode(t:Int):String = t.toString
        def decode(buf:String): Int = buf.toInt
      }
      /* String型の型インスタンス */      
      implicit object StringEncodable extends Encodable[String] {
        def encode(t:String):String = t
        def decode(buf:String): String = buf
      }
      /** Tuple型の型インスタンス
       *  implicit 引数のところは、 implicity でも可能である
       */
      implicit def tupleEncodable[A,B] (implicit ea:Encodable[A], eb:Encodable[B]) : Encodable[(A,B)] = {
        new Encodable[(A,B)] {
          def encode(t:(A,B)):String = "(%s,%s)".format(t._1,t._2)
          def decode(buf:String): (A,B) = {
            val pattern = """\((.*),(.*)\)""".r
            val pattern(first,second) = buf
            (first.asInstanceOf[A],second.asInstanceOf[B])
          }
        }
      }
    }
    it("再帰的に暗黙引数を解決して、入れ子になったPairをencode可能である") {
      import Encodable._
      Encodable.encode((1,2)) should equal("(1,2)")
      Encodable.encode(("a",2)) should equal("(a,2)")
      Encodable.encode(("a",(1,"b"))) should equal("(a,(1,b))")
    }
    it("ユーザが定義したクラスについても、 Encodable で encode できる") {    
      class UserCode(val name:String, val age:Int)
      object UserCode {
        implicit object encoder extends Encodable[UserCode] {
          def encode(t:UserCode):String = "UserCode(%s,%d)".format(t.name,t.age)
          def decode(buf:String): UserCode = {
            val pattern = """UserCode\((.+),(\d+)\)""".r
            val pattern(name,age) = buf
            new UserCode(name,age.toInt)
          }
        }
      }
      import UserCode._
      val user = new UserCode("someone", 25)
      Encodable.encode(user) should equal("UserCode(someone,25)")
    }
  }
  describe("Typeclassを用いて、Dan Rosen のビデオの例をテストする") {
    trait Expression
    case class Number(value:Int) extends Expression
    case class Plus(lhs:Expression, rhs:Expression) extends Expression
    case class Minus(lhs:Expression, rhs:Expression) extends Expression

    object ExpressionEvaluator {
      def eval(expr:Expression):Int = expr match {
        case Number(value) => value
        case Plus(lhs,rhs) => eval(lhs) + eval(rhs)
        case Minus(lhs,rhs) => eval(lhs) - eval(rhs)
      }
    }

    sealed trait JsonValue
    case class JsonObject(entries:Map[String,JsonValue]) extends JsonValue
    case class JsonArray(entries:Seq[JsonValue]) extends JsonValue
    case class JsonString(value:String) extends JsonValue
    case class JsonNumber(value:BigDecimal) extends JsonValue
    case class JsonBoolean(value:Boolean) extends JsonValue
    case object JsonNull extends JsonValue

    object JsonWriter {
      def write(value:JsonValue): String = {
        value match {
          case JsonObject(entries) => {
            val serializedEntries = for((key,value) <- entries) yield {
              key + ":" + write(value)
            }
            "( " + (serializedEntries mkString ", ") + " )"
          }
          case JsonArray(entries) => {
            val serializedEntries = entries map write
            "[ " + (serializedEntries mkString ", ") + " ]"
          }
          case JsonString(value) => "\"" + value + "\""
          case JsonNumber(value) => value.toString
          case JsonBoolean(value) => if(value) "true" else "false"
          case JsonNull => "null"
        }
      }
      def write[A: JsonConverter](value:A):String = {
        write(implicitly[JsonConverter[A]].asJson(value))
      }
    }
    trait JsonConverter[-A] {
      def asJson(value:A): JsonValue
    }
    object JsonConverter {

      implicit object jsonConverter extends JsonConverter[JsonValue] {
        def asJson(json:JsonValue):JsonValue = json
       }
       implicit object expresionConverter extends JsonConverter[Expression] {
        def asJson(expr:Expression):JsonValue = {
          expr match {
            case Number(value) => JsonNumber(value)
            case Plus(lhs,rhs) => JsonObject(
              Map("op" -> JsonString("+"), "lhs" -> asJson(lhs),"rhs" -> asJson(rhs)))
            case Minus(lhs,rhs) => JsonObject(
              Map("op" -> JsonString("-"), "lhs" -> asJson(lhs),"rhs" -> asJson(rhs)))
          }
        }
      }
    }
    it("ExpressionをJsonValueに変換する") {
      import JsonConverter._
      val expression = Plus(Number(1), Minus(Number(3),Number(2)))
      JsonWriter.write(expression) should equal("""( op:"+", lhs:1, rhs:( op:"-", lhs:3, rhs:2 ) )""")
    }
  }
  describe("Typeclassを用いて、Dan Rosen のビデオの例をテストする(その2)") {
    trait Expression[A] {
      def value(expr:A):Int
    }
    object Expression {
      implicit val intExpression = new Expression[Int] {
        def value(n: Int):Int = n
      }
      implicit def pairExpression[T1 : Expression, T2 : Expression] = new Expression[(T1,T2)] {
        def value(pair:(T1,T2)):Int = {
          implicitly[Expression[T1]].value(pair._1) + implicitly[Expression[T2]].value(pair._2)
        }
      }
    }

    object ExpressionEvaluator {
      def eval[A : Expression](expr: A):Int = implicitly[Expression[A]].value(expr)
    }

    sealed trait JsonValue
    case class JsonObject(entries:Map[String,JsonValue]) extends JsonValue
    case class JsonArray(entries:Seq[JsonValue]) extends JsonValue
    case class JsonString(value:String) extends JsonValue
    case class JsonNumber(value:BigDecimal) extends JsonValue
    case class JsonBoolean(value:Boolean) extends JsonValue
    case object JsonNull extends JsonValue

    object JsonWriter {
      def write(value:JsonValue): String = {
        value match {
          case JsonObject(entries) => {
            val serializedEntries = for((key,value) <- entries) yield {
              key + ":" + write(value)
            }
            "( " + (serializedEntries mkString ", ") + " )"
          }
          case JsonArray(entries) => {
            val serializedEntries = entries map write
            "[ " + (serializedEntries mkString ", ") + " ]"
          }
          case JsonString(value) => "\"" + value + "\""
          case JsonNumber(value) => value.toString
          case JsonBoolean(value) => if(value) "true" else "false"
          case JsonNull => "null"
        }
      }
      def write[A: JsonConverter](value:A):String = {
        write(implicitly[JsonConverter[A]].asJson(value))
      }
    }
    trait JsonConverter[-A] {
      def asJson(value:A): JsonValue
    }
    object JsonConverter {
      implicit def intConverter = new JsonConverter[Int] { // implicit object intConverter extends JsonConverter[Int] {
        def asJson(n:Int):JsonValue = JsonNumber(n)
      }
      implicit def boolConverter = new JsonConverter[Boolean] { // implicit object boolConverter extends JsonConverter[Boolean] {
        def asJson(bool:Boolean):JsonValue = JsonBoolean(bool)
      }
      implicit def stringConverter = new JsonConverter[String] { // implicit object stringConverter extends JsonConverter[String] {
        def asJson(value:String):JsonValue = JsonString(value)
      }
      implicit def jsonConverter = new JsonConverter[JsonValue] { // implicit object jsonConverter extends JsonConverter[JsonValue] {
        def asJson(json:JsonValue):JsonValue = json
      }
      implicit def pairConverter[T1 : JsonConverter, T2 : JsonConverter] = new JsonConverter[(T1,T2)] {
        def asJson(pair:(T1,T2)):JsonValue = {
          JsonObject(Map("first" -> implicitly[JsonConverter[T1]].asJson(pair._1),
                         "second" -> implicitly[JsonConverter[T2]].asJson(pair._2)))
        }
      }
    }
    it("PairをJsonValueに変換する") {
      import JsonConverter._
      val expression = (1, ("isRoot",true))
      JsonWriter.write(expression) should equal("""( first:1, second:( first:"isRoot", second:true ) )""")
    }
  }
  /*
  describe("Typeclassを用いて、Dan Rosen のビデオの例をテストする(その3)") {
    trait Expression[A] {
      def value(expr:A):Int
      def calc(expr:A)(func:A => Int):Int
    }
    object Expression {
      implicit def intExpression = new Expression[Int] {
        def value(n:Int):Int = n
        def calc(n:Int)(func: Int => Int):Int = func(n)
      }
      implicit def pairExpression[T1 : Expression, T2 : Expression] = new Expression[(T1,T2)] {
        def value(pair:(T1,T2)):Int = {
          implicitly[Expression[T1]].value(pair._1) + implicitly[Expression[T2]].value(pair._2)
        }
        def calc(pair:(T1,T2))(func: ((T1,T2)) => Int):Int = func(pair)
      }
      implicit def listExpression[T : Expression] = new Expression[List[T]] {
        def value(list:List[T]):Int = {
          implicitly[Expression[T]].value(list.head) + value(list.tail)
        }
        def calc(list:List[T])(func: List[T] => Int):Int = func(list)
        def each(list:List[T])(op: T => Int):Int = {
          implicitly[Expression[T]].value(list.head) + value(list.tail)
        }
      }
    }

    object ExpressionEvaluator {
      def eval[A : Expression](expr: A):Int = implicitly[Expression[A]].value(expr)
      def calc[A : Expression](expr: A)(func:A => Int):Int = {
        val value:Expression[A] = implicitly[Expression[A]]
        value.calc(expr)(func)
      }
    }
    it("PairをJsonValueに変換する") {
      import Expression._
      ExpressionEvaluator.calc((1,2)){pair => pair._1 * pair._2}  should equal(2)
      ExpressionEvaluator.calc(1){one => one + 2}  should equal(3)        
      ExpressionEvaluator.calc(List(1,2,3)){list => list.head}  should equal(1)
    }
  }
  */
  describe("Typeclassとコンパニオンオブジェクトを用いて、型変数に応じて種々の実装を生成できる") {
    
    trait LongFormatter[TSource] {
      def asLong(source:TSource):Long
    }
    object LongFormatter {
      implicit lazy val stringToLong = new LongFormatter[String] {
        def asLong(source:String):Long = source.toLong
      }
      implicit lazy val intToLong = new LongFormatter[Int] {
        def asLong(source:Int):Long = source.toLong
      }
    }
    
    it("コンパニオンオブジェクトだけ import しておけば、引数の型に応じて暗黙変換が起動される") {
      import LongFormatter._ 
      def toLong[T](source:T)(implicit formatter:LongFormatter[T]):Long = formatter.asLong(source)
      
      toLong("100") should equal(100)
      toLong(100) should equal(100)
    }
    it("Context Bound と implicitlyを用いた場合でも、引数の型に応じて暗黙変換が起動される") {
      import LongFormatter._ 
      def toLong[T : LongFormatter](source:T):Long = {
        implicitly[LongFormatter[T]].asLong(source)
      }
      toLong("100") should equal(100)
      toLong(100) should equal(100)
    }
  }

  describe("複数の型変数を用いた Typeclass は、") {
    trait Transformer[TSource,TTarget] {
      def transform(source:TSource):TTarget
    }
    object Transformer {
      def transform[TSource,TTarget](source:TSource)(implicit transformer:Transformer[TSource,TTarget]):TTarget = {
        transformer.transform(source)
      }
      implicit lazy val stringToLong = new Transformer[String,Long] {
        def transform(source:String):Long = source.toLong
      }
      implicit lazy val intToString = new Transformer[Int,String] {
        def transform(source:Int):String = source.toString
      }
    }
    it("コンパニオンオブジェクトだけ import しておけば、引数の型に応じて暗黙変換が起動される") {
      import Transformer._ 
      // def transform[TSource,TTarget](source:TSource)(implicit transformer:Transformer[TSource,TTarget]):TTarget = {
      //   transformer.transform(source)
      // }
      transform("100") should equal(100)
      transform(100) should equal("100")
    }
    it("implicitlyの場合は、 Context Bound ではなく、 implicit parameter を用いなければならない") {
      import Transformer._ 
      def transform[TSource,TTarget](source:TSource)(implicit ev: Transformer[TSource,TTarget]):TTarget = {
        implicitly[Transformer[TSource,TTarget]].transform(source)
      }
      transform("100") should equal(100)
      transform(100) should equal("100")
    }
  }
}
