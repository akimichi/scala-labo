package lambda.monadTransformer



/**
 * Lambda評価器の例
 * c.f. "Monads and Monad Transformer"
 */

trait category {
  /** 
   * monad. 
   */
  trait Monad[+A] {
    def unitM[T](a: T):Monad[T]
    def bindM[B](k: A => Monad[B]):Monad[B]
    def map[B](f: A => B): Monad[B] = bindM(a => unitM(f(a)))
    def flatMap[B](f: A => Monad[B]): Monad[B] = bindM(f)
  }

  object Monad {
    abstract class Identity[A](value:A) extends Monad[A] {
      def unitM[T](value: T):Monad[T] = new Identity[T](value){}
      def bindM[B](k: A => Monad[B]):Monad[B] = k(value)
    }
    
  }
}

object category extends category {
  // trait MonadTrans[F[_[_], _]] {
  //   def lift[G[_] : Monad, A](a: G[A]): F[G, A]
  // }
}

