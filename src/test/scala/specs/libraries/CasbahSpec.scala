import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * Casbahの基本的な用法を示す
 */
// import com.mongodb.casbah.Imports._
// import com.mongodb.{CommandResult}

// class CasbahSpec extends FunSpec with ShouldMatchers with scala_labo.helpers {
//   import mongodb._
  
//   implicit val (database,collection) = connection(dbname = "scala-labo",colname = "test")
//   collection.drop()

//   for(i <- 1 to 100) {
//     val data = MongoDBObject("id" -> i,
//                              "name" -> "name%s".format(i),
// 	                         "lname" -> "Simpson",
//                              "fname" -> "Bobby",
//                              "scores" -> MongoDBList(
//                                MongoDBObject("score" -> i * 11,
//                                              "dname" -> "ACADEMICS PLUS SCHOOL DISTRICT",
//                                              "sname" -> "Test School 6040702",
//                                              "tyear" -> "2007",
// 			                                 "tname" -> "Lit_Scale",
// 			                                 "grade" -> (i * 3).toString)))
//     collection.insert(data)
//   }
  
//   describe("基本操作") {
//     it("空の MongoDBObject を作る", DatabaseTest){
//       val vacant:MongoDBObject = MongoDBObject()
//       vacant.isEmpty should equal(true)
//     }
//     it("++でMongoDBObject同士を結合する", DatabaseTest){
//       (MongoDBObject("a" -> 1) ++ MongoDBObject("b" -> 2)) should equal{
//         MongoDBObject("a" -> 1,"b" -> 2)
//       }
      
//       List(MongoDBObject("a" -> 1),MongoDBObject("b" -> 2)).reduceLeft{(right,left) => right ++ left } should equal{
//         MongoDBObject("a" -> 1,"b" -> 2)
//       }
//     }
//     describe("json文字列をMongoDBObjectに変換する"){
//       import com.mongodb.util.JSON
//       it("単純な例", DatabaseTest){
//         val json = """{"key": "value"}}"""
//         val deserialized:BasicDBObject = JSON.parse(json).asInstanceOf[BasicDBObject]
//         val deserialized_as_mongodbobj = wrapDBObj(deserialized)
//         deserialized_as_mongodbobj.isInstanceOf[MongoDBObject] should equal(true)
//       }
//       it("organisation.jsonのような複雑な例", DatabaseTest){
//         val json = fixture.jsons.organisation
//         val deserialized:BasicDBObject = JSON.parse(json).asInstanceOf[BasicDBObject]
//         val deserialized_as_mongodbobj = wrapDBObj(deserialized)
//         deserialized_as_mongodbobj.isInstanceOf[MongoDBObject] should equal(true)
//       }
//     }
//     describe("applyメソッドで MongoDBObject の一部を抽出する"){
//       describe("単純な例"){
//         val mongodbObj = MongoDBObject("root" -> MongoDBObject("leaf" -> "value"))
//         it("パス表記では取りだせない", DatabaseTest){
//           mongodbObj("root") should equal(MongoDBObject("leaf" -> "value"))
//           wrapDBObj(mongodbObj("root").asInstanceOf[DBObject])("leaf") should equal("value")
//           info("パス表記では取りだせない")
//           intercept[java.util.NoSuchElementException]{
//             wrapDBObj(mongodbObj("root.leaf").asInstanceOf[DBObject]) should equal("value")
//           }
//         }
//         it("containsもパス表記はダメ", DatabaseTest){
//           mongodbObj.contains("root") should equal(true)
//           info("containsもパス表記はダメ")
//           mongodbObj.contains("root.leaf") should equal(false)
//           wrapDBObj(mongodbObj("root").asInstanceOf[DBObject]).contains("leaf") should equal(true)
//         }
//         it("expandメソッドはパス表記が可能", DatabaseTest){
//           info("expandメソッドはパス表記が可能")
//           mongodbObj.expand[String]("root.leaf") should equal(Some("value"))
//         }
//       }
//       it("src/test/resouces/json/organisation.jsonを用いた例では、内部に MongoDBListを含むためにexpandが失敗する", DatabaseTest){
//         import com.mongodb.util.JSON

