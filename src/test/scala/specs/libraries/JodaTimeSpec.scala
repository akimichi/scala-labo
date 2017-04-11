import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


// import org.scala_tools.time.Imports._


/**
 * JodaTimeライブラリ の基本的な用法を示す
 * c.f. http://www.mwsoft.jp/programming/java/joda_time_sample.html
 * 
 */
class JodaTimeSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("JodaTimeを用いて") {
    import org.joda.time.{DateTime,DateTimeZone}
    
    it("DateTimeを求める") {
      val date_time:org.joda.time.DateTime = new DateTime("2009-04-27T18:45:10.313+09:00") // "2009-04-27T02:45:10.313-07:00")
      date_time.toString() should equal{
        "2009-04-27T18:45:10.313+09:00"
      }
      val dt:DateTime = new DateTime(2011, 10, 23, 3, 50, 0)
      
      // タイムゾーンを指定して初期化
      // val dt_zone:DateTime = new DateTime(DateTimeZone.forID("Asia/Tokyo"))
    }
    describe("タイムゾーン"){
      it("タイムゾーンを設定する"){
        val defaultZone:DateTimeZone = DateTimeZone.getDefault()
        defaultZone.toString should equal{
          "Asia/Tokyo"
        }
        val tokyoZone = DateTimeZone.forID("Asia/Tokyo")
        DateTimeZone.setDefault(tokyoZone)
      }
      it("タイムゾーンをローカルからUTCに変換"){
        val date_time:org.joda.time.DateTime =  new DateTime("2013-04-10T03:00:00").withZone(DateTimeZone.forID("UTC"))
        date_time.toString() should equal("2013-04-09T18:00:00.000Z")
      }
    }
    describe("epochを操作する"){
      val date_time:org.joda.time.DateTime = new DateTime("2009-04-27T18:45:10.313+09:00") // "2009-04-27T02:45:10.313-07:00")
      it("epochに変換する"){
        date_time.getMillis.toString should equal("1240825510313")
        new DateTime("2013-04-10T03:00:00.000Z").getMillis.toString should equal("1365562800000")
        new DateTime("2000-04-10T03:00:00.000Z").getMillis.toString should equal("955335600000")
      }
      it("epochを比較する"){
        val date_time_one_day_later = new DateTime("2009-04-28T18:45:10.313+09:00")
        (date_time_one_day_later.getMillis.toString > date_time.getMillis.toString) should equal(true)
      }
      
    }
    // it("ISODateTimeFormatを使ってISO8601に沿った出力をする"){
    //   import org.joda.time.format.ISODateTimeFormat
    //   val date_time:org.joda.time.DateTime = new DateTime("2009-04-27T18:45:10.313+09:00")
    //   date_time.toString(ISODateTimeFormat.basicDateTimeNoMillis()) should equal{
    //     "20090427T184510+0900"
    //   }
    // }
  }

}
