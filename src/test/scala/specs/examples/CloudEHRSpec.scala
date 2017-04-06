// import org.junit.runner.RunWith
// import org.scalatest.junit.JUnitRunner
// import org.scalatest.FunSuite
// import org.scalatest.matchers.ShouldMatchers
// import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

// /**
//  * CloudEHRに関する実験場
//  */
// import com.mongodb.casbah.Imports._
// import com.mongodb.{CommandResult}
// import com.mongodb.casbah.commons.conversions.scala._
// import com.mongodb.util.{JSON}

// import scala.collection.JavaConversions._
// import org.scala_tools.time.Imports._

// class CloudEHRSpec extends FunSpec with ShouldMatchers with scala_labo.helpers {
//   import mongodb._
  
//   RegisterJodaTimeConversionHelpers()

//   implicit val db = MongoConnection()("scala-labo")
//   val ehrs = db("ehrs")
//   val vcompositions = db("vcompositions")
//   val entries = db("entries")
//   ehrs.drop()
//   vcompositions.drop()
//   entries.drop()

//   /*
//    *
//    *   +-------------+       +---------------+      +------------------+
//    *   |EHR          |       |VComposition   |      |Entry             |
//    *   +-------------+       +---------------+      +------------------|
//    *   |- ehr_id     <-------+- ehr_fk       <------+- vcomposition_fk |
//    *   |             |       |- archetype_id |      |- archetype_id    |
//    *   |             |       |               |      |- value           |
//    *   +-------------+       +---------------+      +------------------+
//    *
//    */

//   List(MongoDBObject("ehr_id" -> "0001"), MongoDBObject("ehr_id" -> "0002")).foreach {
//     data => {
//       ehrs.insert(data)
//     }}