//         val json = fixture.jsons.organisation
//         val deserialized:BasicDBObject = JSON.parse(json).asInstanceOf[BasicDBObject]
//         val deserialized_as_mongodbobj = wrapDBObj(deserialized)
//         intercept[java.lang.ClassCastException]{
//           deserialized_as_mongodbobj.expand[String]("details.items.at0007.name") should equal(Some("value"))
//         }
//       }
//     }
//     it("Scala の Map を DBObject に変換する", DatabaseTest) {
//       val map = Map("foo" -> "bar")
//       map.asDBObject.isInstanceOf[DBObject] should equal(true)
//       println(map.asDBObject.toString)

//       Map("foo" -> List("bar","foobar")).asDBObject.isInstanceOf[DBObject] should equal(true)
//     }
//     it("MongoDBObjectを保存して、検索する", DatabaseTest) {
//       val data = MongoDBObject("test" -> "data")
//       collection.insert(data)
//       collection.findOne(data) match {
//         case Some(data) => {
//           assert(true)
//         }
//         case None => fail()
//       }
//       // projectionを使う
//       val result = collection.findOne(MongoDBObject("test" -> "data"),MongoDBObject("test" -> 1))
//       result.get.getAs[String]("test") should equal(Some("data"))
      
//     }
//     it("MongoDBObjectを使う", DatabaseTest) {
//       val mongodbobject = MongoDBObject("key" -> "value")
//       mongodbobject.toString should equal("""{ "key" : "value"}""")

//       val builder = MongoDBObject.newBuilder
//       builder += ("x" -> 5)
//       builder.result should equal(MongoDBObject("x" -> 5))
//     }
//     it("空のMongoDBObjectを使う", DatabaseTest) {
//       val mongodbobject = MongoDBObject()
//       mongodbobject.isEmpty should equal(true)
//     }
    
//     it("MongoDBListを使う", DatabaseTest) {
//       val list = MongoDBList(MongoDBObject("one" -> 1), MongoDBObject("two" -> 2))
//       list.size should equal(2)

//       val mongodblist_builder = MongoDBList.newBuilder
//       mongodblist_builder += "foo"
//       mongodblist_builder += "bar"
//       mongodblist_builder.result.size should equal(2)
//     }
//   }
//   describe("基本データ型") {
//     it("JodaTime の DateTime型を保存できる", DatabaseTest) {
//       import org.scala_tools.time.Imports._
//       import com.mongodb.casbah.commons.conversions.scala._
//       com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers()
      
//       val point_in_time = new DateTime(2012, 1, 1, 12, 0, 0, 0)
//       val data = MongoDBObject("timestamp" -> point_in_time)
//       collection.insert(data)
//       collection.findOne(data) match {
//         case Some(data) => {
//           data.getAs[DateTime]("timestamp") should equal(Some(point_in_time))
//           data.getAs[DateTime]("timestamp").get.toString() should equal("2012-01-01T12:00:00.000+09:00")
//         }
//         case None => fail()
//       }
//       info("日付型に対して比較演算子で比較できる")
//       val later_time = new DateTime(2013, 1, 1, 12, 0, 0, 0)
//       collection.insert(MongoDBObject("timestamp" -> later_time))
      
//       // collection.find(MongoDBObject("timestamp" -> MongoDBObject("$date" -> new java.util.Date(109, 01, 02, 0, 0, 0).getTime))).size should equal(0)
//       collection.find(MongoDBObject("timestamp" -> MongoDBObject("$gt" -> point_in_time))).size should equal(1)
//       collection.find(MongoDBObject("timestamp" -> MongoDBObject("$gte" -> point_in_time))).size should equal(2)
      
//     }
//   }
  
