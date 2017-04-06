// import org.junit.runner.RunWith
// import org.scalatest.junit.JUnitRunner
// import org.scalatest.FunSuite
// import org.scalatest.matchers.ShouldMatchers
// import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

// /**
//  * OpenEHRライブラリに関する実験場
//  */
// import com.mongodb.casbah.Imports._
// import com.mongodb.{CommandResult}
// import com.mongodb.casbah.commons.conversions.scala._

// import scala.collection.JavaConversions._
// import scala.collection.JavaConverters._

// import org.scala_tools.time.Imports._

// import org.openehr.terminology.SimpleTerminologyService
// import org.openehr.rm.support.identification.{HierObjectID,UID, InternetID,ISO_OID,TerminologyID, ArchetypeID, PartyRef}
// import org.openehr.rm.support.terminology.TerminologyService
// import org.openehr.rm.support.measurement.SimpleMeasurementService
// import org.openehr.rm.support.measurement.MeasurementService
// import org.openehr.rm.RMObject
// import org.openehr.rm.datatypes.basic.{DvBoolean}
// import org.openehr.rm.datatypes.text.{CodePhrase,DvText, DvCodedText}
// import org.openehr.rm.datatypes.quantity.datetime.{DvDateTime,DvTime,DvDuration}
// import org.openehr.rm.datastructure.history.{PointEvent, History,Event}
// import org.openehr.rm.datastructure.itemstructure.{ItemSingle,ItemStructure}
// import org.openehr.rm.datastructure.itemstructure.representation.Element
// import org.openehr.rm.common.archetyped.Archetyped
// import org.openehr.rm.common.generic.{PartySelf,PartyProxy,PartyIdentified}
// import org.openehr.rm.composition.content.entry.Observation
// import org.openehr.rm.composition.content.ContentItem
// import org.openehr.rm.composition.{EventContext,Composition}
// import org.openehr.am.archetype.Archetype
// import se.acode.openehr.parser.ADLParser

// class OpenEHRSpec extends FunSpec with ShouldMatchers { // with BeforeAndAfterAll {
//   import org.openehr.build.{RMObjectBuilder,SystemValue}
  
//   val lang:CodePhrase = new CodePhrase("ISO_639-1", "en")
//   val charset:CodePhrase = new CodePhrase("IANA_character-sets","UTF-8")
//   val ts:TerminologyService = SimpleTerminologyService.getInstance()
//   val ms:MeasurementService = SimpleMeasurementService.getInstance()
  
//   def element():Element = {
// 	val name:DvText = new DvText("test element", lang, charset, ts)
// 	val value:DvText = new DvText("test value", lang, charset, ts)
// 	new Element("at0001", name, value)
//   }
//   def itemSingle(value:String = "test item single"):ItemSingle = {
// 	val name:DvText = new DvText(value, lang, charset, ts)
// 	new ItemSingle("at0001", name, element())
//   }
//   val values:Map[SystemValue, Object] = Map(SystemValue.LANGUAGE -> lang,
//                                             SystemValue.CHARSET ->  charset,
//                                             SystemValue.ENCODING -> charset,
//                                             SystemValue.TERMINOLOGY_SERVICE -> ts,
//                                             SystemValue.MEASUREMENT_SERVICE -> ms)
//   val builder = new RMObjectBuilder(values.asJava)
  
//   describe("OpenEHRライブラリの RMObjectBuilder について") {
//     it("DvBooleanを生成する"){
//       val rmtype:String = "DvBoolean"
//       val values:Map[String, Object] =  Map("value" -> "true")
//       val obj:RMObject = builder.construct(rmtype, values)
//       obj.isInstanceOf[DvBoolean] should equal(true)
//       val booleanObj:DvBoolean =  obj.asInstanceOf[DvBoolean]
//       booleanObj.getValue() should equal(true)
//     }
//     it("DvText"){
//       val rmtype:String  = "DvText"
//       val values:Map[String, Object] =  Map("value" -> "test text value")
//       val obj:RMObject = builder.construct(rmtype, values)
//       obj.isInstanceOf[DvText] should equal(true)
      