//   describe("RMインスタンスを格納方法を探る"){
//     it("埋め込みドキュメント方式", DatabaseTest){
//       val content = """{"data" : {
//         "at0002": {
//           "rmTypeName": "org.openehr.rm.datastructure.history.PointEvent",
//           "name": "EVENT",
//           "archetypeNodeId": "at0002",
//           "data": {
//             "at0003": {
//               "items": [
//                 {
//                   "at0100": {
//                     "rmTypeName": "org.openehr.rm.datastructure.itemstructure.representation.Element",
//                     "name": "DV_TEXT",
//                     "value": {
//                       "rmTypeName": "org.openehr.rm.datatypes.text.DvText",
//                       "encoding": null,
//                       "value": "1件目"
//                     },
//                     "archetypeNodeId": "at0100",
//                   }
//                 },
//                 {
//                   "at0200": {
//                     "rmTypeName": "org.openehr.rm.datastructure.itemstructure.representation.Element",
//                     "name": "DV_CODED_TEXT",
//                     "value": {
//                       "rmTypeName": "org.openehr.rm.datatypes.text.DvCodedText",
//                       "encoding": null,
//                       "value": "コード01",
//                       "definingCode": {
//                         "terminologyId": "local",
//                         "rmTypeName": "org.openehr.rm.datatypes.text.CodePhrase",
//                         "codeString": "at0201"
//                       }
//                     },
//                     "archetypeNodeId": "at0200",
//                   }
//                 },
//                 {
//                   "at0300": {
//                     "rmTypeName": "org.openehr.rm.datastructure.itemstructure.representation.Element",
//                     "name": "C_DV_QUANTITY 制約なし",
//                     "value": {
//                       "rmTypeName": "org.openehr.rm.datatypes.quantity.DvQuantity",
//                       "precision": 0,
//                       "units": "",
//                       "magnitude": 167
//                     },
//                     "archetypeNodeId": "at0300",
//                   }
//                 }
//               ]
//             }
//           },
//           "time": {
//             "rmTypeName": "org.openehr.rm.datatypes.quantity.datetime.DvDateTime",
//             "value": "2011-08-30T10:30:15",
//             "value_msec": 1314667815000
//           },
//           "uid": null
//           }
//         }
//       }"""
//       loadJsonToMongoDB(entries)(content)
//       entries.find(MongoDBObject("data.at0002.data.at0003.items.at0300.value.magnitude" -> 167)).size should equal(1)
//       entries.find(MongoDBObject("data.at0002.time.value" -> "2011-08-30T10:30:15")).size should equal(1)
//       info("発見できない")
//       entries.find(MongoDBObject("data.at0002.data.at0003.items.at0300.value" -> MongoDBObject("magnitude" -> 167))).size should equal(0)
//     }
//     it("末端ノードを配列方式で格納する", DatabaseTest){
//       val contents = Seq("""{"data" : {
//         "at0002": {
//           "rmTypeName": "org.openehr.rm.datastructure.history.PointEvent",
//           "name": "EVENT",
//           "archetypeNodeId": "at0002",
//           "data": {
//             "at0003": {
//               "items": [
//                 {
//                   "at0100": {
//                     "rmTypeName": "org.openehr.rm.datastructure.itemstructure.representation.Element",
//                     "name": "DV_TEXT",
//                     "value": [{
//                       "rmTypeName": "org.openehr.rm.datatypes.text.DvText",
//                       "encoding": null,
//                       "value": "1件目"
//                     }],
//                     "archetypeNodeId": "at0100",
//                   }
//                 },
//                 {
//                   "at0200": {
//                     "rmTypeName": "org.openehr.rm.datastructure.itemstructure.representation.Element",
//                     "name": "DV_CODED_TEXT",
//                     "value": [{
//                       "rmTypeName": "org.openehr.rm.datatypes.text.DvCodedText",
//                       "encoding": null,
//                       "value": "コード01",
//                       "definingCode": {
//                         "terminologyId": "local",
//                         "rmTypeName": "org.openehr.rm.datatypes.text.CodePhrase",
//                         "codeString": "at0201"
//                       }
//                     }],
//                     "archetypeNodeId": "at0200",
//                   }
//                 },
//                 {
//                   "at0300": {
//                     "rmTypeName": "org.openehr.rm.datastructure.itemstructure.representation.Element",
//                     "name": "C_DV_QUANTITY 制約なし",
//                     "value": [{
//                       "rmTypeName": "org.openehr.rm.datatypes.quantity.DvQuantity",
//                       "precision": 0,
//                       "units": "cm",
//                       "magnitude": 167
//                     }],
//                     "archetypeNodeId": "at0300",
//                   }
//                 }
//               ]
//             }
//           },
//           "time": {
//             "rmTypeName": "org.openehr.rm.datatypes.quantity.datetime.DvDateTime",
//             "value": "2013-04-04T15:00:00",
//             "value_msec": 1365055200000
//           },
//           "uid": null
//           }
//         }
//       }""", /* 2番目は magnitude の値が1番目と異なる */
//       """{"data" : {
//         "at0002": {
//           "rmTypeName": "org.openehr.rm.datastructure.history.PointEvent",
//           "name": "EVENT",
//           "archetypeNodeId": "at0002",
//           "time": {
//             "rmTypeName": "org.openehr.rm.datatypes.quantity.datetime.DvDateTime",
//             "value": "2011-08-30T10:30:15",
//             "value_msec": 1314667815000
//           },
//           "uid": null,
//           "data": {
//             "at0003": {
//               "items": [
//                 {
//                   "at0100": {
//                     "rmTypeName": "org.openehr.rm.datastructure.itemstructure.representation.Element",
//                     "name": "DV_TEXT",
//                     "value": [{
//                       "rmTypeName": "org.openehr.rm.datatypes.text.DvText",
//                       "encoding": null,
//                       "value": "1件目"
//                     }],
//                     "archetypeNodeId": "at0100",
//                   }
//                 },
//                 {
//                   "at0200": {
//                     "rmTypeName": "org.openehr.rm.datastructure.itemstructure.representation.Element",
//                     "name": "DV_CODED_TEXT",
//                     "value": [{
//                       "rmTypeName": "org.openehr.rm.datatypes.text.DvCodedText",
//                       "encoding": null,
//                       "value": "コード01",
//                       "definingCode": {
//                         "terminologyId": "local",
//                         "rmTypeName": "org.openehr.rm.datatypes.text.CodePhrase",
//                         "codeString": "at0201"
//                       }
//                     }],
//                     "archetypeNodeId": "at0200",
//                   }
//                 },
//                 {
//                   "at0300": {
//                     "rmTypeName": "org.openehr.rm.datastructure.itemstructure.representation.Element",
//                     "name": "C_DV_QUANTITY 制約なし",
//                     "value": [{
//                       "rmTypeName": "org.openehr.rm.datatypes.quantity.DvQuantity",
//                       "precision": 0,
//                       "units": "cm",
//                       "magnitude": 177
//                     }],
//                     "archetypeNodeId": "at0300",
//                   }
//                 }
//               ]}
//             }
//           }
//         }
//       }""")
//       contents.foreach{content =>
//         loadJsonToMongoDB(entries)(content)
//         // loadJson(entries)(content)
//       }
//       entries.find(MongoDBObject("data.at0002.data.at0003.items.at0300.value.magnitude" -> 167)).size should equal(2)
//       entries.find(MongoDBObject("data.at0002.time.value" -> "2011-08-30T10:30:15")).size should equal(2)
//       info("配列で格納しても、通常の検索では発見できない")
//       entries.find(MongoDBObject("data.at0002.data.at0003.items.at0300.value" -> MongoDBObject("magnitude" -> 167))).size should equal{
//         0
//       }
//       info("elemMatchを利用すれば、発見できる")
//       entries.find(MongoDBObject("data.at0002.data.at0003.items.at0300.value" -> MongoDBObject("$elemMatch" -> MongoDBObject("magnitude" -> 167)))).size should equal(1)
//       entries.find(MongoDBObject("data.at0002.data.at0003.items.at0300.value" -> MongoDBObject("$elemMatch" -> MongoDBObject("magnitude" -> 167,"units" -> "cm")))).size should equal(1)
//       entries.find(MongoDBObject("data.at0002.data.at0003.items.at0300.value" -> MongoDBObject("$elemMatch" -> MongoDBObject("magnitude" -> MongoDBObject("$gt" -> 170))))).size should equal(1)
//       info("データ値と時間との組み合わせで検索する")
//       entries.find(MongoDBObject("data.at0002.data.at0003.items.at0300.value" -> MongoDBObject("$elemMatch" -> MongoDBObject("magnitude" -> MongoDBObject("$gt" -> 160))),
//                                  "data.at0002.time.value" -> "2011-08-30T10:30:15")).size should equal(1)

