import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * 文字列の基本的な方法を示す
 * 
 */
class StringSpec extends FunSpec with BeforeAndAfterAll {
  describe("Stringにおいて") {
    it("String#charAt"){
      "abc"(0) should equal('a')
    }
    it("String#startsWith"){
      "abc".startsWith("ab") should equal(true)
    }
    // 
    it("String#endsWith"){
      "abc".endsWith("c") should equal(true)
      "abc".endsWith("b") should equal(false)
    }
    describe("String#split を使って文字列を分割する") {
      it("ドット . で区切る際は ' を使う"){
        "abc.def".split('.').toList should equal(List("abc","def"))
        info("以下の場合は、期待通りには分割されない")
        "abc.def".split(".").toList should equal(Nil)
      }
      it("archetype_id を分割する"){
        val source = "openEHR-EHR-OBSERVATION.FIRST4_blood_pressure.v1"
        val result_by_dash = source.split("-")
        result_by_dash.size should equal(3)
        val result_by_underscore:List[String] = source.split("_").toList
        result_by_underscore(1) should equal("blood")

      }
      it("node_idを分割する"){
        "at0001.1.1".split("\\.").toList should equal(List("at0001","1","1"))
        "at0001.1.1".split("\\.").toList(0) should equal("at0001")
        "at0001".split("\\.").toList should equal(List("at0001"))
      }
      it("node_pathを分割する"){
        "/data[at0002]/events[at0003]/data[at0001]/items[at0004]/value".split("/").toList should equal(List("","data[at0002]","events[at0003]","data[at0001]","items[at0004]","value"))
      }
    }
    it("String#split の境界事例") {
      "ABCDE".split("_").size should equal(1)
    }
    it("map で変換する") {
      "/data[at0001]/".map(_ match {
        case '/' => '#'
        case '[' => '('
        case ']' => ')'
        case x => x
      }) should equal("#data(at0001)#")
    }
  }
}

