import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import org.scalacheck.{Gen}
import org.scalacheck.Prop.forAll

/*
 * 継続モナド continuation monad
 *
 * c.f. http://lampwww.epfl.ch/~emir/bqbase/2005/01/20/monad.html
 *
 */


class ContinuationMonadSpec extends FunSpec with ShouldMatchers {
  /** a class for "monadic artifacts" */
  case class M[+A](in: (A=>Any)=>Any) {
    def map[B](f: A => B)        = bind(this, {x: A => unit(f(x))});
    def flatMap[B](f: A => M[B]) = bind(this, f);
  }
  
  def unit[A](x:A) = {
    val in = {(k:(A=>Any)) =>
      k(x)
    }
    M(in)
  }
  
  def bind[A,B](m:M[A], f:A=>M[B]):M[B] = M(k => m.in (x => f(x).in(k)))

  def callCC[A](e:(A=>M[A])=>M[A]):M[A] = M(k => e(a => M(ignored => k(a))).in(k));

  // Michel's Explanation:

  // The argument given to "e" must be a reified continuation, i.e.
  // a function which, when invoked with some parameter, replaces
  // the current continuation with the one of the call to callCC. In
  // the code above, "ignored" is the current continuation, and "k"
  // is the one of the call to callCC. Finally, "a" is the value
  // which will be "returned" by the call to callCC.

  // We also have to give a continuation to the whole callCC
  // expression, because it is perfectly possible that the reified
  // continuation is never invoked. This continuation is "k", quite
  // naturally. We therefore pass it to e's "input".
  
  // the "final continuation"
  def show[A]():(A)=>Unit = {x:A => Console.println(x)}

  // convenience
  def show[A](m:M[A]):Unit = { m.in (show()) }

  // convenience^2
  def showshow[A](m:M[M[A]]):Unit = { m.in (k => k.in (show())) }

  def reverse(s:String):String = {
    val sb = new StringBuffer();
    var i = s.length(); while(i > 0) {
      i = i - 1;
      sb.append(s.charAt(i));
    }
    sb.toString();
  }

  
  describe("Continuation Monadについて"){
  }
}