//       info("epoch秒は数値が大きすぎて処理できない")
//       intercept[java.lang.IllegalArgumentException]{
//         entries.find(MongoDBObject("data.at0002.data.at0003.items.at0300.value" -> MongoDBObject("$elemMatch" -> MongoDBObject("magnitude" -> MongoDBObject("$gt" -> 160))),
//                                    "data.at0002.time.value_msec" -> MongoDBObject("$gt" -> BigInt("1314667815000")))).size should equal(0)
//       }
//     }
//   }
    
//   describe("EHR,VComposition,Entryの簡単なモデルをもちいてクエリーの実験を試みる"){
//     describe("単一の患者を対象としたクエリーの実験を試みる"){
      
//       it("術前のデータを準備する", DatabaseTest){
//         // vcompositions.drop()
//         // entries.drop()

//         List(MongoDBObject("id" -> "0001a", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"),
//              MongoDBObject("id" -> "0001b", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1"),
//              MongoDBObject("id" -> "0002a", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"),
//              MongoDBObject("id" -> "0002b", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1")).foreach {
//                data => {
//                  vcompositions.insert(data)
//                }}
//         List(MongoDBObject("vcomposition_fk" -> "0001a", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 100),
//              MongoDBObject("vcomposition_fk" -> "0001b", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 140),
//              MongoDBObject("vcomposition_fk" -> "0002a", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 50),
//              MongoDBObject("vcomposition_fk" -> "0002b", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 80),
//              MongoDBObject("vcomposition_fk" -> "0001a", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 70),
//              MongoDBObject("vcomposition_fk" -> "0001b", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 60),
//              MongoDBObject("vcomposition_fk" -> "0002a", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 60),
//              MongoDBObject("vcomposition_fk" -> "0002b", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 55)).foreach {
//                data => {
//                  entries.insert(data)
//                }}
//       }
//       it("術前のデータを検索する", DatabaseTest){
//         // ehr_id 0001 の患者の術前 presurgery_exam のデータ(血圧,体重) を検索する
//         val ehr_id = "0001"
//         val result = for {
//           // 患者の制約
//           ehr <- ehrs.find(MongoDBObject("ehr_id" -> ehr_id))
//           // 術前の診察データ
//           presurgery <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1",
//                                                          "ehr_fk" -> ehr_id))
//           entry <- entries.find(MongoDBObject("vcomposition_fk" -> presurgery.getAs[String]("id")))
//         } yield {
//           // println("entry = " + entry)
//           presurgery.getAs[String]("ehr_fk") should equal(Some(ehr_id))
//           entry
//         }
//         result.size should equal(12)
//       }
//     }
//     describe("複数の患者を対象とした横断的クエリーの実験を試みる"){
//       // vcompositions.drop()
//       // entries.drop()

//       List(MongoDBObject("id" -> "0001a", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"),
//            MongoDBObject("id" -> "0001b", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1"),
//            MongoDBObject("id" -> "0002a", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"),
//            MongoDBObject("id" -> "0002b", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1")).foreach {
//              data => {
//                vcompositions.insert(data)
//              }}
//       List(MongoDBObject("vcomposition_fk" -> "0001a", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 100),
//            MongoDBObject("vcomposition_fk" -> "0001b", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 140),
//            MongoDBObject("vcomposition_fk" -> "0002a", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 50),
//            MongoDBObject("vcomposition_fk" -> "0002b", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 80),
//            MongoDBObject("vcomposition_fk" -> "0001a", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 70),
//            MongoDBObject("vcomposition_fk" -> "0001b", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 60),
//            MongoDBObject("vcomposition_fk" -> "0002a", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 60),
//            MongoDBObject("vcomposition_fk" -> "0002b", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 55)).foreach {
//              data => {
//                entries.insert(data)
//              }}

//       describe("ひとつのアーキタイプについて、ひとつの項目に合致する症例を横断的に検索する"){
//         it("術前の血圧データを複数の患者にまたがって横断的に検索する", DatabaseTest){
//           // vcompositions.drop()
//           // entries.drop()

