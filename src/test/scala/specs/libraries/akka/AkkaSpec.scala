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

/*
object TestKitUsageSpec {
  // Define your test specific configuration here
  val config = """
    akka {
      loglevel = "WARNING"
      actor {
        deployment {
          /my-service {
            router = round-robin
            nr-of-instances = 3
          }
        }
      }
    }"""
  class EchoActor extends Actor {
    def receive = {
      case msg => sender ! msg
    }
  }
}

class TestKitUsageSpec extends TestKit(ActorSystem("TestKitUsageSpec", ConfigFactory.parseString(TestKitUsageSpec.config))) with DefaultTimeout with ImplicitSender  with FunSpec with ShouldMatchers with BeforeAndAfterAll {
  import TestKitUsageSpec._
  import akka.util.duration._
  
  override def afterAll() {
    system.shutdown()
  }
  describe("ConfigFactoryで設定する") {
    it("EchoActor の例") {
      val echo_actor = system.actorOf(Props(new EchoActor))
      // val actor = system.actorOf(Props(new EchoActor))
      // actor ! "test"
      echo_actor ! "test"
      echo_actor ! "test"
      echo_actor ! "test"
      expectMsg("test")
    }
  }
  // "An EchoActor" should {
  //   "Respond with the same message it receives" in {
  //     within(500 millis) {
  //       echo_actor ! "test"
  //       expectMsg("test")
  //     }
  //   }
  // }
}

object silent_actor {
  class SilentActor extends Actor {
    var internal_state = Vector[String]()
    
    def receive = {
      case SilentMessage(data) => {
        internal_state = internal_state :+ data
      }
      case ReplyMessage(data) => {
        sender ! data
      }
    }
    def state = internal_state
  }
  case class SilentMessage(data:String)
  case class ReplyMessage(data:String)
}

object reply_actor {
  class ReplyActor extends Actor {
    var internal_state = "internal"
    
    def receive = {
      case ReplyMessage() => {
        sender ! internal_state
      }
    }
    // def internal_state = _internal_state
  }
  case class ReplyMessage()
}

object throw_actor {
  class ThrowActor extends Actor {
    def receive = {
      case message:String => {
        throw new Exception(message)
      }
    }
  }
  case class ReplyMessage()
}


class AkkaSpec extends TestKit(ActorSystem("testsystem")) with FunSpec with ShouldMatchers with BeforeAndAfterAll {
  override def afterAll() {
    system.shutdown()
  }

  describe("例外を捕捉する") {

    it("intercept で例外を捕捉する"){
      import akka.testkit.TestActorRef
      val actorRef = TestActorRef(new Actor {
        def receive = {
          case "hello" => throw new IllegalArgumentException("boom")
        }
      })
      intercept[IllegalArgumentException] { actorRef.receive("hello") }
    }

    it("ThrowActor を使う"){
      import throw_actor._
      val actor = TestActorRef[ThrowActor]
      intercept[Exception] { actor.receive("hello") }
    }
  }

  describe("ReplyActor") {
    it("ReplyMessageで内部状態を取得する") {
      import akka.pattern.ask
      import akka.util.duration._
      import akka.util.Timeout
      import akka.dispatch.Await
      import reply_actor._
      implicit val timeout = Timeout(1 seconds)
      
      val actor = TestActorRef[ReplyActor]
      val reply = (actor ? ReplyMessage()).mapTo[String]
      val result = Await.result(reply,timeout.duration)
      result should equal("internal")
    }    
  }
  describe("Akka in Action にて") {
    it("2.2.1 の SilentActor の例") {
      import silent_actor._
      val actor = TestActorRef[SilentActor]
      actor ! SilentMessage("...")
      actor.underlyingActor.state should contain("...")
    }
  }
  describe("Scalaレシピ") {
    it("askでfutureを使う例") {
      import akka.pattern.ask
      import akka.util.duration._
      import akka.util.Timeout
      import akka.dispatch.Await
      import silent_actor._
      
      implicit val timeout = Timeout(1 seconds)
      val future_actor = TestActorRef[SilentActor]
      
      val future = future_actor ? ReplyMessage("future message")

      future onComplete {
        case Right(result) => {
          result should equal("future message")
        }
        case Left(failure) => failure.printStackTrace
      }
    }
  }
}



class AkkaWithoutTestKitSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  import silent_actor._
  import akka.util.duration._

  val actor_system = ActorSystem("scala-labo-global")
  
  override def afterAll() {
    actor_system.shutdown()
  }

  describe("ActorSystemを利用して") {
    // これを起動するとテスト終了後もスレッドが殘り続ける。
    // it("ActorSystemを複数起動する"){
    //   for(index <- 0 to 10) {
    //     val actor_system = ActorSystem("scala-labo-%d".format(index))
    //     val props = Props(new SilentActor)
    //     val actor:ActorRef = actor_system.actorOf(props, "%d".format(index))
    //   }
    // }
    it("単一のActorSystemでアクターを複数起動する"){
      for(index <- 0 to 10) {
        val props = Props(new SilentActor)
        val actor:ActorRef = actor_system.actorOf(props, "scala-labo-global-%d".format(index))
      }
    }
  }
}
*/
