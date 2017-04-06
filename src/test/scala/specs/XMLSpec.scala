import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * XML の基本的な用法を示す
 */
class XMLSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("XML literal") {
    val xml:scala.xml.Elem = <root>
    </root>
  }
  // describe("XML.loadString") {
  // }
  describe("XML.loadString") {
    it("属性をとりだす"){
      val string = """<terminology name="HS014" version="060919jlacunyou">	
	    <concept id="1A0060000001920"/>
      </terminology>"""
      val xml:scala.xml.Elem = scala.xml.XML.loadString(string)
      val name:String = (xml \ "@name").text
      name should equal("HS014")
      // xml.toString should equal(string)
    }                                         
    it("パースに失敗する"){
      intercept[org.xml.sax.SAXException] {
        val xml = scala.xml.XML.loadString("""<terminology n""")
      }
    }                                         
  }                                         
}      