//           List(MongoDBObject("id" -> "0001a", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"),
//                MongoDBObject("id" -> "0001b", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1"),
//                MongoDBObject("id" -> "0002a", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"),
//                MongoDBObject("id" -> "0002b", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1")).foreach {
//                  data => {
//                    vcompositions.insert(data)
//                  }}
//           List(MongoDBObject("vcomposition_fk" -> "0001a", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 100),
//                MongoDBObject("vcomposition_fk" -> "0001b", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 140),
//                MongoDBObject("vcomposition_fk" -> "0002a", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 50),
//                MongoDBObject("vcomposition_fk" -> "0002b", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 80)).foreach {
//                  data => {
//                    entries.insert(data)
//                  }}
//           val result = for {
//             vcomposition <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"))
//             entry <- entries.find(MongoDBObject("archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1",
//                                                 "vcomposition_fk" -> vcomposition.getAs[String]("id").get))
//           } yield {
//             val value = entry.getAs[Int]("value").get
//             value
//           }
//           result.length should equal(18)
//         }
//       }
//       describe("複数のアーキタイプにまたがったひとつの項目を指定した検索を実行する"){
//         it("術前から術後にかけて血圧が40以上上昇した症例を横断的に検索する", DatabaseTest){
//           val result = for{
//             // 術前の血圧
//             presurgery <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"))
//             presurgery_bp <- entries.find(MongoDBObject("archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1",
//                                                         "vcomposition_fk" -> presurgery.getAs[String]("id").get))
//             // 術後の血圧
//             postsurgery <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1"))
//             postsurgery_bp <- entries.find(MongoDBObject("archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1",
//                                                          "vcomposition_fk" -> postsurgery.getAs[String]("id").get))
//             // 患者制約
//             if presurgery.getAs[String]("ehr_fk").get == postsurgery.getAs[String]("ehr_fk").get
//             // 条件判定
//             if postsurgery_bp.getAs[Int]("value").get >= (40 + presurgery_bp.getAs[Int]("value").get)
//           } {
//             val id = postsurgery_bp.getAs[String]("vcomposition_fk").get
//             id should equal("0001b")
//           }
//         }
//       }

//       describe("ひとつのアーキタイプ中で複数の項目を指定した検索を実行する"){
//         it("術前のデータにおいて、blood_pressure が90以上かつ body_weightが60以上の症例を検索する", DatabaseTest){
//           val query_on_vcomposition = MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1")
//           val query_on_entry_for_blood_pressure = MongoDBObject("archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> MongoDBObject("$gte" -> 90))
//           val query_on_entry_for_body_weight = MongoDBObject("archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> MongoDBObject("$gte" -> 60))
//           for{
//             vcomposition <- vcompositions.find(query_on_vcomposition)
//             blood_pressure <- entries.find(query_on_entry_for_blood_pressure) if vcomposition.getAs[String]("id").get == blood_pressure.getAs[String]("vcomposition_fk").get
//             body_weight <- entries.find(query_on_entry_for_body_weight) if body_weight.getAs[String]("vcomposition_fk").get == blood_pressure.getAs[String]("vcomposition_fk").get
//             answer <- vcompositions.find(MongoDBObject("id" -> body_weight.getAs[String]("vcomposition_fk").get))
//           } {
//             answer.getAs[String]("id").get should equal("0001a")
//           }
//         }
//       }
//       describe("複数のアーキタイプにまたがった複数の項目を指定した検索を実行する"){
//         it("術前データの blood_pressure が90以上、かつ術後データのbody_weightが60以上の症例を検索する", DatabaseTest){
//         // vcompositions.drop()
//         // entries.drop()

//         List(MongoDBObject("id" -> "0001a", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"),
//              MongoDBObject("id" -> "0001b", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1"),
//              MongoDBObject("id" -> "0002a", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"),
//              MongoDBObject("id" -> "0002b", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1")).foreach {
//                data => {
//                  vcompositions.insert(data)
//                }}
//         List(MongoDBObject("vcomposition_fk" -> "0001a", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 100),
//              MongoDBObject("vcomposition_fk" -> "0001b", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 140),
//              MongoDBObject("vcomposition_fk" -> "0002a", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 50),
//              MongoDBObject("vcomposition_fk" -> "0002b", "archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1", "value" -> 80),
//              MongoDBObject("vcomposition_fk" -> "0001a", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 70),
//              MongoDBObject("vcomposition_fk" -> "0001b", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 60),
//              MongoDBObject("vcomposition_fk" -> "0002a", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 60),
//              MongoDBObject("vcomposition_fk" -> "0002b", "archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1", "value" -> 55)).foreach {
//                data => {
//                  entries.insert(data)
//                }}

//           val result = for{
//             // 術前の血圧
//             presurgery <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.presurgery_exam.v1"))
//             presurgery_pressure <- entries.find(MongoDBObject("archetype_id" -> "openEHR-EHR-OBSERVATION.blood_pressure.v1",
//                                                         "vcomposition_fk" -> presurgery.getAs[String]("id").get))
//             // 術後の体重
//             postsurgery <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.postsurgery_exam.v1"))
//             postsurgery_weight <- entries.find(MongoDBObject("archetype_id" -> "openEHR-EHR-OBSERVATION.body_weight.v1",
//                                                              "vcomposition_fk" -> postsurgery.getAs[String]("id").get))
//             // 患者制約
//             if presurgery.getAs[String]("ehr_fk").get == postsurgery.getAs[String]("ehr_fk").get
//             // 条件判定
//             if presurgery_pressure.getAs[Int]("value").get >= 90
//             if postsurgery_weight.getAs[Int]("value").get >= 60
//           } yield {
//             (presurgery_pressure, postsurgery_weight)
//           }
//           result.foreach {the_result =>
//             val (presurgery_pressure, postsurgery_weight) = the_result
//             presurgery_pressure.getAs[Int]("value").get should be >= (90)
//             postsurgery_weight.getAs[Int]("value").get should be >= (60)
//           }
//         }
//       }
//     }
//   }
//   describe("日付を利用したクエリーの実験を試みる"){
//     // vcompositions.drop()
//     // entries.drop()

