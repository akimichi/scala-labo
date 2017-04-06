import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


class MonadicTransformerSpec extends FunSpec with ShouldMatchers  {
  import org.json4s._
  import org.json4s.native.JsonMethods._

  object namespace {
    type Name = String
    type Answer = Term
    /**
     * term
     */ 
    sealed trait Term
    case class Var(x: Name) extends Term
    case class Con(n: BigInt) extends Term
    case class Add(l: Term, r: Term) extends Term
    case class Lam(x: Name, body: Term) extends Term
    case class App(fun: Term, arg: Term) extends Term
    case object Wrong extends Term {
      override def toString() = "<wrong>"
    }
    
    def transform(t: JValue): M[Term] = t match {
      case JObject(obj) => {
        val fields:List[JField] = obj
        println(fields.head)
        fields.head match {
          case ("lambda",jobj:JObject) => {
            jobj match {
              case JObject(obj) => {
                val fields:List[JField] = obj
                var name:Name = ""
                var body:M[Term] = unitM(new Term{})
                fields foreach { field =>
                  field match {
                    case ("arg", the_arg:JString) => {
                      name = the_arg.s
                    }
                    case ("body", the_body) => {
                      body = transform(the_body)
                    }
                  }
                }
                unitM(Lam(name, body.answer))
              }
              case _ => unitM(Wrong)
            }
          }
          case _ => unitM(Wrong)
        }
      }
      case JInt(num) => unitM(Con(num))
      case JString(s) => unitM(Var(s))
    }
    
    def id[A] = (x: A) => x
    def showM(m: M[Term]): String = id(m.answer).toString()
    def test(t: JValue): String = showM(transform(t))
    /** 
     * monad. 
     */
    case class M[A](answer:A) {
      def bindM[B](k: A => M[B]):M[B]     = M[B](k(answer).answer)
      def map[B](f: A => B): M[B]        = bindM(x => unitM(f(x)))
      def flatMap[B](f: A => M[B]): M[B] = bindM(f)
    }
    def unitM[A](a: A):M[A] = M[A](a)
  }
  
  describe("transformerをテストする") {
    import namespace._
    
    transform(JObject(List(("numbers",JArray(List(JInt(1), JInt(2), JInt(3), JInt(4))))))) should equal(M(Wrong))
    transform(JObject(List(("lambda",
                            JObject(List(("arg",JString("x")),
                                         ("body",JInt(1)))))))) should equal(M(Lam("x",Con(1))))
  }
  
}

