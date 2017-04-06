import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


// import org.scala_tools.time.Imports._


/**
 * nscala-timeライブラリ の基本的な用法を示す
 * c.f. https://github.com/nscala-time/nscala-time
 * nscala-timeは joda-time のscalaラッパーである
 */
class NsScalaTimeSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("NsScalaTimeを用いて") {
    import com.github.nscala_time.time.Imports._
    
    it("DateTimeを求める") {
      val now:org.joda.time.DateTime = DateTime.now // returns org.joda.time.DateTime = 2009-04-27T13:25:42.659-07:00
      assert(DateTime.nextMonth < now + 2.months)
      // DateTime.now.hour(2).minute(45).second(10) // returns org.joda.time.DateTime = 2009-04-27T02:45:10.313-07:00
      // DateTime.now + 2.months // returns org.joda.time.DateTime = 2009-06-27T13:25:59.195-07:00
    }

    /*
    it("parse"){
      import org.joda.time.DateTime
      DateTime.parse("2013-03-02T07:08:09.123+0900") should equal {
        new DateTime("2013-03-02T07:08:09.123+0900")
      }
      // "2012-08-08".toDateTime === new DateTime("2012-08-08")
    }
    */ 
    
    it("Interval"){
      (2.hours + 45.minutes + 10.seconds).millis should equal{
        9910000
      }
      /*
      DateTime.nextMonth < DateTime.now + 2.months // returns Boolean = true
      DateTime.now to DateTime.tomorrow  // return org.joda.time.Interval = > 2009-04-27T13:47:14.840/2009-04-28T13:47:14.840
      (DateTime.now to DateTime.nextSecond).millis // returns Long = 1000

      2.hours + 45.minutes + 10.seconds
      // returns com.github.nscala_time.time.DurationBuilder
      // (can be used as a Duration or as a Period)
      
      2.months + 3.days
      val now = DateTime.now
      assert(DateTime.nextMonth < DateTime.now + 2.months)
      */
    }
  }
}

        



