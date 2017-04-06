import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


class SeqSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Seqに対して") {
    val seq = Seq(1,2,3)
    it("+: 演算子でSeqを生成する"){
      (0 +: seq) should equal{
        Seq(0,1,2,3)
      }
    }
    it("Seq#find(p: (A) ⇒ Boolean): Option[A] を使う") {
      seq.find { item => item % 2 == 0 } should equal(Some(2))
    }
    it("Seq#max") {
      seq.max should equal(3)
    }
    it("Seq#maxBy") {
      Seq(Pair('first, 1),Pair('middle,2),Pair('last, 3)).maxBy{pair =>
	pair._2} should equal(Pair('last,3))
    }
    
    it("mkStringで文字列を作成する") {
      seq.mkString  should equal("123")
      seq.mkString(",")  should equal("1,2,3")
    }
    it("mapで要素を変換する"){
      val serializedEntries = seq map(_.toString)
      "[ " + (serializedEntries mkString ", ") + " ]" should equal("[ 1, 2, 3 ]")
      seq.size should equal(3)
    }
    it("添字を指定して値を取り出す"){
      seq(1) should equal(2)
    }
    it("Seq#contains"){
      seq.contains(3) should equal(true)
    }
    it("Seq.flattenでNoneを消す"){
      val seq = Seq(Some(1),None,Some(3),None)
      seq.flatten should equal(List(1,3))
    }
  }
}



