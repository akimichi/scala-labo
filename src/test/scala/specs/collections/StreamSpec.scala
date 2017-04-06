import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Streamの基本的な用法を示す
 */
class StreamSpec extends FunSpec with ShouldMatchers {
  describe("consでStreamを生成する"){
    def anIntegerBetween(start:Int, end: Int): Stream[Int] = {
      if (start > end)
        Stream.Empty
      else {
        Stream.cons(start, anIntegerBetween(start + 1, end))
      }
    }
    anIntegerBetween(1,100).size should equal(100)
  }
  describe("Streamに対して") {
    val stream:Stream[Int] = 1 #:: 2 #:: 3 #:: 4 #:: 5 #:: Stream.empty
    it("Stream.applyで要素を取得する"){
      stream(1) should equal(2)
    }
    it("Stream.take"){
      stream.take(2) should equal(1 #:: 2 #:: Stream.empty)
      stream.take(1).head should equal(1)
    }
    it("Stream.force"){
      stream.force should equal(1 #:: 2 #:: 3 #:: 4 #:: 5 #:: Stream.empty)
    }
    it("Stream.indices: Range "){
      stream.indices should equal(0 to 4)
    }
    it("Stream#init は末尾以前を返す"){
      stream.init should equal(1 #:: 2 #:: 3 #:: 4 #:: Stream.empty)
    }
    it("Stream#last は末尾の値を返す"){
      stream.last should equal(5)
    }
    it("Stream.mkString"){
      stream.mkString should equal("12345")
      stream.mkString(",") should equal("1,2,3,4,5")
    }
    it("Stream.find"){
      stream.find(_ == 3) should equal(Some(3))
    }
    it("Stream.contains(elem: Any): Boolean "){
      stream.contains(3) should equal(true)
    }
    it("Stream.countで条件に合致した要素の個数を調べる"){
      stream.count(_ == 3) should equal(1)
    }
    it("for式で Stream を処理する"){
      val result = for {
        elem <- stream
      } yield {
        elem * 2
      }
      result.head should equal(2)
    }
    it("Stream.collect[B](pf: PartialFunction[A, B]): Stream[B] で条件に合致した要素から新しいStreamを構築する"){
      stream.collect {
        case n:Int if n % 2 == 0 => n
      }.toList should equal{
        List(2,4)
      }
    }
  }
  describe("フィボナッチ fibonacci "){
    it("素朴な実装"){
      def fib(end: Int, n: Int = 0, fn1: Int = 0, fn2: Int = 0): Stream[Int] =
        if (n > end)
          Stream.Empty
        else {
          val fn = if(n <= 1)
                      n
                   else
                     fn1 + fn2
          Stream.cons(fn, fib(end, n + 1, fn, fn1))
        }
      fib(10000).size should equal(10001)
      fib(10).toList should equal(List(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55))
      fib(10).take(10).foreach {println}
      println(fib(10).take(10).toList)
    }
    it("簡潔な実装"){
      lazy val fibs: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map(n => {
          n._1 + n._2
        })
      fibs.take(5).last should equal(3)
    }
  }
  it("cycleを実装する"){
    object test {
      def cycle[T](a:Iterable[T]) = Stream.continually(a).flatMap(v=>v)
    }
    import test._
    cycle(1 to 3) take(5) should equal(1 #:: 2 #:: 3 #:: 1 #:: 2 #:: Stream.empty)
  }
}
