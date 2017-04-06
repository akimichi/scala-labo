import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * MongoDBの検索結果をmonadとして構成する
 */

// class MongoDBSpec extends FunSpec with ShouldMatchers with scala_labo.helpers {
//   import com.mongodb.casbah.Imports._
//   import com.mongodb.{CommandResult}
//   import mongodb._


//   implicit val db = MongoConnection()("scala-labo")
//   val results = db("results")
//   results.drop()
//   List(MongoDBObject("_id" -> 1, "result" -> 1),
//        MongoDBObject("_id" -> 2, "result" -> 2),
//        MongoDBObject("_id" -> 3, "result" -> 3)
//      ).foreach {
//     data => {
//       results.insert(data)
//     }
//   }

//   trait ResultSet[A] {
//     def stream:Stream[A]
    
//     // def bind[B](m:ResultSet[A], f:A=>ResultSet[B]):ResultSet[B] = f(m.stream.head)
//     // def unit[A](a:A):ResultSet[A] = new ResultSet[A] { def stream = Stream.cons(a,Stream.empty) }
//     // def map[B](f: A => B):ResultSet[B] = bind(this, {a: A => unit(f(a))});
//     // def flatMap[B](f: A => ResultSet[B]): ResultSet[B] = bind(this,f)
//   }

//   class DBResultSet(val _stream:Stream[DBObject]) extends ResultSet[DBObject] {
//     def stream:Stream[DBObject] = _stream
//     def map[B](f: DBObject => B):ResultSet[B] = new ResultSet[B]{
//       val stream:Stream[B] = _stream.map(f)
//     }
//     def flatMap[B](f: DBObject => ResultSet[B]): ResultSet[B] = {
//       f(_stream.head)
//     }
//   }
//   object DBResultSet {
//     def apply(stream:Stream[DBObject]) = new DBResultSet(stream)
//   }

//   describe("DBResultSetを使う"){
//     val cursor:MongoCursor = results.find(MongoDBObject())
//     val db_resultset = DBResultSet(cursor.toStream)
//     db_resultset.stream.size should equal(3)
//     it("キーを指定して、値のみを取り出す", DatabaseTest){
//       val int_resultset = for {
//         result <- db_resultset
//       } yield {
//         result("result")
//       }
//       int_resultset.stream.head should equal(1)
//       int_resultset.stream.tail should equal(Stream(2,3))
//     }
    
//     it("MongoDBObjectとして取り出す", DatabaseTest){
//       val obj_resultset = for {
//         result <- db_resultset
//       } yield {
//         result
//       }
//       obj_resultset.stream.head should equal{
//         MongoDBObject("_id" -> 1, "result" -> 1)
//       }
//     }
//   }
// }



