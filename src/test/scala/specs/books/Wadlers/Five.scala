package wadler

trait five {
  type A = Value
  type Name = String
  

  /** 
   * Non-deterministic monad. 
   */
  /*
     trait Monad[A] {
       def bindM[B](f: A => Monad[B]):Monad[B]
       // def flatMap[B](f: A => Monad[B]):Monad[B] = bindM(f)
       def map[B](f: A => B):Monad[B]
     }
  */
  
  case class LMonad[+A](choice:List[A]) {
    def bindM[B](k: A => LMonad[B]):LMonad[B] = {
      val list_monads:List[LMonad[B]] = for {
        item <- choice
        b = k(item)
      } yield b
      list_monads.foldRight(LMonad(List.empty[B])){(accum, item) =>
        plusM(accum, item)
      }
    }
    def flatMap[B](f: A => LMonad[B]):LMonad[B] = bindM(f)
    def map[B](f: A => B):LMonad[B] = {
      bindM{ a =>
        val b = f(a)
        unitM(b)
      }
    }
    def showM : String = "Choices: %s".format(choice)
  }
  def unitM[T](a: T) : LMonad[T] = LMonad[T](List(a))
  def zeroM[T]() : LMonad[T] = LMonad[T](List.empty[T])
  def plusM[T](a: LMonad[T], b:LMonad[T]) : LMonad[T] = LMonad(a.choice ++ b.choice)

  /**
   * 項 term
   */ 
  sealed trait Term
  case class Var(x: Name) extends Term
  case class Con(n: Int) extends Term
  case class Add(l: Term, r: Term) extends Term
  case class Lam(x: Name, body: Term) extends Term
  case class App(fun: Term, arg: Term) extends Term
  case class Fail() extends Term
  case class Amb(terms:Stream[Term]) extends Term

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
  case class Fun(f: Value => LMonad[Value]) extends Value {
    override def toString() = "<function>"
  }

  /**
   * 評価器 interpreter
   */ 
  type Environment = List[Pair[Name, Value]]

  def interp(t: Term, e: Environment): LMonad[Value] = t match {
    case Var(x) => lookup(x, e)
    case Con(n) => unitM(Num(n))
    case Add(l, r) => {
      for {
        a <- interp(l, e)
		b <- interp(r, e)
		c <- add(a, b)
      } yield c
    }
    case Lam(x, t) => unitM(Fun(a => interp(t, Pair(x, a) :: e)))
    case App(f, t) => {
      for {
        a <- interp(f, e)
		b <- interp(t, e)
		c <- apply(a, b)
      } yield {
        c
      }
    }
    case Fail() => zeroM()
    case Amb(stream) => {
      val results = for {
        term <- stream
      } yield {
        interp(term, e)
      }
      results.foldRight(LMonad(List.empty[Value])){(accum, item) =>
        plusM(accum, item)
      }
    }
  }

  def lookup(x: Name, e: Environment): LMonad[Value] = e match {
    case List() => unitM(Wrong)
    case Pair(y, b) :: e1 => {
      if (x == y)
        unitM(b)
      else
        lookup(x, e1)
    }
  }

  def add(a: Value, b: Value): LMonad[Value] = Pair(a, b) match {
    case Pair(Num(m), Num(n)) => unitM(Num(m + n))
    case _ => unitM(Wrong)
  }
  
  def apply(a: Value, b: Value): LMonad[Value] = a match {
    case Fun(k) => k(b)
    case _ => unitM(Wrong)
  }
  
  
  def test(t: Term): String = {
    interp(t, List()).showM
  }
}

