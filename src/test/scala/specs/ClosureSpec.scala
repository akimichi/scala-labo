import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * closure の基本的な用法を示す
 */
class ClosureSpec extends FunSpec with ShouldMatchers {
  describe("object closure") {
    object test {
      var counter = 0
      val count: () => Int = {
        () => {
          counter = counter + 1
          counter
        }
      }
    }
    import test._
    count() should equal(1)
    count() should equal(2)
  }
  describe("trait closure") {
    it("immutable"){
      trait test {
	val seq:Seq[Int]
	val init:Int
	def compute(f:(Int,Int) => Int) = {
	  seq.foldLeft(init)(f)
	}
      }
      val computer = new test {
	val seq = Seq(1,2,3)
	val init = 0
      }
      val adder = (a:Int,b:Int) => a + b
      computer.compute(adder) should equal(6)
    }
    it("mutable"){
      trait test {
	var _counter = 0
	val count: () => Int = {
          () => {
            _counter = _counter + 1
            _counter
          }
	}
      }
      val counter = new test {}
      counter.count() should equal(1)
      counter.count() should equal(2)
      val another_counter = new test {}
      another_counter.count() should equal(1)
    }
  }
  describe("closure inside a function") {
    it("内部に可変な状態を持つクロージャは参照透明性を欠く"){
      object namespace {
        val count:() => Int  = {
          var counter = 0
          () => {
            counter = counter + 1
            counter
          }
        }
      }
      import namespace._
      count() should equal(1)
      count() should equal(2)
    }
    it("メソッドの場合(1)"){
      object namespace {
        def count:() => Int  = {
          var counter = 0
          () => {
            counter = counter + 1
            counter
          }
        }
      }
      import namespace._
      count() should equal(1)
      count() should equal(1)
    }
    it("メソッドの場合は、"){
      object namespace {
        var counter = 0
        def count:() => Int  = {
          () => {
            counter = counter + 1
            counter
          }
        }
      }
      import namespace._
      count() should equal(1)
      count() should equal(2)
    }
  }
}