//     }
//     it("CodePhrase"){
//       val rmtype:String  = "CodePhrase"
//       val id:TerminologyID = new TerminologyID("openehr")
//       val obj:RMObject = builder.construct(rmtype, Map("terminologyId" -> id, "codeString" -> "1234"))
//       obj.isInstanceOf[CodePhrase] should equal(true)
      
//     }
//     it("DvCodedText"){
//       val rmtype:String  = "DvCodedText"
//       val definingCode:CodePhrase = new CodePhrase("local", "at0001")
//       val obj:RMObject = builder.construct(rmtype, Map("value" -> "test text value", "definingCode" -> definingCode))
//       obj.isInstanceOf[DvCodedText] should equal(true)
//     }
//     it("DvDateTime"){
//       val rmtype:String  = "DvDateTime"
//       val obj:RMObject = builder.construct(rmtype, Map("value" -> "1999-10-20T18:15:45"))
//       obj.isInstanceOf[DvDateTime] should equal(true)
//     }
//     it("DvTime"){
//       val rmtype:String  = "DvTime"
//       val obj:RMObject = builder.construct(rmtype, Map("value" -> "18:15:45"))
//       obj.isInstanceOf[DvTime] should equal(true)
//     }
//     it("History"){
//       val rmtype:String  = "History"
//       val archetypeNodeId = "at0001"
//       val name = new DvText("test history", lang, charset, ts)
//       val origin = new DvDateTime("2004-10-30T14:22:00")
//       val event = new PointEvent[ItemSingle](null, "at0003", new DvText("point event", lang, charset, ts), null, 
//                                              null, null, null, new DvDateTime("2004-10-31T08:00:00"), itemSingle("test item single"), null)
//       val events:java.util.List[Event[ItemSingle]] = List[PointEvent[ItemSingle]](event)
//       val values:Map[String, Object] =  Map("archetypeNodeId" -> archetypeNodeId,
//                                             "name" -> name,
//                                             "origin" -> origin,
//                                             "events" -> events)
//       val obj:RMObject = builder.construct(rmtype, values)
//       obj.isInstanceOf[History[_]] should equal(true)
//     }
//   }
//   describe("OpenEHRライブラリの Ehr について") {
// 	def text(value:String):DvText = new DvText(value, lang, charset, ts)
//     def pointEvent():Event[ItemStructure] = new PointEvent[ItemStructure](null, "at0003", text("point event"),  
//                                                                           null, null, null, null, new DvDateTime("2006-07-12T08:00:00"), 
//                                                                           itemSingle(), null)
//     def event():History[ItemStructure] = { 
//       val items:java.util.List[Event[ItemStructure]] = List[Event[ItemStructure]](pointEvent())
//       new History[ItemStructure](null, "at0002", text("history"),
//                                  null, null, null, null, new DvDateTime("2006-07-12T09:22:00"), 
//                                  items, DvDuration.getInstance("PT1h"), 
//                                  DvDuration.getInstance("PT3h"), null);
//     }
//     def subject():PartySelf = {
// 	  val party:PartyRef = new PartyRef(new HierObjectID("1.2.4.5.6.12.1"),"PARTY")
// 	  new PartySelf(party)
// 	}
//     def  provider():PartyIdentified = {
// 	  val performer:PartyRef = new PartyRef(new HierObjectID("1.3.3.1"),"ORGANISATION")
// 	  new PartyIdentified(performer, "provider's name", null)
// 	}
    
