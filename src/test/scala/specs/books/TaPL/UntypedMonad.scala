trait UntypedMonad {
  type Name = String
  /** 
   * monad. 
   */
  trait Monad[A] {
    def unitM[A](a: A):Monad[A]
    def bindM[B](k: A => Monad[B]):Monad[B]
    def map[B](f: A => B): Monad[B] = bindM(a => unitM(f(a)))
    def flatMap[B](f: A => Monad[B]): Monad[B] = bindM(f)
  }
  case class Success[A](a:A) extends Monad[A] {
    def unitM[A](a: A):Monad[A] = Success(a)
    def bindM[B](k: A => Monad[B]):Monad[B] = k(a)
  }
  case class Error[A](s:String) extends Monad[A]{
    def unitM[String](a: String):Monad[String] = Error(s)
    def bindM[B](k: A => Monad[B]):Monad[B] = Error(s)
  }
  def showM[A](m: Monad[A]): String = {
    m match {
      case Success(a) => a.toString
      case Error(s) => s.toString
    }
  }
  // Environments:
  trait env extends terms {
    abstract class Env  {
      def apply(v : Var): Monad[Value]
      def extend(v : Var, x : Value) = new Env {
        def apply(w: Var): Monad[Value] = w match {
          case _: v.type => Success(x) // v eq w, hence a = b
          case _ =>  Env.this.apply(w)
        }
      }
    }
    object emptyEnv extends Env {
      def apply(v : Var): Monad[Value] = Error("not found : " + v.x) 
    }
  }
  trait evaluator extends env with terms {
    def eval(t : Term, env : Env): Monad[Value]
  }
  object evaluator extends evaluator with env {

    def eval(t: Term, env: Env = emptyEnv): Monad[Value] = t match {
      case v : Var => env(v)
      case Lam(x, t) => {
        Success(Fun(a => eval(t, env.extend(x, a))))
      }
      case num @ Num(n) => Success(num)// unitM(Num(n))
      case App(f,t) => {
        for {
          a <- eval(f, env)
		  b <- eval(t, env)
		  c <- fapply(a, b)
        } yield c
        // Success(eval(a.f, env)(eval(a.e, env)))
      }
      case add @ Add(l, r) => {
        for {
          a <- eval(l, env)
		  b <- eval(r, env)
        } yield {
          (a,b) match {
            case Pair(Num(m), Num(n)) => add.calc(Num(m))(Num(n))
            case _ => throw new Exception
          }
        }
      }
      case sub @ Sub(l, r) => {
        for {
          a <- eval(l, env)
		  b <- eval(r, env)
        } yield {
          (a,b) match {
            case Pair(Num(m), Num(n)) => sub.calc(Num(m))(Num(n))
            case _ => throw new Exception
          }
        }
      }
      case mul @ Mul(l, r) => {
        for {
          a <- eval(l, env)
		  b <- eval(r, env)
        } yield {
          (a,b) match {
            case Pair(Num(m), Num(n)) => mul.calc(Num(m))(Num(n))
            case _ => throw new Exception
          }
        }
      }
      case IsZero(i) => {
        for {
          n <- eval(i, env)
        } yield {
          n match {
            case Num(m) => {
              if(m == 0)
                True
              else
                False
            }
            case _ => throw new Exception
          }
        }
      }
      case Succ(i) => {
        for {
          n <- eval(i, env)
        } yield {
          n match {
            case Num(m) => Num(m + 1) // unitM(Num(m + n))
            case _ => throw new Exception
          }
        }
      }
      case Cond(pred,tru,fls) => {
        for {
          condition <- eval(pred,env)
          true_statement <- eval(tru,env)
          false_statement <- eval(fls,env)
        } yield {
          condition match {
            case True => true_statement
            case False => false_statement
            case _ => throw new Exception
          }
        }
      }
    }
    def fapply(a: Value, b: Value): Monad[Value] = a match {
      case Fun(k) => k(b)
      case _ => Error("should be a function") // unitM(Wrong)
    }
  }
  // Terms:
  trait terms {
    sealed trait Term
    case class Var(x: Name) extends Term
    case class Lam(x: Var, body: Term) extends Term
    case class App(fun: Term, arg: Term) extends Term
    case class Succ(i:Term) extends Term
    case class Cond (val pred:Term, val tru:Term, val fls:Term ) extends Term

    case class IsZero(i: Term) extends Term
    
    trait BinOp extends Term {
      def calc:Num => Num => Num
    }
    case class Add(l: Term, r: Term) extends BinOp {
      def calc:Num => Num => Num = {l:Num => {r:Num =>
        Num(l.n + r.n)
      }}
    }
    case class Sub(l: Term, r: Term) extends BinOp {
      def calc:Num => Num => Num = {l:Num => {r:Num =>
        Num(l.n - r.n)
      }}
    }
    case class Mul(l: Term, r: Term) extends BinOp {
      def calc:Num => Num => Num = {l:Num => {r:Num =>
        Num(l.n * r.n)
      }}
    }
    trait Value extends Term
    case object Wrong extends Value {
      override def toString() = "<wrong>"
    }
    case class Fun(f: Value => Monad[Value]) extends Value {
      override def toString() = "<function>"
    }
    case class Num(n: Int) extends Value {
      override def toString() = n.toString()
    }
    class Bool(val value : Boolean) extends Value
    case object True extends Bool(true)
    case object False extends Bool(false)
    
  }
}
