import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/*
 *
 * 関手（functor） F は2つの圏の構造を保持するマッピング（structure-preserving mapping）である
 *
 */
class FunctorSpec extends FunSpec with ShouldMatchers {
  trait Functor[T[_]] {
    def unit[A] : A => T[A]
    def fmap[A,B] : (A => B) => (T[A] => T[B])

    def id[A](x:A):A = x
    def apply[A](x : A) : T[A] = unit(x)
    def map[A,B](f: A=>B)(x : T[A]) : T[B] = fmap(f)(x)

  }
  describe("Bifunctor"){
    info("c.f. http://tmorris.net/posts/funky-scala-bifunctor/index.html")
    trait Bifunctor[F[+_, +_]] {
      def bimap[A, B, C, D](fa: F[A, B], f: A => C, g: B => D): F[C, D]
    }
    
    object Bifunctor {
      implicit def Tuple2Bifunctor = new Bifunctor[Tuple2] {
          def bimap[A, B, C, D](fa: (A, B), f: A => C, g: B => D) =
            (f(fa._1), g(fa._2))
      }
      
      implicit def EitherBifunctor = new Bifunctor[Either] {
        def bimap[A, B, C, D](fa: Either[A, B], f: A => C, g: B => D) =
          fa match {
            case Left(a) => Left(f(a))
            case Right(b) => Right(g(b))
          }
      }
    }
  }
  describe("type lambda"){
    class MapFunctor[T] extends Functor[({type λ[α] = Map[T,α]})#λ] {
      def unit[A] : A => Map[T,A] = {
        Map.empty
      }
      def fmap[A,B] : (A => B) => (Map[T,A] => Map[T,B]) = (f: A => B) => {
        (ta:Map[T,A]) => ta.mapValues(f)
      }
    }
  }
  
  describe("OptionFunctor型でFunctorの性質を確認する") {
    object OptionFunctor extends Functor[Option] {
      def unit[A] : A => Option[A] = (x:A) => Option(x)
      def fmap[A,B] : (A => B) => (Option[A] => Option[B]) = (f: A => B) => {
        (oa:Option[A]) => oa.map{f(_)}
      }
      def join[A] : Option[Option[A]] => Option[A] = (ooa:Option[Option[A]]) => {
        ooa.flatMap{ oa:Option[A] => oa}
      }
    }
    it("OptionFunctor を使う"){
      val to_s = (x: Int) => x.toString
      val oa:Option[Int] = Some(1)
      OptionFunctor.map(to_s)(oa) should equal {
        Some("1")
      }
      OptionFunctor.map(to_s)(None) should equal {
        None
      }
    }
    
  }
  describe("ListFunctor型でFunctorの性質を確認する") {
    object ListFunctor extends Functor[List] {
      def unit[A] : A => List[A] = (x:A) => List(x)
      def fmap[A,B] : (A => B) => (List[A] => List[B]) = (f: A => B) => {
        (xs:List[A]) => xs.map{f(_)}
      }
      def join[A] : List[List[A]] => List[A] = (xss:List[List[A]]) => {
        xss.flatMap{ xs:List[A] => xs}
      }
      def rev[A] = (listA: List[A]) => listA.reverse
     }
    it("ListFunctor.map を確認する"){
      val sqrt = (x: Int) => x * x
      val xs:List[Int] = List(1,2,3,4,5)
      ListFunctor.map(sqrt)(xs) should equal {
        List(1, 4, 9, 16, 25)
      }
    }
    describe("Basic Category Theory for Computer Scientists,p.42"){
      val xs:List[Int] = List(5,6,7)
      val f = (i:Int) => i.toString
      ListFunctor.map(f)(ListFunctor.rev(xs)) should equal {
        List("7", "6", "5")
      }
      /*
       *   List[Int]-------(rev[Int])--------> List[Int]
       *     |                                   |
       *     |                                   |
       *    map(f)                             map(f)
       *     |                                   |
       *     |                                   |
       *   List[String]----(rev[String])-----> List[String]
       *
       *  f: Int => String
       * 
       */

      it("rev は natural transformation であるから、 rev . maplist(f) == maplist(f) . rev を満す"){
        ((ListFunctor.rev) compose ListFunctor.map(f) _)(xs) should equal {
          (ListFunctor.map(f) _ compose (ListFunctor.rev[Int]))(xs)
        }
      }

    }
  }
  describe("'Scala in Depth',p.258") {
    object test {
      trait Config[+A] {
        def map[B](f:A => B) : Config[B] = Config(f(get))
        def flatMap[B](f:A => Config[B]) : Config[B] = f(get)
        def get : A
      }
      object Config {
        def apply[A](data : => A) = new Config[A] {
          def get = data
        }
      }

      trait Functor[F[_]] {
        def map[A,B](fa:F[A])(f:A=>B):F[B]
      }
    }
  }
}
