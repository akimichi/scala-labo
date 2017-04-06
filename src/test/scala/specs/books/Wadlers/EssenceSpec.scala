import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }

/*
 * The essence of functional programming
 * 
 */ 
class EssenceSpec extends FunSpec with ShouldMatchers  {
  
  describe("zero") {
    import wadler.zero._
    it("ゼロ評価器で評価する"){
      val term0 = App(Lam("x", Add(Var("x"), Var("x"))), Add(Con(10), Con(11)))
      test(term0) should equal("42")
      interp(term0, Nil) should equal(M(Num(42)))
    }
  }
  describe("one") {
    import wadler.one
    it("ワン評価器で評価する"){
      object OneInterpreter extends one
      import OneInterpreter._
      val term0 = App(Lam("x", Add(Var("x"), Var("x"))), Add(Con(10), Con(11)))
      test(term0) should equal("42")
      test(App(Con(10), Con(11))) should equal("should be a function")
    }
    it("Church boolean"){
      object OneInterpreter extends one
      import OneInterpreter._
      val tru = Lam("t",
                    Lam("f",Var("t")))
      val fls = Lam("t",
                    Lam("f",Var("f")))
      // test = λl.(λm.(λn. ((l m) n)))
      val conditional = Lam("l",
                            Lam("m",
                                Lam("n",
                                    App(
                                      App(Var("l"),
                                          Var("m")),
                                      Var("n")))))
      test(App(App(App(conditional, tru),
                   Con(1)),
               Con(2))) should equal("1")
      test(App(App(App(conditional, fls),
                   Con(1)),
               Con(2))) should equal("2")
              
    }
  }
  describe("three") {
    
    describe("スリー評価器で評価する"){
      object ThreeInterpreter extends wadler.three {
        // case class State(content:Int) {
        //   def init = State(Num(0))
        //   def succ(state:State) = State(Num(state.content + 1))
        // }
        // type S = State
        // type S = Int
      }
      import ThreeInterpreter._
      
      it("""App(Lam("x", Add(Var("x"), Var("x"))), Add(Con(10), Con(11)))"""){
        val term0 = App(Lam("x", Add(Var("x"), Var("x"))), Add(Con(10), Con(11)))
        interp(term0,Nil).run(Num(0)) match {
          case (state,answer) => {
            answer should equal(Num(42))
            state should equal(Num(1))
          }
        }
      }
      it("""App(Lam("x", Add(Var("x"), Var("x"))), App(Lam("y", Var("y")),Con(10)))"""){
        val term0 = App(Lam("x", Add(Var("x"), Var("x"))), App(Lam("y", Var("y")),Con(10)))
        interp(term0,Nil).run(Num(0)) match {
          case (state,answer) => {
            answer should equal(Num(20))
            state should equal(Num(2))
          }
        }
        // interp(term0,Nil).showM(Num(0)) should equal("42")
      }
      // test(term0,Num(0)) should equal("42")
      // test(App(Con(10), Con(11)),Num(0)) should equal("should be a function")
    }
  }
  describe("four") {
    describe("フォー評価器で評価する"){
      object FourInterpreter extends wadler.four
      import FourInterpreter._
      
      it("""Add(Out (Con(41)), (Out (Con(1))))"""){
        val term0 = Add(Out (Con(41)), (Out (Con(1))))
        test(term0) should equal("Output: 41; 1; , Value: 42")        
      }
    }
  }
  describe("five") {
    describe("ファイブ評価器で評価する"){
      object FiveInterpreter extends wadler.five
      import FiveInterpreter._
      
      it("""App(Lam("x", Add(Var("x"), Var("x"))), (Amb (Con(1), Con(2))))"""){
        val term0 = App(Lam("x", Add(Var("x"), Var("x"))), Amb(Stream(Con(1), Con(2))))
        test(term0) should equal("Choices: List(2, 4)")        
      }
      it("""App(Lam("x", Amb(Stream(Var("x"), Add(Var("x"), Con(1))))),Con(1))"""){
        val term0 = App(Lam("x", Amb(Stream(Var("x"), Add(Var("x"), Con(1))))),Con(1))
        test(term0) should equal("Choices: List(1, 2)")        
      }
      it("""an-integer-between"""){
        def anIntegerBetween(start:Int, end: Int): Stream[Con] = {
          if (start > end)
            Stream.empty[Con]
          else {
            Stream.cons(Con(start), anIntegerBetween(start + 1, end))
          }
        }

        val term0 = App(Lam("x", Add(Var("x"), Var("x"))),Amb(anIntegerBetween(1,100)))
        interp(term0, List.empty).choice(2) should equal(Num(6))
      }
    }
  }
}

    



