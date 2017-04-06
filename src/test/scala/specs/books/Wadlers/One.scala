package wadler

trait one {
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

  sealed trait Term
  case class Var(x: Name) extends Term
  case class Con(n: Int) extends Term
  case class Add(l: Term, r: Term) extends Term
  case class Lam(x: Name, body: Term) extends Term
  case class App(fun: Term, arg: Term) extends Term

  trait Value
  case object Wrong extends Value {
   override def toString() = "<wrong>"
  }
  case class Num(n: Int) extends Value {
    override def toString() = n.toString()
  }
  case class Fun(f: Value => Monad[Value]) extends Value {
    override def toString() = "<function>"
  }

  type Environment = List[Pair[Name, Value]]

  def showval(value: Value): String = value.toString()
  
  def interp(t: Term, e: Environment): Monad[Value] = t match {
    case Var(x) => lookup(x, e)
    case Con(n) => Success(Num(n))// unitM(Num(n))
    case Add(l, r) => {
      for {
        a <- interp(l, e)
		b <- interp(r, e)
		c <- add(a, b)
      } yield c
    }
    case Lam(x, t) => Success(Fun(a => interp(t, Pair(x, a) :: e))) // unitM(Fun(a => interp(t, Pair(x, a) :: e)))
    case App(f, t) => {
      for {
        a <- interp(f, e)
		b <- interp(t, e)
		c <- apply(a, b)
      } yield c
    }
  }

  def lookup(x: Name, e: Environment): Monad[Value] = e match {
    case List() => Error("unbound variable") // unitM(Wrong)
    case Pair(y, b) :: e1 => {
      if (x == y)
        Success(b) // unitM(b)
      else
        lookup(x, e1)
    }
  }


  def add(a: Value, b: Value): Monad[Value] = Pair(a, b) match {
    case Pair(Num(m), Num(n)) => Success(Num(m + n)) // unitM(Num(m + n))
    case _ => Error("wrong") // unitM(Wrong)
  }
  
  def apply(a: Value, b: Value): Monad[Value] = a match {
    case Fun(k) => k(b)
    case _ => Error("should be a function") // unitM(Wrong)
  }
  
  def test(t: Term): String = showM(interp(t, List()))
}

