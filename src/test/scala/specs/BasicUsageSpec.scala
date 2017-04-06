import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Specsの基本的な用法を示す
 *
 * c.f. http://www.scalatest.org/user_guide/using_matchers
 * 
 */
// class BasicUsageSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
//   var global:Option[Boolean] = Some(true)
//   // var global:Option[Boolean] = None
  
//   // override def beforeAll(configMap: Map[String, Any]) {
//   //   global = Some(true)
//   // }

//   describe("Specsにて") {
//     it("should matcher を使う") {
//       1 should equal(1)
// 	}
//     it("beforeAll を確認する") {
//       global should equal(Some(true))
// 	}
//   }
//   describe("should matcher の用法を確認する") {
//     describe("should matcher") {
//       it("equal"){
//         "hello" should equal{
//           "hello"
//         }
//       }
//       it("Greater and less than"){
//         1 should be > 0
//       }
//     }
//   }
//   describe("itメソッドの実行順序を確認する") {
//     var item = 1
//     it("first") {
//       item should equal(1)
//     }
//     it("second") {
//       item should not equal(2)
//     }
//     it("third") {
//       3 should equal(3)
//     }
//     it("fourth") {
//       item = 2
//       item should equal(2)
//     }
//   }
// }