//     List(MongoDBObject("id" -> "0001a", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.surgery.v1"),
//          MongoDBObject("id" -> "0001b", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.report.v1"),
//          MongoDBObject("id" -> "0002a", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.surgery.v1"),
//          MongoDBObject("id" -> "0002b", "ehr_fk" -> "0002", "archetype_id" -> "openEHR-EHR-COMPOSITION.report.v1")
//        ).foreach {
//          data => {
//            vcompositions.insert(data)
//          }}
//   val now = DateTime.now
//   List(MongoDBObject("vcomposition_fk" -> "0001a", "archetype_id" -> "openEHR-EHR-ACTION.surgical_procedure.v1", "occurred_at" -> now),
//        MongoDBObject("vcomposition_fk" -> "0001b", "archetype_id" -> "openEHR-EHR-OBSERVATION.ecg.v1", "occurred_at" -> (now - 2.hours)),
//        MongoDBObject("vcomposition_fk" -> "0002a", "archetype_id" -> "openEHR-EHR-ACTION.surgical_procedure.v1", "occurred_at" -> (now + 1.day)),
//        MongoDBObject("vcomposition_fk" -> "0002b", "archetype_id" -> "openEHR-EHR-OBSERVATION.ecg.v1", "occurred_at" -> (now - 30.days)),
//        MongoDBObject("vcomposition_fk" -> "0001a", "archetype_id" -> "openEHR-EHR-ACTION.surgical_procedure.v1", "occurred_at" -> now),
//        MongoDBObject("vcomposition_fk" -> "0001b", "archetype_id" -> "openEHR-EHR-OBSERVATION.ecg.v1", "occurred_at" -> (now - 2.hours)),
//        MongoDBObject("vcomposition_fk" -> "0002a", "archetype_id" -> "openEHR-EHR-ACTION.surgical_procedure.v1", "occurred_at" -> (now + 1.day)),
//        MongoDBObject("vcomposition_fk" -> "0002b", "archetype_id" -> "openEHR-EHR-OBSERVATION.ecg.v1", "occurred_at" -> (now - 30.days))).foreach {
//          data => {
//            entries.insert(data)
//          }}
//     it("術前に1日以内に心電図を検査しなかった症例を検索する", DatabaseTest){
//       val result = for {
//         // 手術の日程
//         surgery <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.surgery.v1"))
//         surgery_date <- entries.find(MongoDBObject("archetype_id" -> "openEHR-EHR-ACTION.surgical_procedure.v1",
//                                                    "vcomposition_fk" -> surgery.getAs[String]("id").get))
//         // 心電図検査
//         ecg <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.report.v1"))
//         ecg_date <- entries.find(MongoDBObject("archetype_id" -> "openEHR-EHR-OBSERVATION.ecg.v1",
//                                                "vcomposition_fk" -> ecg.getAs[String]("id").get))
//         // 患者制約
//         if surgery.getAs[String]("ehr_fk").get == ecg.getAs[String]("ehr_fk").get
//         // 条件判定
//         if surgery_date.getAs[DateTime]("occurred_at").get > (ecg_date.getAs[DateTime]("occurred_at").get + 24.hours)
//       } {
//         ecg_date.getAs[String]("vcomposition_fk") == Some("0002b")
//       }
//     }
//     it("テルフェナジン terfenadine とイトラコナゾール Itraconazole の処方期間がオーバーラップしている症例を検索する", DatabaseTest){
//       /*                    1      2   3  4 5   6
//        *  テルフェナジン:       |----------|---|
//        *  イトラコナゾール:             |-----|-----|
//        *                           ^^    ^^
//        */
//       // vcompositions.drop()
//       // entries.drop()
      
//       List(MongoDBObject("id" -> "0001-a1", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-a2", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-a3", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-b1", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-b2", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-b3", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1")
//          ).foreach {
//            data => {
//              vcompositions.insert(data)
//            }}
//       val a1_time = new DateTime(2012, 1, 1, 12, 0, 0, 0)
//       val a2_time = a1_time + 7.days
//       val a3_time = a1_time + 10.days
//       val b1_time = a1_time + 5.days
//       val b2_time = a1_time + 8.days
//       val b3_time = a1_time + 12.days
//       List(MongoDBObject("vcomposition_fk" -> "0001-a1", "archetype_id" -> "openEHR-EHR-ACTION.medication.v1",
//                          "name" -> "terfenadine", "occurred_at" -> a1_time),
//            MongoDBObject("vcomposition_fk" -> "0001-a2", "archetype_id" -> "openEHR-EHR-ACTION.medication.v1",
//                          "name" -> "terfenadine", "occurred_at" -> a2_time),           
//            MongoDBObject("vcomposition_fk" -> "0001-a2", "archetype_id" -> "openEHR-EHR-ACTION.medication.v1",
//                          "name" -> "terfenadine", "occurred_at" -> a3_time),
//            MongoDBObject("vcomposition_fk" -> "0001-b1", "archetype_id" -> "openEHR-EHR-ACTION.medication.v1",
//                          "name" -> "itraconazole", "occurred_at" -> b1_time),
//            MongoDBObject("vcomposition_fk" -> "0001-b2", "archetype_id" -> "openEHR-EHR-ACTION.medication.v1",
//                          "name" -> "itraconazole", "occurred_at" -> b2_time),
//            MongoDBObject("vcomposition_fk" -> "0001-b3", "archetype_id" -> "openEHR-EHR-ACTION.medication.v1",
//                          "name" -> "itraconazole", "occurred_at" -> b3_time)).foreach {
//                            data => {
//                              entries.insert(data)
//                            }}
      
