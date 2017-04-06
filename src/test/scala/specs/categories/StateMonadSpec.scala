import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/** StateMonad  (c.f. Scala in Action,sec.5.7)
 *  "So state monad is a general abstraction layer that allows you to build computations for sequences of actions that require a shared state."
 *
 *
 *  T α ≝ State ⇒ (State × α)
 *  unit :: α ⇒ T α
 *  bind :: T α ⇒ (α ⇒ T β) ⇒ T β
 *  get :: T State
 *  put :: State ⇒ T 1
 *
 * 
 * 
 */ 
class StateMonadSpec extends FunSpec with ShouldMatchers {

  trait StateMonad {
    import State._
    trait State[S, +A] {
      def apply(s: S): (S, A)
      def bind[B](f: A => State[S, B]): State[S, B] = state(apply(_) match {
        case (s, a) => f(a)(s)
      })
      def map[B](f: A => B): State[S, B] = bind{ a:A =>
        new State[S, B] {
          def apply(s: S) = (s,f(a))
        } 
      }
      def flatMap[B](f: A => State[S, B]): State[S, B] = bind(f)
    }
    object State {
      def state[S, A](f: S => (S, A)) = new State[S, A] {
        def apply(s: S) = f(s)
      }
      def init[S]: State[S, S] = state[S, S](s => (s, s))
      def modify[S](f: S => S) =
        init[S] flatMap (s => state(_ => (f(s), ())))
    }
  }

  trait MonadState[S] extends StateMonad {
    def get[S]: State[S, S] = State.state(s => (s, s))
    def put[S](s: S): State[S, Unit] = State.state(s => (s, ()))
  }
}


