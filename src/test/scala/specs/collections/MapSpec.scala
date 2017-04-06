import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Mapの基本的な用法を示す
 */
class MapSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  type Code = String
  type Lang = String
  type Text = String
  type Description = String                                      
  type TerminologyName = String                                      
  type ConceptId = String

  def fixture = new {
    val simple_map = Map("1" -> "value1", "2" -> "value2")
    val terms:Map[Pair[Code,Lang],Pair[Option[Text],Option[Description]]] = Map(("at0000","jp") -> (Some("blood pressure"),Some("intravenour pressure")),
                                                                                ("at0001","en") -> (Some("palse"),None))
  }
  describe("simple_mapを利用して") {
    val simple_map = Map("1" -> "value1", "2" -> "value2")
    it("Map#filterKeysでキーの条件を満たす要素だけのMapを返す"){
      simple_map.filterKeys{key => key == "2"} should equal(Map("2" -> "value2"))
    }
    it("Map#nonEmptyで空でないことを確認する"){
      simple_map.nonEmpty should equal(true)
    }
    it("Map#values"){
      simple_map.values.toList should equal(List("value1","value2"))
    }
    it("Map#keys"){
      simple_map.keys.toList should equal(List("1","2"))
    }
    it("toListでMapをListに変換する") {
      simple_map.toList should equal(List(("1","value1"), ("2","value2")))
    }
    
  }
  describe("Mapに対して") {
    it("++で連結する"){
      Map(1 -> "1") ++ Map(2 -> "2") should equal(Map(1 -> "1", 2 -> "2"))
    }
    it("Map.sizeでマップのサイズを取得する"){
      fixture.simple_map.size should equal(2)
    }
    it("getでキーを指定して、要素を取り出す"){
      fixture.simple_map.get("1") should equal(Some("value1"))
    }
    it("foreachとパターンマッチで要素を取り出す") {
      fixture.terms.foreach { case (key,value) =>
        key match {
          case (term,lang) if term == "at0000" => {
            lang should equal("jp")
          }
          case _ => {}
        }
      }
    }
    it("forとパターンマッチで要素を取り出す") {
      for {
        (key, value) <- fixture.terms if key._1 == "at0001"
      } {
        key._2 should equal("en")
      }
      info("タプルを入れ子にして取り出せる")
      for {
        ((code,lang), value) <- fixture.terms if code == "at0001"
      } {
        lang should equal("en")
      }
    }
    it("Map#maxBy"){
      Map('first -> 1, 'middle -> 2, 'last -> 3).maxBy{item =>
	item._2
      }  should equal(Pair('last,3))
      
    }
    it("foreachで要素を繰り返し処理する") {
      fixture.simple_map.foreach { case (key,value) =>
        value should equal("value%s".format(key))
      }
    }
    it("headOptionでMapの先頭を取得する") {
      fixture.simple_map.headOption should equal(Some(Pair("1","value1")))
      val (key,value) = fixture.simple_map.headOption.get
      key should equal("1")
    }
    it("varと+=演算子を用いて、immutable なMapに要素を追加する") {
      var map = Map("ja" -> "テキスト")
      map += ("en" -> "text")
      map.get("en") should equal(Some("text"))
    }
  }   
}
