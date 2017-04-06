import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Type-Level Programming 基本的な用法を示す
 * 型の基本的な用法を示す
 */
class TypeSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("構造型 structural type") {
    
  }
  describe("条件文を型で実行する(c.f. Scala in Depth,p.168)") {
    sealed trait TBool {
      type If[TrueType <: Up, FalseType <: Up, Up] <: Up
    }
    class TTrue extends TBool {
      type If[TrueType <: Up, FalseType <: Up, Up] = TrueType
    }
    class TFalse extends TBool {
      type If[TrueType <: Up, FalseType <: Up, Up] = FalseType
    }
    it("If型を利用する"){
      object test {
        type X[T <: TBool] = T#If[String,Int,Any]
      }
      import test._
      val x:X[TTrue] = "Hi"
      x should equal("Hi")
    }
  }
}
