import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach, BeforeAndAfter }

/**
 * Monadの基本的な用法を示す
 *
 * (M, unitM, bindM)
 * unitM :: a -> M a
 * bindM :: M a -> (a -> M b) -> M b
 *
 * left unit: 単位元律
 *   (unitM a) 'bindM' k = k a
 * right unit: 同一律
 *   m 'bindM' unitM = m
 * associativity: 結合律
 *   m 'bindM' (\a -> ((k a) 'bindM' h)) = (m 'bindM' (\a -> k a)) 'bindM' h
 *
 * joinM :: M (M a) -> M a
 * mapM :: (a -> b) -> (M a -> M b)
 * 
 */
  
class MonadSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("ListはMonadである") {
    
  }
  describe("Option型でMonad規則を確認する") {
    it("同一律 m flatMap unit => m を確認する"){
      val unit = (value:Int) => Some(value)
      Some(1) flatMap { x => Some(x)} should equal(Some(1))
      Some(1) flatMap unit should equal(Some(1))
      None flatMap { x => None} should equal(None)
    }
    it("単位元律 unit(v) flatMap f => f(v) を確認する"){
      val func = (value:Int) => Some(value)
      Some(1) flatMap { x => func(x)} should equal(Some(1))
      Some(1) flatMap func should equal(Some(1))
      Some(1) flatMap func should equal(func(1))
      None flatMap { x => func(x)} should equal(None)
    }
    it("結合律 m flatMap g flatMap h => m flatMap { x => g(x) flatMap h} を確認する"){
      val g = (value:Int) => Some(value)
      val h = (value:Int) => None
      val m = Some(1)
      m flatMap g flatMap h should equal(m flatMap { x => g(x) flatMap h})
    }
  }
  describe("ConfigクラスでMonad規則を確認する") {
    object test {
      case class Config[+A](value:A) {
        def map[B](f:A => B) : Config[B] = Config(f(value))
        def flatMap[B](f:A => Config[B]) : Config[B] = f(value)
//        def foreach(f:A => Unit): Unit = map { x => f(x) } // yield を使うfor文では不要
      }
      val unit = (value:Int) => Config(value)
    }
    it("同一律 m flatMap unit => m を確認する"){
      import test._
      Config(1) flatMap { x => Config(x)} should equal(Config(1))
      Config(1) flatMap unit should equal(Config(1))
    }
    it("単位元律 unit(v) flatMap f => f(v) を確認する"){
      import test._
      val func = (value:Int) => Config(value)
      Config(1) flatMap { x => func(x)} should equal(Config(1))
      unit(1) flatMap func should equal(func(1))
    }
    it("結合律 m flatMap g flatMap h => m flatMap { x => g(x) flatMap h} を確認する"){
      import test._
      val g = (value:Int) => Config(value)
      val h = (value:Int) => Config(-value)
      val m = Config(1)
      m flatMap g flatMap h should equal(
        m flatMap { x => g(x) flatMap h}
      )
    }
    it("mapを利用する"){
      import test._
      Config(1) map { x => x.toString} should equal(Config("1"))
    }
    it("flatMapを利用する"){
      import test._
      Config(1) flatMap { x => Config(x.toString)} should equal(Config("1"))
    }
    it("for文を利用する"){
      import test._
      val config = for{
        v <- Config(2)
      } yield {
        v * 3  // should equal(1)
      }
      config should equal(Config(6))
    }
   }
  
}


