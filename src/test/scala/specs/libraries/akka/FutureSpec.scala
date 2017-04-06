import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Suite, FunSpec, WordSpec, BeforeAndAfterAll}


// import akka.routing.{ Listeners, Listen }
// import akka.testkit.{TestKit, TestActorRef,DefaultTimeout,ImplicitSender}
// import com.typesafe.config.ConfigFactory



// class FutureSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
//   object ExpensiveCalc {
//     val NumInterations = 1000
    
//     def expensiveCalc = calculatePiFor(0, 1000000)
    
//     def calculatePiFor(start: Int, nrOfElements: Int): Double = {
//         var acc = 0.0
//       for (i <- start until (start + nrOfElements))
//         acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
//       acc
//     }
//   }

//   describe("Futureを用いて") {
//     import akka.actor._
//     import akka.actor.Actor._
//     import akka.dispatch.{Await, ExecutionContext, Future}
// //    import akka.util.duration._
    
//     it("ActorSystem から ExecutionContext を提供する"){
//       implicit val system = ActorSystem("MySystem").dispatcher
      
//       val future = Future {
//         ExpensiveCalc.expensiveCalc
//       }
//       future onComplete { result =>
//         result match {
//           case Right(the_result) => {
//             the_result should equal(3.1415916535897743)
//           }
//           case Left(ex) => fail(ex)
//         }
//       }
//     }
//     it("明示的に ExecutionContext を提供する"){
//       import java.util.concurrent.{TimeoutException, Executors}
      
//       val executorService = Executors.newFixedThreadPool(8)
//       implicit val context = ExecutionContext.fromExecutor(executorService)
      
//       val future = Future {
//         1 * 10
//       }
//       future onComplete { result =>
//         result match {
//           case Right(the_result) => {
//             the_result should equal(10)
//           }
//           case Left(ex) => fail(ex)
//         }
//       }
//     }
//     it("for"){
//       import java.util.concurrent.{TimeoutException, Executors}
//       val executorService = Executors.newFixedThreadPool(8)
//       implicit val context = ExecutionContext.fromExecutor(executorService)
      
//       val futures = for {
//         a <- Future{ExpensiveCalc.expensiveCalc}
//         b <- Future{ExpensiveCalc.expensiveCalc}
//         c <- Future{ExpensiveCalc.expensiveCalc}
//       } yield (a + b + c)/3
//       info("for式のなかの future は並列実行されていない")
//       futures onComplete { result =>
//         result match {
//           case Right(the_result) => {
//             the_result should equal(3.1415916535897743)
//           }
//           case Left(ex) => fail(ex)
//         }
//       }
//     }
//     it("sequence"){
//       import java.util.concurrent.{TimeoutException, Executors}
//       val executorService = Executors.newFixedThreadPool(8)
//       implicit val context = ExecutionContext.fromExecutor(executorService)
//       val listOfFutures:List[Future[Double]] = List(Future{ExpensiveCalc.expensiveCalc}.mapTo[Double],
//                                                     Future{ExpensiveCalc.expensiveCalc}.mapTo[Double],
//                                                     Future{ExpensiveCalc.expensiveCalc}.mapTo[Double])
      
//       // now we have a Future[List[Int]]
//       // val futureList:Future[List[Double]] = Future.sequence(listOfFutures)
      
//       // Find the sum of the odd numbers
//       Future.fold(listOfFutures)(0.0)(_ + _) onComplete { result =>
//         result match {
//           case Right(the_result) => {
//             (the_result / 3) should equal(3.1415916535897743)
//           }
//           case Left(ex) => fail(ex)
//         }
//       }
//     }
//     it("Streamのsequence"){
//       import java.util.concurrent.{TimeoutException, Executors}
//       val executorService = Executors.newFixedThreadPool(8)
//       implicit val context = ExecutionContext.fromExecutor(executorService)
//       val streamOfFutures:Stream[Future[Double]] = Stream.cons(Future{ExpensiveCalc.expensiveCalc}.mapTo[Double], Stream.cons(Future{ExpensiveCalc.expensiveCalc}.mapTo[Double], Stream.cons(Future{ExpensiveCalc.expensiveCalc}.mapTo[Double],Stream.empty)))
//       // now we have a Future[List[Int]]
//       val futureStream:Future[Stream[Double]] = Future.sequence(streamOfFutures)
      
//       // Find the sum of the odd numbers
//       Future.fold(streamOfFutures)(0.0)(_ + _) onComplete { result =>
//         result match {
//           case Right(the_result) => {
//             (the_result / 3) should equal(3.1415916535897743)
//           }
//           case Left(ex) => fail(ex)
//         }
//       }
//     }
//     // it("andThen"){
//     //   import java.util.concurrent.{TimeoutException, Executors}
//     //   val executorService = Executors.newFixedThreadPool(8)
//     //   implicit val context = ExecutionContext.fromExecutor(executorService)
//     //   val firstFuture = Future{ExpensiveCalc.expensiveCalc}.mapTo[Double]
//     //   val secondFuture = Future{ExpensiveCalc.expensiveCalc}.mapTo[Double]
//     //   val thirdFuture = Future{ExpensiveCalc.expensiveCalc}.mapTo[Double]

//     //   firstFuture andThen {
//     //     case Success(result) => secondFuture
//     //   } andThen {
//     //     case Success(result) => thirdFuture
//     //   }
      
//     //   // Find the sum of the odd numbers
//     //   Future.fold(listOfFutures)(0.0)(_ + _) onComplete { result =>
//     //     result match {
//     //       case Right(the_result) => {
//     //         (the_result / 3) should equal(3.1415916535897743)
//     //       }
//     //       case Left(ex) => fail(ex)
//     //     }
//     //   }
//     // }
//   }
// }