//   describe("クエリを実行する") {
//     describe("Exampleでクエリーする") {
//       it("直接値を指定する", DatabaseTest) {
//         val example = MongoDBObject("lname" -> "Simpson")
//         collection.findOne(example) match {
//           case Some(result) => {
//             result.getAs[String]("lname") should equal(Some("Simpson"))
//           }
//           case None => fail()
//         }
//       }
//       it("Mapで値を指定する", DatabaseTest) {
//         val example = Map("lname" -> "Simpson")
//         collection.findOne(example) match {
//           case Some(result) => {
//             result.getAs[String]("lname") should equal(Some("Simpson"))
//           }
//           case None => fail()
//         }
//       }
//       it("比較演算子 $lt を指定する", DatabaseTest) {
//         val example = MongoDBObject("id" -> MongoDBObject("$lt" -> 3))
//         collection.findOne(example) match {
//           case Some(result) => {
//             result.getAs[Int]("id") should equal(Some(1))
//           }
//           case None => fail()
//         }
//       }
//     }
//     describe("述語でクエリーする") {
//       object test {
//         // def filter (p: (DBObject) ⇒ Boolean) : Iterable[DBObject]
//         def filter[T>:DBObject](criteria: T => Boolean)(col:MongoCollection) = col.filter(criteria)
//         // def find (p: (DBObject) ⇒ Boolean) : Option[DBObject]
//         def find[T>:DBObject](criteria: T => Boolean)(col:MongoCollection) = col.find(criteria)
//         // def even:DBObject => Boolean = _ % 2 == 0
//         // def evenFilter = filter(even) _
//       }
//       it("filter で検索する", DatabaseTest) {
//         import test._
//         filter({(i => i.keySet().contains("id"))})(collection).size should equal(100)
//         filter({(i => i.keySet().contains("id"))})(collection).size should equal(100)
//         //isInstanceOf[MongoDBList] should equal(true)
//       }
//       it("find で検索する", DatabaseTest) {
//         import test._
//         find({i =>
//           i.getAs[Int]("id").get % 3 == 0
//         })(collection) match {
//           case None => fail()
//           case Some(the_result) => {
//             the_result.getAs[Int]("id").get % 3 should equal(0)
//           }
//         }
//       }
//     }
//     describe("Query DSL を使う") {
//       val data = MongoDBObject("includes" -> MongoDBList(MongoDBObject("String" -> "blood_pressure"),
//                                                          MongoDBObject("Regex" -> "body_weight")))
//       collection.insert(data)
//       it("配列の内部を検索する", DatabaseTest) {
//         val query = MongoDBObject("includes" -> MongoDBObject("String" -> "blood_pressure"))
//         collection.findOne(data) match {
//           case Some(data) => {
//             data.getAs[MongoDBList]("includes") should equal(Some(MongoDBList(MongoDBObject("String" -> "blood_pressure"),
//                                                                               MongoDBObject("Regex" -> "body_weight"))))

