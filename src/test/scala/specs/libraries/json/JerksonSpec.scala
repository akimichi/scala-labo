import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * JerksonをもちいたJSON処理の基本的な用法を示す
 */
class JerksonSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  import com.codahale.jerkson.Json._
  
  describe("jerksonを用いて") {
    
    it("jerksonのパーサーでJSONをパースする") {
      val ast = parse[Map[String,List[Int]]](""" { "numbers" : [1, 2, 3, 4] } """)
      ast.get("numbers") should equal(Some(List(1,2,3,4)))
    }
    it("jerksonでscalaオブジェクトからJSON文字列を生成する") {
      generate(List(1, 2, 3)) should equal("[1,2,3]")
      generate(Map("key"  -> List(1, 2, 3))) should equal("""{"key":[1,2,3]}""")
    }
    // it("jerksonのパーサーで日付型のJSONをパースする") {
    //   val ast = parse[Map[String,List[Int]]](""" { "timestamp" :  } """)
    //   ast.get("numbers") should equal(Some(List(1,2,3,4)))
    // }
  }
}
