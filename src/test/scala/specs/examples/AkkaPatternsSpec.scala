import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Suite, FunSpec, WordSpec, BeforeAndAfterAll}


import akka.actor._
import akka.actor.Actor._
import akka.routing.{ Listeners, Listen }
import akka.testkit.{TestKit, TestActorRef,DefaultTimeout,ImplicitSender}
import com.typesafe.config.ConfigFactory

import scala.util.matching.Regex

// object PipeFilterSpec {
//   val config = """
//     akka {
//       loglevel = "WARNING"
//       actor {
//         deployment {
//           /my-service {
//             router = round-robin
//             nr-of-instances = 3
//           }
//         }
//       }
//     }"""
  
//   class EchoActor extends Actor {
//     def receive = {
//       case data:Data => sender ! data
//     }
//   }
//   class QueryActor(pattern:Regex, pipe:ActorRef) extends Actor {
//     def receive = {
//       case data:Data => {
//         val filtered = data.rows.filter{ row =>
//           pattern.findFirstIn(row) match {
//             case Some(the_match) => true
//             case None => false
//           }
//         }
//         pipe ! data.copy(
//           rows = filtered
//         )
//       }
//     }
//   }
//   class SortActor(direction:Boolean, pipe:ActorRef) extends Actor {
//     def receive = {
//       case data:Data => {
//         pipe ! {
//           if(direction == true) {
//             data.copy(
//               rows = data.rows.sortWith{(a,b) => a < b }
//               )
//           } else {
//             data.copy(
//               rows = data.rows.sortWith{(a,b) => a > b }
//               )
//           }
//         }
//       }
//     }
//   }
//   class LimitActor(limit:Int, pipe:ActorRef) extends Actor {
//     def receive = {
//       case data:Data => {
//         pipe ! data.copy(
//           rows = data.rows.take(limit)
//         )
//       }
//     }
//   }
//   case class Data(rows:List[String])
// }


// class PipeFilterSpec extends TestKit(ActorSystem("PipeFilterSpec", ConfigFactory.parseString(PipeFilterSpec.config))) with DefaultTimeout with ImplicitSender  with FunSpec with ShouldMatchers with BeforeAndAfterAll {
//   import PipeFilterSpec._
//   import akka.util.duration._
  
//   override def afterAll() {
//     system.shutdown()
//   }
//   // describe("Pipe and Filter の例") {
//   //   it("Query,Sort,Limitの順で実行する") {
//   //     val echo_actor = system.actorOf(Props(new EchoActor))
//   //     val limit_actor = system.actorOf(Props(new LimitActor(3,echo_actor)))
//   //     val sort_actor = system.actorOf(Props(new SortActor(true,limit_actor)))
//   //     val pattern:Regex = """[0-9]+""".r
//   //     val query_actor = system.actorOf(Props(new QueryActor(pattern, sort_actor)))
//   //     val data = Data(List("123","abc","098qwe","poi567","0987"))
//   //     query_actor ! data
//   //     expectMsg("test")
//   //   }
//   // }
//   // "An EchoActor" should {
//   //   "Respond with the same message it receives" in {
//   //     within(500 millis) {
//   //       echo_actor ! "test"
//   //       expectMsg("test")
//   //     }
//   //   }
//   // }
// }
