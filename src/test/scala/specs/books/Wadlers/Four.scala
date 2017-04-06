package wadler

trait four {
  type A = Value
  type Name = String
  

  /** 
   * Output monad.
   *
   * type O a = (String, a)
   * 
   * unitO a = ("", a)
   * m 'bindO' k = let (r,a) = m
   *                   (s,b) = k a
   *               in (r++s, b)
   *
   * 
   */
  /*
     trait Monad[A] {
       def bindM[B](f: A => Monad[B]):Monad[B]
       // def flatMap[B](f: A => Monad[B]):Monad[B] = bindM(f)
       def map[B](f: A => B):Monad[B]
     }
  */
  
  case class OMonad[A](output:String, value:A) {
    def bindM[B](k: A => OMonad[B]):OMonad[B] = {
      val b = k(value)
      OMonad(output + b.output, b.value)
    }
    def flatMap[B](f: A => OMonad[B]):OMonad[B] = bindM(f)
    def map[B](f: A => B):OMonad[B] = {
      bindM{ a =>
        val b = f(a)
        unitM(b)
      }
    }
    def showM : String = "Output: %s, Value: %s".format(output,value.toString)
    
  }
  def unitM[T](a: T) : OMonad[T] = OMonad[T]("",a)
  

  /**
   * 項 term
   */ 
  sealed trait Term
  case class Var(x: Name) extends Term
  case class Con(n: Int) extends Term
  case class Add(l: Term, r: Term) extends Term
  case class Lam(x: Name, body: Term) extends Term
  case class App(fun: Term, arg: Term) extends Term
  case class Out(arg: Term) extends Term

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
  case class Fun(f: Value => OMonad[Value]) extends Value {
    override def toString() = "<function>"
  }

  /**
   * 評価器 interpreter
   */ 
  type Environment = List[Pair[Name, Value]]

  def interp(t: Term, e: Environment): OMonad[Value] = t match {
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
    case Out(u) => for {
      a <- interp(u, e)
      _ <- out(a)
    } yield a
  }

  def lookup(x: Name, e: Environment): OMonad[Value] = e match {
    case List() => unitM(Wrong)
    case Pair(y, b) :: e1 => {
      if (x == y)
        unitM(b)
      else
        lookup(x, e1)
    }
  }

  def add(a: Value, b: Value): OMonad[Value] = Pair(a, b) match {
    case Pair(Num(m), Num(n)) => unitM(Num(m + n))
    case _ => unitM(Wrong)
  }
  
  def apply(a: Value, b: Value): OMonad[Value] = a match {
    case Fun(k) => k(b)
    case _ => unitM(Wrong)
  }
  
  def out(a: A) : OMonad[A] = OMonad[A]("%s; ".format(a),a)
  
  def test(t: Term): String = {
    interp(t, List()).showM
  }
}

