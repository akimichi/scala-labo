import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Json4s処理の基本的な用法を示す
 * c.f. http://json4s.org/
 * c.f. https://github.com/json4s/json4s
 * 
 */
class Json4sSpec extends FunSpec with ShouldMatchers {
  describe("パースする") {
    import org.json4s._
    import org.json4s.native.JsonMethods._
    implicit val formats = DefaultFormats
    
    it("JSON文字列をパースして、JObjectに変換する"){
      parse(""" { "numbers" : [1, 2, 3, 4] } """) should equal(JObject(List(("numbers",JArray(List(JInt(1), JInt(2), JInt(3), JInt(4)))))))
      compact(render(parse(""" { "numbers" : [1, 2, 3, 4] } """))) should equal("""{"numbers":[1,2,3,4]}""")
    }
    it("JSON文字列をパースして、valuesメソッドでMapに変換する"){
      parse(""" { "numbers" : [1, 2, 3, 4] } """).values should equal(Map("numbers" -> List(1,2,3,4)))
    }
  }
  describe("scalaオブジェクトを render関数でJSON文字列にシリアライズする"){
    import org.json4s.native.JsonMethods._
    import org.json4s.JsonDSL._

    it("List[Int]"){
      val json = List(1, 2, 3)
      compact(render(json)) should equal("[1,2,3]")
    }
    // it("Map[String,Any]"){
    //   val json = Map("a" -> 1, "b" -> "2")
    //   compact(render(json)) should equal("[1,2,3]")
    // }
  }
  describe("Jsonオブジェクトに対してクエリーを実行する"){
    import org.json4s._
    import org.json4s.native.JsonMethods._
    val json = parse("""
                     { "name": "joe",
                     "children": [
                       {
                         "name": "Mary",
                         "age": 5
                       },
                       {
                         "name": "Mazy",
                         "age": 3
                       }
                     ]}""")

    (json \ "children")(0) should equal{
      JObject(List(("name",JString("Mary")), ("age",JInt(5))))
    }
    
    (json \ "children")(1) \ "name" should equal{
      JString("Mazy")
    }

    
    json \\ classOf[JInt] should equal{
      List(5, 3)
    }
    
    json \ "children" \\ classOf[JString] should equal{
      List("Mary", "Mazy")
    }
  }
}