//     it("Observation"){
//       val name:DvText = new DvText("test observation", lang, charset, ts)
//       val node = "at0001"
//       val archetypeDetails = new Archetyped(new ArchetypeID("openehr-ehr_rm-observation.physical_examination.v3"), "v1.0")
//       val data:History[ItemStructure] = event()
//       val values:Map[String, Object] =  Map("archetypeNodeId" -> node,
//                                             "archetypeDetails" -> archetypeDetails,
//                                             "name" -> name,
//                                             "language" -> lang,
//                                             "encoding" -> charset,
//                                             "subject" -> subject(),
//                                             "provider" -> provider(),
//                                             "data" -> data)
//       val obj:RMObject = builder.construct("Observation", values)
//       obj.isInstanceOf[Observation] should equal(true)
//     }
    
//     it("Composition"){
//       val name:DvText = new DvText("test observation", lang, charset, ts)
//       def context():EventContext =  {
//     	val home:CodePhrase = new CodePhrase("openehr", "225")
//         val homeSetting:DvCodedText = new DvCodedText("home setting", lang, charset,home, ts);
//         new EventContext(null, new DvDateTime("2006-02-01T12:00:09"), null, null, null, homeSetting, null, ts)
//       }
//       val node = "at0001"
//       val archetypeDetails = new Archetyped(new ArchetypeID("openehr-ehr_rm-Composition.physical_exam.v2"), "1.0")
//       val EVENT:CodePhrase = new CodePhrase("openehr", "433")
//       val composer:PartyProxy = provider()
//       val category:DvCodedText = new DvCodedText("event", lang, charset, EVENT, ts)
//       val territory:CodePhrase = new CodePhrase("ISO_3166-1", "SE")
//       val data:History[ItemStructure] = event()

//       val observation:Observation = {
//         val archetypeDetails = new Archetyped(new ArchetypeID("openehr-ehr_rm-observation.physical_examination.v3"), "v1.0");
//         new Observation("at0001", name, archetypeDetails, lang,charset, subject(), provider(), event(), ts);
//       }
//       val content:java.util.List[Observation]  = List[Observation](observation)
      
//       val values:Map[String, Object] =  Map("archetypeNodeId" -> node,
//                                             "archetypeDetails" -> archetypeDetails,
//                                             "name" -> name,
//                                             "content" -> content,
//                                             "context" -> context(),
//                                             "composer" -> composer,
//                                             "category" -> category,
//                                             "territory" -> territory,
//                                             "language" -> lang)
//       val obj:RMObject = builder.construct("Composition", values)
//       obj.isInstanceOf[Composition] should equal(true)
//       val composition:Composition = obj.asInstanceOf[Composition]
//       composition.getArchetypeNodeId() should equal(node)
//     }
//   }
//   describe("OpenEHRライブラリの rm.support.identification.HierObjectID について") {
//     it("HierObjectIDを生成できる") {
//       val args = List("1.2.840.113554.1.2.2::345","1-2-840-113554-1::789","w123.com::123","1.2.840.113554.1.2.2","1-2-840-113554-1","w123.com")
//       args.foreach { arg =>
//         val hier_object_id = new HierObjectID(arg)
//         hier_object_id.isInstanceOf[HierObjectID] should equal(true)
//       }
//     }
//     it("HierObjectIDをInternetIDから生成できる") {
//       val hier_object_id = new HierObjectID("w123.com::123")
//       hier_object_id.root() should equal(new InternetID("w123.com"))
//       hier_object_id.extension() should equal("123")
//     }
//     it("HierObjectIDをISO_OIDから生成できる") {
//       val hier_object_id = new HierObjectID("1.2.840.113554.1.2.2::345")
//       hier_object_id.root() should equal(new ISO_OID("1.2.840.113554.1.2.2"))
//       hier_object_id.extension() should equal("345")
//     }
//   }  
//   // describe("tapコンビネータを用いて builder のかわりとする") {
//   //   object test {
//   //     implicit def kestrel[A](instance:A) = new {
//   //       def tap(sideEffect: A => Unit):A ={
//   //         import instance._
//   //         sideEffect(instance)
//   //         instance
//   //       }
//   //     }
//   //   }
//   //   it("tapコンビネータで DvBoolean を作る") {
//   //     import test._
//   //     val dv_boolean = new DvBoolean.tap(instance => {
        
