import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Cake Patternの基本的な用法を示す
 */
class CakePatternSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Cake Patternを用いて") {
    describe("Scalable Component で紹介された例") {
            /*
      class SymbolTable {
        class Name { /* name specific operations */ }
        class Type { /* subclasses of Type and type specific operations */ }  
        class Symbol { /* subclasses of Symbol and symbol specific operations */ }  
        object definitions { /* global definitions */ }
        // other elements
      }
      */
      trait Names {
        class Name { /* name specific operations */ }
      }
      trait  Symbols { self : Names with Types => 
        class Symbol { /* subclasses of Symbol and symbol specific operations */ }
      }
      trait Definitions { self : Names with Symbols => 
        object definitions {  }
      }
      trait Types { self : Names with Symbols with Definitions =>
        class Type { /* subclasses of Type and type specific operations */ }
      }
      class SymbolTable extends Names with Types with Symbols with Definitions
      trait Trees { self : Names with Symbols with Definitions =>
        class Tree { /* Asbtract Syntax Tree */ }
      }
      class ScalaCompiler extends SymbolTable with Trees

      info("コンパイラのシンボル操作と型操作にログ機能を付加する")
      trait LogSymbols extends Symbols { self : Names with Types => 
        import java.io.PrintStream
        def log: PrintStream
      }
      trait LogTypes extends Types {self : Names with Symbols with Definitions =>
        import java.io.PrintStream
        def log: PrintStream
      }
      class LoggedCompiler extends ScalaCompiler with LogSymbols with LogTypes {
        import java.io.PrintStream
        val log: PrintStream = System.out
      }
    }
    describe("Scala for the Impatient,p.257 で紹介された例") {
      trait LoggerComponent {
        trait Logger { def log(message:String):String = message }
        /* 抽象メンバー */
        val logger:Logger
        
        class FileLogger(val file:String) extends Logger
      }
      /*
       * LoggerComponent を self として指定しているので、 AuthComponent の実装は LoggerComponent を mixin することが必須であり、
       * mixinされたことを前提として LoggerComponent 内の定義にアクセス可能である。
       */ 
      trait AuthComponent { self: LoggerComponent =>
        trait Auth {
          def validate:Boolean
        }
        val auth:Auth
        class MockAuth(file:String) extends Auth {
          def validate:Boolean ={
            logger.log(file) /* 抽象メンバー logger へのアクセス */
            true
          }
        }
      }
      it("object で複数コンポーネントを合成する例") {
        object LoggerAuthService extends LoggerComponent with AuthComponent {
          val logger = new FileLogger("test.log")
          val auth = new MockAuth("users.txt")
        }
        LoggerAuthService.auth.validate should equal(true)
        LoggerAuthService.logger.log("message") should equal("message")
        LoggerAuthService.logger.file should equal("test.log")
      }
      it("無名クラスで複数コンポーネントを合成する例") {
        val logger_auth_service = new LoggerComponent with AuthComponent {
          val logger = new FileLogger("test.log")
          val auth = new MockAuth("users.txt")
        }
        logger_auth_service.auth.validate should equal(true)
        logger_auth_service.logger.log("message") should equal("message")
        logger_auth_service.logger.file should equal("test.log")
      }
      it("クラスで複数コンポーネントを合成する例") {
        class LoggerAuthService(val log_file:String, val auth_file:String) extends LoggerComponent with AuthComponent {
          val logger = new FileLogger(log_file)
          val auth = new MockAuth(auth_file)
        }
        val logger_auth_service = new LoggerAuthService("test.log", "users.txt")
        logger_auth_service.auth.validate should equal(true)
        logger_auth_service.logger.log("message") should equal("message")
        logger_auth_service.logger.file should equal("test.log")
      }
    }
  }
}
