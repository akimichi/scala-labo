import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

//import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.{MongoDBObject,MongoDBObjectBuilder}
import com.mongodb.casbah.commons._
/*
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._
*/ 

/**
 * Salatの基本的な用法を示す
 */

/*
class SalatSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Salatを使う") {

    import helpers.mongodb._

    val (database,collection) = connection(dbname = "scala-labo",colname = "salat-test")
    collection.drop()

    val data = MongoDBObject("key" -> "value")
/*
    case class SampleData(name: String, value: Option[MongoDBObject])
    val sample_data = SampleData("abc", Some(data))
    */
    
    case class User(no:Int, name: String)
    val user = User(1, "data")

//    val sample_data_grater = grater[SampleData]

    it("MongoDBObjectを含んだオブジェクトを格納する") {
      val db_object = grater[User].asDBObject(user) // ここでエラーが発生してテストが実行できない => [error] Could not run test SalatSpec: java.lang.NoSuchMethodError: com.mongodb.casbah.commons.MongoDBObject$.newBuilder()Lcom/mongodb/casbah/commons/MongoDBObjectBuilder;
      collection.insert(db_object)
    }

/*
    it("MongoDBObjectを含んだオブジェクトを検索する") {
      val query = MongoDBObject("name" -> "abc")
      collection.findOne(data) match {
        case Some(data) => {
          val sample_data_object = sample_data_grater.asObject(data)
        }
        case None => fail("MongoDBObjectを含んだオブジェクトを検索出来ませんでした")
      }
    }
    */

  }
}
*/
