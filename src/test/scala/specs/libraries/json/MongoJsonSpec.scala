import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * mongo-java-driver を用いたJSON処理の基本的な用法を示す
 */
// class MongoJsonSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
//   describe("mongo-java-driverを用いて") {
//     import com.mongodb.util.JSON
//     // import com.mongodb.BasicDBObject
//     import com.mongodb.casbah.Imports._
    
//     it("JSONをパースする") {
//       val json = """{"timestamp": { "$date" : "2012-08-31T17:30:01.882Z"}}"""
//       val deserialized = JSON.parse(json)
//       // println(deserialized)
//       deserialized.isInstanceOf[BasicDBObject] should equal(true)
//       val deserialized_as_mongodbobj = wrapDBObj(deserialized.asInstanceOf[BasicDBObject])
//       deserialized_as_mongodbobj.isInstanceOf[MongoDBObject] should equal(true)
      
//       import org.scala_tools.time.Imports.{DateTime, DateTimeZone}
//       val timezone:DateTimeZone = DateTimeZone.forID("Asia/Tokyo")
//       // deserialized_as_mongodbobj.getAs[DateTime]("timestamp") should equal( Some(new DateTime(2012, 8, 31, 17, 30, 1, 882, timezone)))
        
//     }
//   }
// }
