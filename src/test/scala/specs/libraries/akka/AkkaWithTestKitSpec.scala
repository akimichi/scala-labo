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


/**
 * Akkaライブラリ の基本的な用法を示す
 */
// import sample.actors._

// object TestKitUsageSpec {
//   // Define your test specific configuration here
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
//       case msg => sender ! msg
//     }
//   }
// }

// class AkkaWithTestKitSpec extends TestKit(ActorSystem("TestKitUsageSpec", ConfigFactory.parseString(TestKitUsageSpec.config))) with DefaultTimeout with ImplicitSender  with FunSpec with ShouldMatchers with BeforeAndAfterAll {
//   import TestKitUsageSpec._
//   import akka.util.duration._
  
//   override def afterAll() {
//     system.shutdown()
//   }
//   describe("ConfigFactoryで設定する") {
//     it("EchoActor の例") {
//       val echo_actor = system.actorOf(Props(new EchoActor))
//       echo_actor ! "test"
//       echo_actor ! "test"
//       echo_actor ! "test"
//       expectMsg("test")
//     }
//   }
//   describe("例外を捕捉する") {

//     it("intercept で例外を捕捉する"){
//       import akka.testkit.TestActorRef
//       val actorRef = TestActorRef(new Actor {
//         def receive = {
//           case "hello" => throw new IllegalArgumentException("boom")
//         }
//       })
//       intercept[IllegalArgumentException] { actorRef.receive("hello") }
//     }

//     it("ThrowActor を使う"){
//       val actor = TestActorRef[ThrowActor]
//       intercept[Exception] { actor.receive("hello") }
//       // intercept[Exception] { actor ! "hello" }
//     }
//   }

//   describe("ReplyActor") {
//     it("ReplyMessageで内部状態を取得する") {
//       import akka.pattern.ask
//       import akka.util.duration._
//       import akka.util.Timeout
//       import akka.dispatch.Await
//       implicit val timeout = Timeout(1 seconds)
      
//       val actor = TestActorRef[ReplyActor]
//       val reply = (actor ? ReplyMessage()).mapTo[String]
//       val result = Await.result(reply,timeout.duration)
//       result should equal("internal")
//       info("underlyingActorを使えば、内部の状態を取得できる")
//       actor.underlyingActor.internal_state should equal("internal")
//     }    
//   }
//   describe("Akka in Action にて") {
//     it("2.2.1 の SilentActor の例") {
//       // import silent_actor._
//       val actor = TestActorRef[SilentActor]
//       actor ! SilentMessage("...")
//       actor.underlyingActor.state should contain("...")
//     }
//   }
//   describe("Scalaレシピ") {
//     it("askでfutureを使う例") {
//       import akka.pattern.ask
//       import akka.util.duration._
//       import akka.util.Timeout
//       import akka.dispatch.Await
//       // import silent_actor._
      
//       implicit val timeout = Timeout(1 seconds)
//       val future_actor = TestActorRef[SilentActor]
      
//       val future = future_actor ? ReplyMessage()

//       future onComplete {
//         case Right(result) => {
//           result should equal("future message")
//         }
//         case Left(failure) => failure.printStackTrace
//       }
//     }
//   }
  
// }