//   //     })
//   //     val rmtype:String = "DvBoolean"
//   //     val values:Map[String, Object] =  Map("value" -> "true")
//   //     val obj:RMObject = builder.construct(rmtype, values)
//   //     obj.isInstanceOf[DvBoolean] should equal(true)
//   //     val booleanObj:DvBoolean =  obj.asInstanceOf[DvBoolean]
//   //     booleanObj.getValue() should equal(true)
//   //   }
//   // }
  

//   describe("OpenEHRライブラリの ADL parser について") {
//     import scalax.io.Resource
//     it("openEHR-EHR-OBSERVATION.lab_test-immunology-ANA.v1"){
//       val adl = Resource.fromFile("src/test/resources/adl/openEHR-EHR-OBSERVATION.lab_test-immunology-ANA.v1.adl").slurpString
//       val parser = new ADLParser(adl)
//       try {
//         val archetype:Archetype = parser.parse()
//         archetype.getAdlVersion() should equal("1.4")
//         archetype.getConcept() should equal("at0000.1.1")
//         archetype.getParentArchetypeId() should equal{
//           new ArchetypeID("openEHR-EHR-OBSERVATION.FIRST4_lab_test-immunology.v1")
//         }
//       } catch {
//         case ex:Exception => {
//           fail(ex)
//         }
//       }
//     }
//     it("openEHR-EHR-CLUSTER.inspection-tympanic_membrane.v1"){
//       val adl = Resource.fromFile("src/test/resources/adl/openEHR-EHR-CLUSTER.inspection-tympanic_membrane.v1.adl").slurpString
//       val parser = new ADLParser(adl)
//       try {
//         val archetype:Archetype = parser.parse()
//       } catch {
//         case ex:Exception => {
//           fail(ex)
//         }
//       }
//     }
//   }
//   describe("OpenEHRライブラリの TerminologyService ") {
//     import org.openehr.terminology.{SimpleTerminologyService,SimpleTerminologyAccess,SimpleCodeSetAccess}
//     import org.openehr.rm.support.terminology.{TerminologyService, TerminologyAccess,CodeSetAccess,OpenEHRCodeSetIdentifiers}
//     val simple_terminology_service:TerminologyService = SimpleTerminologyService.getInstance()    

//     it("TerminologyServiceでopenEHRの内部ターミノロジーにアクセスする") {
//       simple_terminology_service.openehrCodeSets().asScala should equal(Map("languages" -> "ISO_639-1",
//                                                                     "integrity check algorithms" -> "openehr_integrity_check_algorithms",
//                                                                     "character sets" -> "IANA_character-sets",
//                                                                     "compression algorithms" -> "openehr_compression_algorithms",
//                                                                     "countries" -> "ISO_3166-1",
//                                                                     "media types" -> "IANA_media-types",
//                                                                     "normal statuses" -> "openehr_normal_statuses"))
//     }
//     it("TerminologyAccessでopenEHRの内部ターミノロジーにアクセスする") {
//       val terminology_access:TerminologyAccess = simple_terminology_service.terminology(TerminologyService.OPENEHR)
//       terminology_access should not equal(null)
//       terminology_access.rubricForCode("240","en") should equal("signed")
//       terminology_access.codesForGroupName("attestation reason","en").asScala should equal(Set(new CodePhrase("openehr", "648"), new CodePhrase("openehr", "240")))
//       terminology_access.allCodes() should not equal(Nil)
      
//     }
//     it("TerminologySource") {
//       import org.openehr.terminology.{TerminologySourceFactory,TerminologySource}

//       val source:TerminologySource = TerminologySourceFactory.getXMLTerminologySource()
//       val groups = source.getConceptGroups()
//     }
//   }
// }

