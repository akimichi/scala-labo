package sample.actors

import akka.actor._
import akka.actor.Actor._
import akka.routing.{ Listeners, Listen }
import akka.testkit.{TestKit, TestActorRef,DefaultTimeout,ImplicitSender}
import com.typesafe.config.ConfigFactory


// class SilentActor extends Actor {
//   var internal_state = Vector[String]()
  
//   def receive = {
//     case SilentMessage(data) => {
//       internal_state = internal_state :+ data
//     }
//     case ReplyMessage => {
//       sender ! "silent"
//     }
//   }
//   def state = internal_state
// }
// case class SilentMessage(data:String)
// case class ReplyMessage()


// class ReplyActor extends Actor {
//   var internal_state = "internal"
  
//   def receive = {
//     case x:ReplyMessage => {
//       sender ! internal_state
//     }
//   }
//   // def internal_state = _internal_state
// }


// class ThrowActor extends Actor {
//   def receive = {
//     case message:String => {
//       throw new Exception(message)
//     }
//   }
// }


// class ParentActor extends Actor {
//   import akka.util.duration._
//   import akka.util.Timeout
//   import akka.actor.OneForOneStrategy
//   import akka.actor.SupervisorStrategy._
  
//   override val supervisorStrategy =
//     OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
//       case _: ArithmeticException      ⇒ Resume
//       case _: NullPointerException     ⇒ Escalate
//       case _: IllegalArgumentException ⇒ Stop
//       case _: Exception                ⇒ Restart
//     }

//   def receive = {
//     case message:String => {
//       val child_actor = context.actorOf(Props[ChildActor])
//       child_actor ! message
//     }
//   }
// }

// class ChildActor extends Actor {
//   def receive = {
//     case message:String => {
//       val throw_actor = context.actorOf(Props[ThrowActor])
//       throw_actor ! message
//     }
//   }
// }