//       val terfenadines = for{
//         prescription <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"))
//         terfenadine <- entries.find(MongoDBObject("archetype_id" -> "openEHR-EHR-ACTION.medication.v1","name" -> "terfenadine",
//                                                   "vcomposition_fk" -> prescription.getAs[String]("id").get))
//       } yield {
//         terfenadine
//       }
//       // テルフェナジンを処方日時順にソートする
//       val sorted_terfenadines = terfenadines.toList.map{_.getAs[DateTime]("occurred_at").get}.sortWith{(a,b) => a < b }
//       // println(sorted_terfenadines.zip(sorted_terfenadines drop(1)))
//       val itraconazoles  = for{
//         (before,after) <- sorted_terfenadines.zip(sorted_terfenadines drop(1)) // テルフェナジンの処方時期について前後のペアを生成する。
//         // イトラコナゾールについての制約条件は、イトラコナゾールが処方された時期の前後に テルフェナジンが処方されていることである。
//         prescription <- vcompositions.find(MongoDBObject("archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"))
//         itraconazole <- entries.find(MongoDBObject("archetype_id" -> "openEHR-EHR-ACTION.medication.v1","name" -> "itraconazole",
//                                                    "occurred_at" -> MongoDBObject("$gte" -> before, "$lte" -> after),
//                                                    "vcomposition_fk" -> prescription.getAs[String]("id").get))
//       } yield {
//         // println(itraconazole)
//         itraconazole
//       }
//       itraconazoles.length should equal(2)
      
//     }
//     it("テルフェナジンとイトラコナゾールが併用された時期以降より、QTcが延長した症例を検索する", DatabaseTest){
//       /*
//        *  テルフェナジン:       |----|---------|---|
//        *  イトラコナゾール:                |-----|-----|---|
//        *  QTc:           |----------|---------|-----|
//        *
//        */
//     }

//   }
//   describe("new aggregation framework をもちいてクエリーの実験を試みる"){
//     // import scala_labo.helpers.mongodb._
//     import scala.collection.JavaConversions._
//     com.mongodb.casbah.commons.conversions.scala.RegisterConversionHelpers()

//     it("new aggregation framework をもちいて、テルフェナジン terfenadine とイトラコナゾール Itraconazole の処方期間がオーバーラップしている症例を検索する", DatabaseTest){
//       // vcompositions.drop()
//       // entries.drop()
      
//       List(MongoDBObject("id" -> "0001-a1", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-a2", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-a3", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-b1", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-b2", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1"),
//            MongoDBObject("id" -> "0001-b3", "ehr_fk" -> "0001", "archetype_id" -> "openEHR-EHR-COMPOSITION.prescription.v1")
//          ).foreach {
//            data => {
//              vcompositions.insert(data)
//            }}
//       val a1_time = "2012-01-01T03:00:00Z"
//       val a2_time = "2012-01-08T03:00:00Z"
//       val a3_time = "2012-01-11T03:00:00Z" 
//       val b1_time = "2012-01-06T03:00:00Z"
//       val b2_time = "2012-01-09T03:00:00Z"
//       val b3_time = "2012-01-13T03:00:00Z"
//       List(MongoDBObject("vcomposition_fk" -> "0001-a1",
//                          "archetype_id" -> "openEHR-EHR-ACTION.medication.v1",
//                          "name" -> "terfenadine",
//                          "history" -> MongoDBList(MongoDBObject("occurred_at" -> a1_time),
//                                                   MongoDBObject("occurred_at" -> a3_time),
//                                                   MongoDBObject("occurred_at" -> a2_time)),
//                          "ehr_fk" -> "0001"),
//            MongoDBObject("vcomposition_fk" -> "0001-b1",
//                          "archetype_id" -> "openEHR-EHR-ACTION.medication.v1",
//                          "name" -> "itraconazole",
//                          "history" -> MongoDBList(MongoDBObject("occurred_at" -> b1_time),
//                                                   MongoDBObject("occurred_at" -> b2_time),
//                                                   MongoDBObject("occurred_at" -> b3_time)),
//                          "ehr_fk" -> "0001")).foreach {data => {
//                              entries.insert(data)
//                            }}

