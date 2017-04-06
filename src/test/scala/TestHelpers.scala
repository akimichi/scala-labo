package scala_labo

trait helpers {
  import org.scalatest.matchers._

  import org.scalatest.Tag
  object DatabaseTest extends Tag("DatabaseTest")
  
  def anInstanceOf[T](implicit manifest: Manifest[T]) = { 
    val clazz = manifest.erasure.asInstanceOf[Class[T]] 
    new BePropertyMatcher[AnyRef] { 
      def apply(left: AnyRef) = BePropertyMatchResult(clazz.isAssignableFrom(left.getClass), "an instance of " + clazz.getName) 
    }
  }

  /* テストデータの準備 */
  val fixture = new {
    val jsons = new {
      val organisation_path = "src/test/resources/json/organisation.json"
      lazy val organisation = scala.io.Source.fromFile(organisation_path, "UTF-8").mkString
    }
  }
  object ExpensiveCalc {
    val NumInterations = 1000
    
    def expensiveCalc = calculatePiFor(0, 1000000)
    
    def calculatePiFor(start: Int, nrOfElements: Int): Double = {
      var acc = 0.0
      for (i <- start until (start + nrOfElements))
        acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
      acc
    }
  }

  object mongodb {
    import com.mongodb.casbah.Imports._
    import com.mongodb.{CommandResult}
    // import com.codahale.jerkson.Json._
    import com.mongodb.util._
    import scala.collection.JavaConversions._

    com.mongodb.casbah.commons.conversions.scala.RegisterConversionHelpers()
    def connection(dbname:String, colname:String):(MongoDB, MongoCollection) = {
      val db = MongoConnection()(dbname)
      val collection = db(colname)
      (db,collection)
    }

    // resourcesディレクトリ下にある fileをMongoDBに読みこむ
    val loadJsonToMongoDB = (collection:MongoCollection) => (source:String) =>  {
      var record = JSON.parse(source).asInstanceOf[BasicDBObject]
      collection.insert(record)
    }
    def loadJson(collection:MongoCollection, file:String):Unit = {
      import scala.io._
      val source:Source = try {
        // import scala_labo.helpers.mongodb._          
        val content = Source.fromFile("src/test/resources/%s".format(file),"UTF-8")
        loadJsonToMongoDB(collection)(content.mkString)
        content
      } catch {
        case ex => throw ex
      }
      source.close()
    }

    def aggregationResult(collectionName:String, pipeline: MongoDBList)(implicit database:MongoDB):Either[Throwable, MongoDBList] = {
      val command_result:CommandResult = database.command(MongoDBObject("aggregate" -> collectionName, "pipeline" -> pipeline))
      command_result.ok() match {
        case true => {
          Right(command_result.as[MongoDBList]("result"))
        }
        case false => Left(new Exception(command_result.getErrorMessage()))
      }
    }
  }
  
}

