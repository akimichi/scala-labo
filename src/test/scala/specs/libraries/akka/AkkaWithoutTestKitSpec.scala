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
// class AkkaWithoutTestKitSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
//   import akka.util.duration._
//   import sample.actors._

//   val actor_system = ActorSystem("scala-labo-global")
  
//   override def afterAll() {
//     actor_system.shutdown()
//   }

//   describe("ActorSystemを利用して") {
//     // これを起動するとテスト終了後もスレッドが殘り続ける。
//     // it("ActorSystemを複数起動する"){
//     //   for(index <- 0 to 10) {
//     //     val actor_system = ActorSystem("scala-labo-%d".format(index))
//     //     val props = Props(new SilentActor)
//     //     val actor:ActorRef = actor_system.actorOf(props, "%d".format(index))
//     //   }
//     // }
//     it("単一のActorSystemでアクターを複数個起動する"){
      
//       for(index <- 0 to 10) {
//         val props = Props(new SilentActor)
//         val actor:ActorRef = actor_system.actorOf(props, "scala-labo-global-%d".format(index))
//       }
//     }
//     it("contextで子のアクターを生成する"){
//       val parent_actor:ActorRef = actor_system.actorOf(Props(new ParentActor))
//       try {
//         parent_actor ! "Hi"
//       } catch {
//         case ex:Exception => {
//           ex.getMessage should equal("")
//         }
//       }
      
//     }
//   }
// }