//       val pipelines = MongoDBList(MongoDBObject("$unwind" -> "$history"),
//                                   MongoDBObject("$sort" -> MongoDBObject("history.occurred_at" -> -1)),
//                                   MongoDBObject("$group" -> MongoDBObject("_id" -> MongoDBObject("vcomposition_fk" -> "$vcomposition_fk",
//                                                                                                  "archetype_id" -> "$archetype_id",
//                                                                                                  "name" -> "$name"),
//                                                                           "occurred" -> MongoDBObject("$addToSet" -> "$history.occurred_at"))),
//                                   MongoDBObject("$match" -> MongoDBObject("_id.name" -> "terfenadine")),
//                                   MongoDBObject("$project" ->
//                                                  MongoDBObject("name" -> "$_id.name","occurred" -> "$occurred")))

//       aggregationResult("entries", pipelines) match {
//         case Right(result:MongoDBList) => {
//           println("result= " + result.mkString)
//           result.length should equal(1)
//           result(0).asInstanceOf[BasicDBObject].getAs[String]("name").get should equal("terfenadine")
//           result(0).asInstanceOf[BasicDBObject].getAs[MongoDBList]("occurred").get should equal(MongoDBList(a1_time,a2_time,a3_time))
//         }
//         case Left(ex) => fail(ex)
//       }
//     }
    
//     describe("ノードパスをもちいたクエリーの実験を試みる"){
//       loadJson(entries,"entries.json")
//       loadJson(vcompositions,"compositions.json")
      
//       val composition_archetype_id = "openEHR-EHR-COMPOSITION.problem_list.v1"
//       val entry_archetype_id = "openEHR-EHR-OBSERVATION.lab_test.v1"
//       it("ノードパスでvalueを検索する", DatabaseTest){
//         {
//           // db.entries.find({"entry.data.at0023.events.at0003.data.at0008.item.at0007.value.value": {$exists : true}},{"entry.data.at0023.events.at0003.data.at0008.item.at0007.value.value" : 1})
//           val query = "entry.data.at0023.events.at0003.data.at0008.item.at0007.value.value" $exists true
//           val projection = MongoDBObject("entry.data.at0023.events.at0003.data.at0008.item.at0007.value.value" -> true)
//           val results = for (result <- entries.find(query, projection)) yield {
//             println(result)
//             result
//           }
//           results.size should equal(1)
//         }
//       }
//       it("ノードパスで datetime を検索する", DatabaseTest){
//         {
//           // db.entries.find({"entry.data.at0023.events.at0003.data.at0008.item.at0007.value.value": {$exists : true}},{"entry.data.at0023.events.at0003.data.at0008.item.at0007.value.value" : 1})
//           val query = MongoDBObject("entry.data.at0023.events.at0003.time.value" -> MongoDBObject("$gte" -> new DateTime(2012, 1, 1, 12, 0, 0, 0)))
//           val projection = MongoDBObject("entry.data.at0023.events.at0003.time.value" -> true)
//           val results = for (result <- entries.find(query, projection)) yield {
//             println(result)
//             result
//           }
//           results.size should equal(1)
//         }
//       }
//     }
//     describe("属性内部を配列としたデータにおいて、ノードパスをもちいたクエリーの実験を試みる"){
//       it("ノードパスで検索する", DatabaseTest){
//         loadJsonToMongoDB(entries)("""{"entry": {
//           "archetypeID": "openEHR-EHR-OBSERVATION.test.v1",
//             "archetypeNodeId": "at0000",
//             "rmTypeName": "Observation",
//             "protocol": [
//                 {"at0001": {
//                    "archetypeNodeId": "at0001",
//                    "rmTypeName": "ItemSingle",
//                    "item": {
//                        "at0007": {
//                            "archetypeNodeId": "at0007",
//                            "rmTypeName": "Element",
//                            "value": {
//                                "rmTypeName": "DvQuantity",
//                                "value": 3
//                            }
//                        }
//                    }
//                 }}
//             ]
//         }}""")
//         val node_path = "entry.protocol.at0001.item.at0007.value.value"
//         val query = MongoDBObject(node_path -> 3)
//         val projection = MongoDBObject(node_path -> true)
//         val results = for (result <- entries.find(query, projection)) yield {
//           println(result)
//           result
//         }
//         results.size should equal(1)
//       }
//       it("同レベルに複数の同一ノードIDを持つ場合にもノードパスを指定して検索できる", DatabaseTest){
//         loadJsonToMongoDB(entries)("""{"entry": {
//           "archetypeID": "openEHR-EHR-OBSERVATION.test.v1",
//             "archetypeNodeId": "at0000",
//             "rmTypeName": "Observation",
//             "protocol": [
//                 {"at0001": {
//                    "archetypeNodeId": "at0001",
//                    "rmTypeName": "ItemSingle",
//                    "item": {
//                        "at0007": {
//                            "archetypeNodeId": "at0007",
//                            "rmTypeName": "Element",
//                            "value": {
//                                "rmTypeName": "DvQuantity",
//                                "value": 4
//                            }
//                        }
//                    }
//                 }},
//                 {"at0001": {
//                    "archetypeNodeId": "at0001",
//                    "rmTypeName": "ItemSingle",
//                    "item": {
//                        "at0007": {
//                            "archetypeNodeId": "at0007",
//                            "rmTypeName": "Element",
//                            "value": {
//                                "rmTypeName": "DvQuantity",
//                                "value": 5
//                            }
//                        }
//                    }
//                 }}
//             ]
//         }}""")
//         val node_path = "entry.protocol.at0001.item.at0007.value.value"
//         val query = MongoDBObject(node_path -> 4)
//         val projection = MongoDBObject(node_path -> true)
//         val results = for (result <- entries.find(query, projection)) yield {
//           println(result)
//           result
//         }
//         results.size should equal(1)
//       }
//       it("elemMatchを利用して同レベルに複数の同一ノードIDを持つ場合にも検索できる", DatabaseTest){
//         loadJsonToMongoDB(entries)("""{"entry": {
//           "archetypeID": "openEHR-EHR-OBSERVATION.test.v1",
//             "archetypeNodeId": "at0000",
//             "rmTypeName": "Observation",
//             "protocol": [
//                 {"at0001": {
//                    "archetypeNodeId": "at0001",
//                    "rmTypeName": "ItemSingle",
//                    "items": [
//                        {"at0017": {
//                            "archetypeNodeId": "at0017.01",
//                            "rmTypeName": "Element",
//                            "value": {
//                                "rmTypeName": "DvQuantity",
//                                "value": 1
//                            }
//                        }},
//                        {"at0017": {
//                            "archetypeNodeId": "at0017.02",
//                            "rmTypeName": "Element",
//                            "value": {
//                                "rmTypeName": "DvQuantity",
//                                "value": 2
//                            }
//                        }}
//                    ]
//                 }}
//             ]
//         }}""")
//         loadJsonToMongoDB(entries)("""{"entry": {
//           "archetypeID": "openEHR-EHR-OBSERVATION.test.v1",
//             "archetypeNodeId": "at0000",
//             "rmTypeName": "Observation",
//             "protocol": [
//                 {"at0001": {
//                    "archetypeNodeId": "at0001",
//                    "rmTypeName": "ItemSingle",
//                    "items": [
//                        {"at0017": {
//                            "archetypeNodeId": "at0017.01",
//                            "rmTypeName": "Element",
//                            "value": {
//                                "rmTypeName": "DvQuantity",
//                                "value": 2
//                            }
//                        }},
//                        {"at0017": {
//                            "archetypeNodeId": "at0017.02",
//                            "rmTypeName": "Element",
//                            "value": {
//                                "rmTypeName": "DvQuantity",
//                                "value": 3
//                            }
//                        }}
//                    ]
//                 }}
//             ]
//         }}""")
//         val node_path = "entry.protocol.at0001.items.at0017.value.value"
//         val query = MongoDBObject("entry.protocol.at0001.items.at0017.archetypeNodeId" -> "at0017.02",
//                                   "entry.protocol.at0001.items.at0017.value.value" -> 2)
//         val results = for (result <- entries.find(query)) yield {
//           println(result)
//           result
//         }
//         results.size should equal(2) // 2
//         entries.find(MongoDBObject("entry.protocol.at0001.items" -> MongoDBObject("$elemMatch" -> MongoDBObject("at0017.archetypeNodeId" -> "at0017.02",
//                                                                                                                 "at0017.value.value" -> 2)))).length should equal(1)
//       }
      
