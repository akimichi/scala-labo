 /*
class ReaderMonadSpec extends FunSpec with ShouldMatchers {
  describe("Reader Monadについて"){
    /**
     * dependency "framework"
     */
    package object di {
      // Reader Monad
      case class Reader[C, A](g: C => A) {
        def apply(c: C) = g(c)
        def map[B](f: A => B): Reader[C, B] = Reader(c => f(g(c)))
        def flatMap[B](f: A => Reader[C, B]): Reader[C, B] = Reader(c => f(g(c))(c))
      }
      implicit def reader[A, B](f: A => B) = Reader(f)
      def pure[C, A](a: A) = Reader[C, A](con => a)
    }

    /**
     * Database API mock package
     */
    package object db {
      // DB Connection Class
      case class Connection(driver: String, url: String)
      // mixin to use some DB API functions
      trait DBAPI {
        def setUserPwd(id: String, pwd: String): Connection => Unit =
          c => {
            println("setUserPwd with Connection: " + c)
          }
        def getUserPwd(id: String): Connection => String =
          c => {
            println("getUserPwd with Connection: " + c)
            "3"
          }
      }
    }

    trait ConnectionProvider {
      def apply[A](f: Reader[Connection, A]): A
    }
    
    object ConnectionProvider {
      def apply(driver: String, url: String) =
        new ConnectionProvider {
          def apply[A](f: Reader[Connection, A]): A = f(Connection(driver, url))
        }
    }
    it("Reader Monadを使う") {
      import di._
      import db._

      trait MyBusinessLogic extends DBAPI {
        // Monad comprehension
        def changePwd(userid: String, oldPwd: String, newPwd: String): Reader[Connection, Boolean] = {
          for {
            pwd <- getUserPwd(userid)
            eq <- if (pwd == oldPwd) for {_ <- setUserPwd(userid, newPwd)} yield true
                  else pure[Connection, Boolean](false)
          } yield eq
        }
        def businessApplication(userid: String, oldPwd: String, newPwd: String): ConnectionProvider => Unit =
          r => {
            r(changePwd(userid, oldPwd, newPwd))
          }
      }

      /**
       * --------------------------------------------------------------------------------
       */
      // object ExecutingMain extends App with MyBusinessLogic {

      //   lazy val sqliteTestDB = ConnectionProvider("org.sqlite.JDBC", "jdbc:sqlite::memory:")
      //   lazy val mysqlProdDB = ConnectionProvider("org.gjt.mm.mysql.Driver", "jdbc:mysql://prod:3306/?user=one&password=two")

      //   def runInTest[A](f: ConnectionProvider => A): A = f(sqliteTestDB)

      //   def runInProduction[A](f: ConnectionProvider => A): A = f(mysqlProdDB)

      //   runInTest(businessApplication("Some Test", "1", "2"))
      //   runInProduction(businessApplication("Some Test", "3", "2"))
      // }
    }
  }
}
*/

