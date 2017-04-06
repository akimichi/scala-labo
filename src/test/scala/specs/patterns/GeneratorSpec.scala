
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


// class GeneratorSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
//   import scala.collection.Iterator
//   import scala.util.continuations._

//   class StandaloneGenerator[T] extends Iterator[T] {
//     private var nextValue:Option[T] = None
//     private var nextStep:Option[Unit=>Unit] = None

//     /** Subclass calls this method to generate values.
//      * @param body The code for your generator.
//      */
//     protected def generate(body: => Unit @suspendable) {
//       reset {
//         suspend
//         body
//       }
//     }

//     /** Yield the next generated value.
//      * Call this code from your generator to deliver the next value.
//      */
//     protected def yld(x:T):Unit @suspendable = {
//       nextValue = Some(x)
//       suspend
//     }

//     /** Retrieve the next generated value.
//      * Call this from your main code.
//      */
//     def next:T = {
//       step
//       nextValue match {
//         case None => throw new NoSuchElementException("next on empty generator")
//         //make it similar to the equivalent Iterator exception
//         case Some(x) => nextValue = None; x
//       }
//     }

//     /** True if there is another value to retrieve.
//      * Call this from your main code.
//      */
//     def hasNext:Boolean = {
//       step
//       nextValue.isDefined
//     }

//     /** Save our continuation to resume later. */
//     private def suspend:Unit @suspendable = {
//       shift { k:(Unit=>Unit) =>
//         nextStep = Some(k)
//       }
//     }

//     /** If we have a next step but we don't have a value queued up,
//      * run the generator until it calls yld or completes. */
//     private def step = {
//       if (nextValue.isEmpty) nextStep foreach { nextStep = None; _() }
//     } 
//   }
//   describe("整数生成子 IntGen"){
//     class IntGen(max:Int) extends StandaloneGenerator[Int] {
//       generate {
// 	var x = 1
// 	while (x<=max) {
//           yld(x)
//           x = x + 1
// 	}
//       }
//     }
//     val ints:Iterator[Int] = for {
//       generated <- new IntGen(3) 
//     } yield generated
//     ints.length should equal(3)
//   }
// }
