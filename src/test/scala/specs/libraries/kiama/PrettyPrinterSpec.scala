import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


import org.kiama


/**
 * Kiamaライブラリ の PrettyPrinter の 基本的な用法を示す
 */
class PrettyPrinterSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("PrettyPrinter により") {
    describe("PrettyPrinterでADTをシリアライズ"){
      object ast {
        trait Expr
        case class Num(x: Int) extends Expr
        case class Add(e1: Expr, e2: Expr) extends Expr
      }
        
      object PrettyPrinter extends org.kiama.output.PrettyPrinter {
        import ast._
        def pretty (t :Expr) : String = super.pretty (show (t))
        def show (t :Expr) : Doc = t match {
          case Num (d)       => value (d)
          case Add(e1,e2)    => parens (show (e1) <+> "+" <+> show (e2))
          }
      }
      it("PrettyPrinter.prettyメソッドを使う"){
        import ast._
        PrettyPrinter.pretty(Add(Num(3), Num(2))) should equal("(3 + 2)")
        }
      it("PrettyPrinter.productメソッドを使う"){
        import ast._
        PrettyPrinter.pretty(PrettyPrinter.product(Add(Num(3), Num(2)))) should equal("Add (Num (3), Num (2))")
      }
    }
  }
}  
