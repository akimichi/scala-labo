package lambda



/**
 * Lambda評価器の例
 * c.f. "Matching Objects With Patterns",p.19
 */
  /** 
   * monad. 
   */
  /*
  case class M[A](answer:A)  {
    def bindM[B](k: A => M[B]):M[B]     = M[B](k(answer).answer)
    def map[B](f: A => B): M[B]        = bindM(x => unitM(f(x)))
    def flatMap[B](f: A => M[B]): M[B] = bindM(f)
  }
  def unitM[A](a: A):M[A] = M[A](a)
  */

trait category {
  type Answer
  trait Monad[T[_]] {
    def apply[A](x : A) : T[A]
    def map[A,B](x : T[A])(f : A=>B) : T[B]
    def flatten[A](m : T[T[A]]) : T[A]
    def flatMap[A,B](x : T[A])(f : A => T[B]) : T[B] = flatten(map(x)(f))
  }
}

object category extends category with values {

   trait Result[A] {
     type Continuation = A => A
     def output:String
     def in: (A => A) => A
     def value:A = in(id)
     def id[A]:A => A = {x:A => x}
     override def toString = value.toString // showM(this)
   }
   object Result {
     implicit def monadOps[M[_] : Monad, A](ma: M[A]) = new {
       val monad = implicitly[Monad[M]]
       final def map[B](f : A=>B) : M[B] = monad.map(ma)(f)
       def flatten[B](implicit $ev0: M[A] <:< M[M[B]]) : M[B] = monad.flatten(ma)
       def flatMap[B](f : A=>M[B]) : M[B] = monad.flatMap(ma)(f)
     }

     implicit object monad extends Monad[Result] {
       override final def apply[A](a : A) = unitM(a)
       override final def map[A,B](ma : Result[A])(mapping : A => B) : Result[B]= new Result[B] {
         // val value:B = mapping(ma.value)
         val output:String = ma.output
         // val output:String = ma flatMap {a =>  // ma.output
         //   mapping(a)
         // }
           
         val in : (B => B) => B = {c:(B => B) =>
           c(mapping(ma.value))
         }
       }
       override final def flatMap[A,B](ma : Result[A])(f : A=>Result[B]) : Result[B] = new Result[B] {
         val output:String = {
           ma.output
         }
         val in : (B => B) => B = {(c:(B => B)) =>
           f(ma.in(ma.id)).in(c)
           // c(f(ma.in(ma.id)).value)
           // c(f(ma.value).value)
         }
         
       }
       override final def flatten[A](mma : Result[Result[A]]) : Result[A] = new Result[A] {
         // val value:A = mma.value.value
         val output:String = {
           mma.output 
           // mma.in{ma =>
           //   mma.output ++ (ma.output)
           // }
         }
         val in : (A => A) => A = {(c:(A => A)) =>
           c(mma.in(mma.value.id).in(mma.value.id))
           // c(mma.in(mma.value.id).value)
           // mma.in(mma.value.id).value
           // mma.value.in(mma.value.id)
           // c(mma.value.value)
         }
       }
     }

   }
   def unitM[A](a: A): Result[A] =  new Result[A]{
     val output:String = ""
     val in = {c:(A => A) =>
       c(a)
     }
   }
  // def id[A] = (x: A) => x

}

 /*
  *  Term 型付項
  *
  */ 
trait terms {
   trait Term[+T]
   case class Var[T] (val name : String) extends Term[T]
   case class Const(val value : Int) extends Term[Int]
   // case class Num(val value : Int) extends Term[Int]
  case class Add[T](l: Term[T], r: Term[T]) extends Term[T]
  /** lambda x:B.e:B -> C */
  case class Lam[T1, T2] (val x : Var[T1], val e : Term[T2]) extends Term[T1 => T2]
  case class App[T1, T2] (val f : Term[T1 => T2], val e : Term[T1]) extends Term[T2]
  // case class Succ() extends Term[Int => Int]
  case class Zero[T]()
  case class Out[T](u:Term[T]) extends Term[T]
  case class CCC[T](k:String, u:Term[T]) extends Term[T]
}

