package wadler

trait three {
  type S = Num
  type Name = String
  
  // type S
  // trait S {
  //   def init:S
  //   def succ:S
  // }

  /** 
   * monad. 
   */
  trait StateMonad[A] {
    def run(initial:S) : (S,A)
    def bindM[B](f: A => StateMonad[B]):StateMonad[B] = {
      unitM { s =>
        val (s1,a) = run(s)
        f(a).run(s1)
      }
    }
    def flatMap[B](f: A => StateMonad[B]):StateMonad[B] = bindM(f)
    // bindM(a => unitM(f(a)))
    def map[B](f: A => B):StateMonad[B] = {
      bindM{ a =>
        val b = f(a)
        unitM { s =>
          (s,b)
        }
      }
    }
    def showM(s:S) : String = {
      run(s) match {
        case (s,a) => "State: %s, Value: %s".format(s,a)
      }
    }
  }
  def unitM[T](f: S => (S,T)) : StateMonad[T] = new StateMonad[T] {
    def run(initial:S) : (S,T) = f(initial)
  }
  def fetchM : S => (S,S) = {(s:S) =>
    (s,s)
  }
  object StateMonad {
    def get:StateMonad[S] = unitM{ (s:S) =>
      (s,s)
    }
    def put(s:S):StateMonad[Unit] = unitM{ _ =>
      (s,())
    }
  }

  /**
   * 項 term
   */ 
  sealed trait Term
  case class Var(x: Name) extends Term
  case class Con(n: Int) extends Term
  case class Add(l: Term, r: Term) extends Term
  case class Lam(x: Name, body: Term) extends Term
  case class App(fun: Term, arg: Term) extends Term
  case class Count() extends Term

  /**
   * 値 value
   */ 
  trait Value
  case object Null extends Value {
    override def toString() = "<null>"
  }
  case object Wrong extends Value {
   override def toString() = "<wrong>"
  }
  case class Num(n: Int) extends Value {
    override def toString() = n.toString()
  }
  case class Fun(f: Value => StateMonad[Value]) extends Value {
    override def toString() = "<function>"
  }

  /**
   * 評価器 interpreter
   */ 
  type Environment = List[Pair[Name, Value]]

  // def showval(value: Value): String = value.toString()
  
  def interp(t: Term, e: Environment): StateMonad[Value] = t match {
    case Var(x) => lookup(x, e)
    case Con(n) => unitM{ s => 
      (s,Num(n))
    } // Success(Num(n))
    case Add(l, r) => {
      for {
        a <- interp(l, e)
		b <- interp(r, e)
		c <- add(a, b)
      } yield c
    }
    case Lam(x, t) => unitM { s =>
      (s,Fun(a => interp(t, Pair(x, a) :: e)))
    }
    case App(f, t) => {
      for {
        a <- interp(f, e)
		b <- interp(t, e)
		c <- apply(a, b)
        d <- tickM(c)
      } yield {
        d
      }
    }
    case Count() => unitM(fetchM)
  }

  def tickM(a:Value) : StateMonad[Value] = unitM {s => 
    (Num(s.n + 1), a)
  }
  def lookup(x: Name, e: Environment): StateMonad[Value] = e match {
    case List() => unitM{ s =>
      (s,Wrong)
    } // Error("unbound variable") 
    case Pair(y, b) :: e1 => {
      if (x == y)
        unitM { s =>
          (s,b)
        }// Success(b)
      else
        lookup(x, e1)
    }
  }

  def add(a: Value, b: Value): StateMonad[Value] = Pair(a, b) match {
    case Pair(Num(m), Num(n)) => unitM { s => 
      (s,Num(m + n))
    } // Success(Num(m + n)) 
    case _ => unitM { s =>
      (s,Wrong)
    }// Error("wrong")
  }
  
  def apply(a: Value, b: Value): StateMonad[Value] = a match {
    case Fun(k) => k(b)
    case _ => unitM { s =>
      (s,Wrong)
    } // Error("should be a function")
  }
  
  def test(t: Term, initial:S): String = {
    interp(t, List()).showM(initial)
  }
}