//           }
//           case None => fail()
//         }
//       }
//       describe("openEHRの archetypeの配列に対する深い検索を実行する"){
//         /*
//          {
//                   "_id" : ObjectId("522e968a9f02c8e1a411cf8a"),
//                  "archetypeId" : [
//                          {
//                                  "full" : "openEHR-EHR-OBSERVATION.lab_test-immunology.v1",
//                                  "namespaceId" : "OPENEHR",
//                                  "rmOriginator" : "openEHR",
//                                  "rmName" : "EHR",
//                                  "rmEntity" : "OBSERVATION",
//                                  "domainConcept" : "lab_test-immunology",
//                                  "versionID" : "v1",
//                                  "conceptName" : "lab_test",
//                                  "archetypeLineage" : [
//                                          "openEHR-EHR-OBSERVATION.lab_test-immunology.v1"
//                                  ]
//                          }
//                  ]
//          }
//          {
//                  "_id" : ObjectId("522e968a9f02c8e1a411cf8b"),
//                  "archetypeId" : [
//                          {
//                                  "full" : "openEHR-EHR-OBSERVATION.lab_test-immunology-ANA.v1",
//                                  "namespaceId" : "OPENEHR",
//                                  "rmOriginator" : "openEHR",
//                                  "rmName" : "EHR",
//                                  "rmEntity" : "OBSERVATION",
//                                  "domainConcept" : "lab_test-immunology-ANA",
//                                  "versionID" : "v1",
//                                  "conceptName" : "lab_test",
//                                  "archetypeLineage" : [
//                                          "openEHR-EHR-OBSERVATION.lab_test-immunology.v1",
//                                          "openEHR-EHR-OBSERVATION.lab_test-immunology-ANA.v1"
//                                  ]
//                          }
//                  ]
//         */
//         val lab_test_immunology = {
//           val archetypeId = MongoDBObject("full" -> "openEHR-EHR-OBSERVATION.lab_test-immunology.v1",
//                                           "namespaceId" -> "OPENEHR",
//                                           "rmOriginator" -> "openEHR",
//                                           "rmName" -> "EHR",
//                                           "rmEntity" -> "OBSERVATION",
//                                           "domainConcept" -> "lab_test-immunology",
//                                           "versionID" -> "v1",
//                                           "conceptName" -> "lab_test")
//           archetypeId += "archetypeLineage" -> List("openEHR-EHR-OBSERVATION.lab_test-immunology.v1")
//           MongoDBObject("archetypeId" -> MongoDBList(archetypeId))
//         }
//         val lab_test_immunology_ANA = {
//           val archetypeId = MongoDBObject("full" -> "openEHR-EHR-OBSERVATION.lab_test-immunology-ANA.v1",
//                                           "namespaceId" -> "OPENEHR",
//                                           "rmOriginator" -> "openEHR",
//                                           "rmName" -> "EHR",
//                                           "rmEntity" -> "OBSERVATION",
//                                           "domainConcept" -> "lab_test-immunology-ANA",
//                                           "versionID" -> "v1",
//                                           "conceptName" -> "lab_test")
//           archetypeId += "archetypeLineage" -> List("openEHR-EHR-OBSERVATION.lab_test-immunology.v1", "openEHR-EHR-OBSERVATION.lab_test-immunology-ANA.v1")
//           MongoDBObject("archetypeId" -> MongoDBList(archetypeId))
//         }
//         collection.insert(lab_test_immunology)
//         collection.insert(lab_test_immunology_ANA)
//         it("lab_test_immunologyとともに、 lab_test_immunology_ANA もマッチさせる", DatabaseTest){
//           val query = MongoDBObject("archetypeId.archetypeLineage" -> "openEHR-EHR-OBSERVATION.lab_test-immunology.v1")
//           val result_set = collection.find(query)
//           result_set.length should equal(2)
//         }
//       }
//       describe("配列に対する深い検索を実行する"){
//         /*
//         // Document 1
//         """{ "foo" : [
//               {
//                 "shape" : "square",
//                 "color" : "purple",
//                 "thick" : false
//               },
//               {
//                 "shape" : "circle",
//                 "color" : "red",
//                 "thick" : true
//         }] }"""
//         // Document 2
//         { "foo" : [
//             {
//               "shape" : "square",
//               "color" : "red",
//               "thick" : true
//               },
//             {
//               "shape" : "circle",
//               "color" : "purple",
//               "thick" : false
//              }
//         ]}
//         */
//         val document1 = MongoDBObject("foo" -> MongoDBList(MongoDBObject("shape" -> "square",
//                                                                          "color" -> "purple",
//                                                                          "thick" -> false,
//                                                                          "weight" -> 1),
//                                                            MongoDBObject("shape" -> "circle",
//                                                                          "color" -> "red",
//                                                                          "thick" -> true,
//                                                                          "weight" -> 3)))
//         val document2 = MongoDBObject("foo" -> MongoDBList(MongoDBObject("shape" -> "square",
//                                                                          "color" -> "red",
//                                                                          "thick" -> true,
//                                                                          "weight" -> 2),
//                                                            MongoDBObject("shape" -> "circle",
//                                                                          "color" -> "purple",
//                                                                          "thick" -> false,
//                                                                          "weight" -> 3)))
        
//         collection.insert(document1)
//         collection.insert(document2)
//         it("通常の find では正確に検索できずに余分にマッチしてしまう", DatabaseTest){
//           val query = MongoDBObject("foo.shape" -> "square","foo.color" -> "purple")
//           val result_set = collection.find(query)
//           result_set.length should equal(2)
//         }
        
//         describe("$elemMatchで検索する"){
//           it("{$elemMatch : {shape : square, color : purple}", DatabaseTest){
//             val query = MongoDBObject("foo" -> MongoDBObject("$elemMatch" -> MongoDBObject("shape" -> "square","color" -> "purple")))
//             val result_set = collection.find(query)
//             result_set.length should equal(1)
//           }

//           it("{$elemMatch : {shape : {$eq : square} , color : {$eq : purple}}} ", DatabaseTest){
//             val query = MongoDBObject("foo" -> MongoDBObject("$elemMatch" -> MongoDBObject("shape" -> MongoDBObject("$eq" -> "square"),
//                                                                                            "color" -> MongoDBObject("$eq" -> "purple"))))
//             val result_set = collection.find(query)
//             result_set.length should equal(1)
//           }
//           it("{$elemMatch : {shape : square, weight : {$gte : 2}}", DatabaseTest){
//             val query = MongoDBObject("foo" -> MongoDBObject("$elemMatch" -> MongoDBObject("shape" -> "square", "weight" -> MongoDBObject("$gte" -> 2))))
//             val result_set = collection.find(query)
//             result_set.length should equal(1)
//           }