/**
 * 値 value
 */ 
trait values extends category {
  import category._
  
  type Answer
  
  trait Value
  case object Null extends Value {
    override def toString() = "<null>"
  }
  case object Wrong extends Value {
   override def toString() = "<wrong>"
  }
  case class Num(n: Int) extends Value {
    override def toString() = "Num(%d)".format(n) // n.toString()
  }
  case class Fun(f: Value => Result[Value]) extends Value {
    override def toString() = "<function>"
  }
}

/*
 * 環境 Environment
 *
 */ 
trait env extends category with terms with values {
  import category._

  abstract class Env {
    def apply[T](v : Var[T]): Value
    def extend[T](v : Var[T], x : Value) = new Env {
      def apply[T1](w: Var[T1]):Value = w match {
        case _: v.type => x
        case _ =>  Env.this.apply(w)
      }}}
  object emptyEnv extends Env {
    def apply[T](x : Var[T]):Value = Wrong
  }
  def lookup[T](variable: Var[T], env: Env): Result[Value] = unitM(env(variable))
}

trait interpreter extends category with terms with values with env {
  import category._

  // def succ(num: Value): M[Value] = num match {
  //   case Num(n) => unitM(Num(n + 1))
  //   case _ => unitM(Wrong)
  // }
  def out(a: Value) : Result[Value] = new Result[Value] {
    // val value:Value = a
    val output:String = "%s; ".format(a)
    val in = {c:(Value => Value) =>
      c(value)
    }
  }
  def add(a: Value, b: Value): Result[Value] = Pair(a, b) match {
    case Pair(Num(m), Num(n)) => unitM(Num(m + n))
    case _ => unitM(Wrong)
  }
  def apply(fun: Value, operand: Value): Result[Value] = fun match {
    case Fun(f) => f(operand)
    case _ => unitM(Wrong)
  }
  /*
   *  def callCC[A](h: (A => CMonad[A]) => CMonad[A]) : CMonad[A] =
   *   CMonad[A]{c =>
   *     h(a => CMonad[A](d => c(a))).in(c)
   *   }
   */
  def callCC(h: (Value => Result[Value]) => Result[Value]) : Result[Value] = new Result[Value]{
    def output = ""
    def in = (c:Value => Value) => h{a:Value =>
      new Result[Value]{
        def output = ""
        def in = {d => c(a)}
      }
    } in(c)
    // def in = {(c:Value=>Value) =>
    //   h{(a:Value) =>
    //     unitM(c(a))
    //   } in(c)
    // }
    // def in = {(c:A=>A) =>
    //   h{a:A =>
    //     new Result[A]{
    //       def output = ""
    //       def in = {d => c(a)}
    //     }
    //   } in(c)
    // }
  }
  // Evaluation:
  def interp[T](term : Term[T], env : Env): Result[Value] = term match {
    case v:Var[_] => lookup(v,env)
    case n:Const => unitM(Num(n.value))
    case f @ Lam(x, body) => unitM(Fun{arg =>
     interp(body, env.extend(x, arg))
    })
    case Add(l, r) => {
      for {
        a <- interp(l, env)
		b <- interp(r, env)
		c <- add(a, b)
      } yield c
    }
    case App(fun, arg) => {
      for {
        a <- interp(fun, env)
		b <- interp(arg, env)
		c <- apply(a, b)
      } yield c
    }
    case Out(u) => for {
      a <- interp(u, env)
      _ <- out(a)
    } yield a

    // callCC(k => interp(t, Pair(x, Fun(k)) :: e))
    case CCC(v, body) => {
      callCC{(k:Value => Result[Value]) =>
        interp(body, env.extend(Var(v), Fun(k)))        
      }
    }

  }
  def showM[A](m: Result[A]): String = (m in m.id).toString()
}

trait typedLambda extends category with terms with values with env with interpreter {
  import category._
  
  type Name = String

}