//       it("特殊化時にノードパスを指定して検索できる", DatabaseTest){
//         loadJsonToMongoDB(entries)("""{"entry": {
//           "archetypeID": "openEHR-EHR-OBSERVATION.test.v1",
//             "archetypeNodeId": "at0000",
//             "rmTypeName": "Observation",
//             "protocol": [
//                 {"at0001": {
//                    "archetypeNodeId": "at0001.1",
//                    "rmTypeName": "ItemSingle",
//                    "item": {
//                        "at0007": {
//                            "archetypeNodeId": "at0007",
//                            "rmTypeName": "Element",
//                            "value": {
//                                "rmTypeName": "DvQuantity",
//                                "value": 5
//                            }
//                        }
//                    }
//                 }},
//                 {"at0001": {
//                    "archetypeNodeId": "at0001.2",
//                    "rmTypeName": "ItemSingle",
//                    "item": {
//                        "at0007": {
//                            "archetypeNodeId": "at0007",
//                            "rmTypeName": "Element",
//                            "value": {
//                                "rmTypeName": "DvQuantity",
//                                "value": 6
//                            }
//                        }
//                    }
//                 }}
//             ]
//         }}""")
//         val node_path = "entry.protocol.at0001.item.at0007.value.value"
//         info("前のテストで保存されたデータもクエリーに合致するので、結果は2となる")
//         entries.find(MongoDBObject(node_path -> 5), MongoDBObject(node_path -> true)).size should equal(2)
//         info("特殊化アーキタイプの合致するデータのみを検索対象とする場合は、 archetypeNodeId でそのノードIDを指定する")
//         entries.find(MongoDBObject(node_path -> 5, "entry.protocol.at0001.archetypeNodeId" -> "at0001.1"), MongoDBObject(node_path -> true)).size should equal(1)
//       }
//     }
//   }
// }

