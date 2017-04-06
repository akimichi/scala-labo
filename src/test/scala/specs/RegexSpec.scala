import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Regexの基本的な用法を示す
 */
class RegexSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  import scala.util.matching.Regex

  def fixture = new {
    val simple = "abc123"
    val archetype_id = "openEHR-EHR-OBSERVATION.FIRST4_blood_pressure.v1"
    val node_path = "/protocol[at0011]/items"
  }
  
  describe("逆引きScala,p.79 で紹介された") {
    it("findFirstInはマッチした最初の文字列をOption型で返す") {
      val pattern:Regex = """[0-9]+""".r
      pattern.findFirstIn(fixture.simple) match {
        case Some(the_match) => {
          the_match should equal("123")
        }
        case None => fail()
      }
    }
    describe("case文でmatchさせる") {
      it("archetype_idをmatchさせる"){
        val pattern:Regex = """^([a-zA-Z0-9]+)-([A-Z]+)-([A-Z]+).([a-zA-Z0-9_]+).(v\d+)$""".r
        fixture.archetype_id match {
          case pattern(rm_originator,rm_name,rm_entity,domain_concept,version_id) => {
            rm_originator should equal("openEHR")
            rm_name should equal("EHR")
            rm_entity should equal("OBSERVATION")
            domain_concept should equal("FIRST4_blood_pressure")
            version_id should equal("v1")

            val pattern = """^([A-Z][A-Z0-9]*)_([a-zA-Z0-9_]+)$""".r
            domain_concept match {
              case pattern(namespace,domain_concept) => {
                namespace should equal("FIRST4")
                domain_concept should equal("blood_pressure")
              }
              case _ => fail()
            }
          }
          case _ => fail()
        }
      }
      it("node_idをmatchさせる"){
        val pattern:Regex = """^([\w\d]+)(?:\[((?:at|ac)[.\d]+)\])?$""".r
        "data[at0001]" match {
          case pattern(rmAttrName,code) => {
            rmAttrName should equal("data")
            code should equal("at0001")
          }
          case _ => fail()
        }
        "data" match {
          case pattern(rmAttrName,code) => {
            rmAttrName should equal("data")
            code should equal(null)
          }
          case _ => fail()
        }
        "events[at0078.1.1]" match {
          case pattern(rmAttrName,code) => {
            rmAttrName should equal("events")
            code should equal("at0078.1.1")
          }
          case _ => fail()
        }
      }
    }
    val pattern:Regex = """^([a-zA-Z0-9]+)(\[([atc][0-9.])\])?$""".r
    it("replaceAllInで文字列を正規表現で置換する") {
      val pattern:Regex = """/""".r
      val result = pattern.replaceAllIn(fixture.node_path, {
        _ => "#"
      })
      result should equal("#protocol[at0011]#items")
    }
    it("正規表現で文字列をsplitする"){
      val pattern = "[, ]".r
      val result = pattern.split("abc d,e").toList
      result should equal(List("abc","d","e"))
    }
  }
}