//         }
//       }
//       it("_id で検索する", DatabaseTest) {
//         val query = MongoDBObject("includes" -> MongoDBObject("String" -> "blood_pressure"))
//         collection.findOne(query) match {
//           case Some(the_result) => {
//             val object_id:ObjectId = the_result.getAs[ObjectId]("_id").get
//             info("""ここで _id を用いたクエリー MongoDBObject("_id" -> object_id) を準備する""")
//             val query_by_id = MongoDBObject("_id" -> object_id)
//             collection.findOne(query_by_id) match {
//               case Some(the_instance) => {
//                 the_instance.getAs[ObjectId]("_id") should equal(Some(object_id))
//                 // the_instance.getAs[String]("includes") should equal(Some(object_id))
//               }
//               case None => fail()
//             }
//           }
//           case None => fail()
//         }
//       }
//       it("$exists を使う", DatabaseTest) {
//         collection.findOne("includes.String" $exists true ).get should not equal(None)
//         collection.findOne("doesn't exist" $exists false ).get should not equal(None)
//       }
//       it("正規表現を使う", DatabaseTest) {
//         //val query = MongoDBObject("includes.Regex" -> ".*weight$".r, "includes.String" -> "blood_pressure")
//         //collection.findOne(query).get should not equal(None)
//         val query = MongoDBObject("includes.Regex" -> ".*weight$".r)
//         collection.findOne(query ++ ("includes.String" -> "blood_pressure")).get should not equal(None)
        
//       }
//     }
//   }

//   describe("new aggregation framework を使う") {
//     describe("http://jp.docs.mongodb.org/manual/tutorial/aggregation-examples/ の例") {
//       import com.codahale.jerkson.Json._
//       import scala.collection.JavaConversions._
//       com.mongodb.casbah.commons.conversions.scala.RegisterConversionHelpers()

//       // {"city": "ACMAR", "loc": [-86.51557, 33.584132], "pop": 6055, "state": "AL", "_id": "35004"}
//       //   val newyork = MongoDBObject("_id" -> "10280",
//       //                               "city" -> "NEW YORK",
//       //                               "state" -> "NY",
//       //                               "pop" -> 5574,
//       //                               "loc" -> MongoDBList(-74.016323,40.710537))

//       def loadZipJson(collection:MongoCollection,file:String):List[Map[String, Any]] =  {
//         import scala.io._
//         val source = try {
//           Source.fromFile("src/test/resources/%s".format(file),"UTF-8")
//         } catch {
//           case ex:Throwable => throw ex
//         }
//         var records:List[Map[String, Any]] = Nil
//         source.getLines.foreach {line:String => {
//           val record = parse[Map[String,Any]](line)
//           collection.insert(record)
//           records ::= record
//         }}
//         source.close()
//         records
//       }
//       val records = loadZipJson(collection, "zips.json")
//       it("JSONファイルの読み込みを確認する", DatabaseTest) {
//         records(0).get("city") should equal(Some("THAYNE"))
//       }

