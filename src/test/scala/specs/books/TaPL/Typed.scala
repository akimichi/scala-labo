


trait TypedLambda {
  // Terms:
  trait terms {
    trait Term[A]
    case class Var[T] (val name : String) extends Term[T]
    case class Lam[Tx, Tr] (val arg : Var[Tx], val body : Term[Tr]) extends Term[Tx => Tr]
    case class App[T1, T2] (val func : Term[T1 => T2], val arg : Term[T1]) extends Term[T2]
    
    case class Num(val value : Int) extends Term[Int]
    case class Succ() extends Term[Int => Int]
  }
  trait env extends terms {
    // Environments:
    abstract class Env {
      def apply[T](v : Var[T]): T
      def extend[T](v : Var[T], x : T) = new Env {
        def apply[U](vv: Var[U]): U = vv match {
          case _: v.type => x
          case _ =>  Env.this.apply(vv)
        }}}
    object emptyEnv extends Env {
      def apply[T](x : Var[T]): T = throw new Error("not found : "+x.name) 
    }
  }

  trait evaluator extends env with terms {
    def eval[A](t : Term[A], env : Env): A
  }
  object evaluator extends evaluator {
    // Evaluation:
    def eval[a](t : Term[a], env : Env): a = t match {
      case v : Var[b] => env(v) // a = b
      case n : Num => n.value // a = Int
      case i : Succ => { y : Int => y + 1 } // a = Int=>Int
      case f : Lam[b,c] => { y : b =>
        val newEnv = env.extend(f.arg, y)
        eval(f.body, newEnv)
      }
      case a : App[b,c] => eval(a.func, env)(eval(a.arg, env)) // a = c
    }
  }
}