//       it("States with Populations Over 10 Million の例", DatabaseTest){
//         /*
//          * db.zipcodes.aggregate(
//          *   { $group :
//          *     { _id : "$state",
//          *       totalPop : { $sum : "$pop" } } },
//          *   { $match : {totalPop : { $gte : 10*1000*1000 } } } )
//          * => 7
//          */
//         val pipelines_builder = MongoDBList.newBuilder
//         for(pipeline <-List(Map("$group" -> Map("_id" -> "$state",
//                                                 "totalPop" -> Map("$sum" -> "$pop"))),
//                             Map("$match" -> Map("totalPop" -> Map("$gte" -> 10*1000*1000)))))  {
//                               pipelines_builder += pipeline.asDBObject
//                             }
//         aggregationResult("test", pipelines_builder.result) match {
//           case Right(result) => {
//             result.size should equal(7)
//           }
//           case Left(ex) => fail(ex)
//         }
//       }
//       it("Average City Population by State", DatabaseTest) {
//         /*
//          * db.zipcodes.aggregate(
//          *   { $group :
//          *     { _id : { state : "$state", city : "$city" },
//          *       pop : { $sum : "$pop" } } },
//          *   { $group :
//          *     { _id : "$_id.state",
//          *       avgCityPop : { $avg : "$pop" } } } )
//          */
//         val pipelines = MongoDBList(MongoDBObject("$group" ->
//                                                   MongoDBObject("_id" -> MongoDBObject("state" -> "$state", "city" -> "$city"),
//                                                                 "pop" -> MongoDBObject("$sum" -> "$pop"))),
//                                     MongoDBObject("$group" ->
//                                                   MongoDBObject("_id" -> MongoDBObject("_id" -> "$_id.state"),
//                                                                 "avgCityPop" -> MongoDBObject("$avg" -> "$pop"))))
//         aggregationResult("test", pipelines) match {
//           case Right(result) => {
//             result.size should equal(52)
//           }
//           case Left(ex) => fail(ex)
//         }
//       }
//       it("Largest and Smallest Cities by State", DatabaseTest){
//         /*
//          * db.zipcodes.aggregate( { $group:
//          *                          { _id: { state: "$state", city: "$city" },
//          *                            pop: { $sum: "$pop" } } },
//          *                        { $sort: { pop: 1 } },
//          *                        { $group:
//          *                          { _id : "$_id.state",
//          *                            biggestCity:  { $last: "$_id.city" },
//          *                            biggestPop:   { $last: "$pop" },
//          *                            smallestCity: { $first: "$_id.city" },
//          *                            smallestPop:  { $first: "$pop" } } },
//          *
//          *                        // the following $project is optional, and modifies the output format.
//          *                        { $project:
//          *                          { _id: 0,
//          *                            state: "$_id",
//          *                            biggestCity:  { name: "$biggestCity",  pop: "$biggestPop" },
//          *                            smallestCity: { name: "$smallestCity", pop: "$smallestPop" } } } )
//          */
//         val pipelines = MongoDBList(MongoDBObject("$group" ->
//                                                   MongoDBObject("_id" -> MongoDBObject("state" -> "$state", "city" -> "$city"),
//                                                                 "pop" -> MongoDBObject("$sum" -> "$pop"))),
//                                     MongoDBObject("$sort" -> MongoDBObject("pop" -> 1)),
//                                     MongoDBObject("$group" ->
//                                                   MongoDBObject("_id" -> MongoDBObject("_id" -> "$_id.state"),
//                                                                 "biggestCity" ->   MongoDBObject("$last" ->  "$_id.city"),
//                                                                 "biggestPop" ->   MongoDBObject("$last" ->  "$pop"),
//                                                                 "smallestCity" ->  MongoDBObject("$first" ->  "$_id.city"),
//                                                                 "smallestPop" ->  MongoDBObject("$first" -> "$pop"))),
//                                     MongoDBObject("$project" ->
//                                                   MongoDBObject("_id" -> 0,
//                                                                 "state" -> "$_id",
//                                                                 "biggestCity" ->  MongoDBObject("name" -> "$biggestCity",  "pop" -> "$biggestPop"),
//                                                                 "smallestCity" -> MongoDBObject("name" -> "$smallestCity",
//                                                                                                 "pop" -> "$smallestPop"))))

//         aggregationResult("test", pipelines) match {
//           case Right(result) => {
//             result.size should equal(52)
//           }
//           case Left(ex) => fail(ex)
//         }

//       }
//     }
//     describe("aggregateを100個のデータに実行する") {
//       collection.find(MongoDBObject("lname" -> "Simpson")).length should equal(100)
//       val pipebuilder = MongoDBList.newBuilder
//       pipebuilder += MongoDBObject("$unwind" -> "$scores")
//       pipebuilder += MongoDBObject("$group" -> MongoDBObject("_id" -> "all", // constant, so we'll just create one bucket
//                                                              "tests" -> MongoDBObject("$addToSet" -> "$scores.tname"),
//                                                              "years" -> MongoDBObject("$addToSet" -> "$scores.tyear"),
//                                                              "grades" -> MongoDBObject("$addToSet" -> "$scores.grade")))
//       it("aggregateを実行する", DatabaseTest) {
//         val pipeline = pipebuilder.result()
//         val result = aggregationResult("test", pipeline)
//         result match {
//           case Right(the_result) => {
//             println(the_result.mkString)
//             the_result should not equal(Nil)
//           }
//           case Left(ex) => fail(ex.getMessage)
//         }
//       }
//     }
//   }
// }

