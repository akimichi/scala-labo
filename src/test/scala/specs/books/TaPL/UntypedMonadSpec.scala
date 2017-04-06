import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }

/*
 * Types and Programming Languages
 * 
 */ 
// class UntypedMonadSpec extends FunSpec with ShouldMatchers with UntypedMonad {
//   import evaluator._
  
//   describe("lambda") {
//     it("terms"){
//       Lam(Var("x"), Var("x")) should equal{
//         Lam(Var("x"), Var("x"))
//       }
//       // Lam(Var("x"), App(Succ(),Var("x"))).e should equal{
//       //   App(Succ(),Var("x"))
//       // }
//     }
//     /*
//     it("eval"){
//       eval(App(Succ(Num(1)),emptyEnv) should equal(2)
//       // eval(Cond(True,Num(1),Num(2)),emptyEnv) should equal(1)
//       // eval(App[Int,Int](Lam(Var("x"), App(Succ(),Var("x"))),Num(1)),emptyEnv) should equal(2)
//       // eval(App[Int,Int](Lam(Var("x"), App(Succ(),Var("x"))),Num(1)),emptyEnv.extend(Var("y"), Num(2))) should equal(2)
//     }
//     */ 
//     describe("eval"){
//       it("IsZero"){
//         eval(IsZero(Num(0)),emptyEnv) should equal(Success(True))
//         eval(IsZero(Num(1)),emptyEnv) should equal(Success(False))
//       }
//     }
// 	/*
//     it("Y combinator"){
//       /*
//        * Y = λf.((λx.f (λy. (x x) y))
//        *         (λx.f (λy. (x x) y)))
//        */
//       val Y = Lam(Var("f"),
//                   App(
//                     Lam(Var("x"),
//                         App(Var("f"),
//                             Lam(Var("y"),
//                                 App(
//                                   App(Var("x"),Var("x")),
//                                   Var("y"))))),
//                     Lam(Var("x"),
//                         App(Var("f"),
//                             Lam(Var("y"),
//                                 App(
//                                   App(Var("x"),Var("x")),
//                                   Var("y")))))))
//       /*
//        * fact = λf.λn.if (n = 0) 1 (n * f(n - 1))
//        */ 
//       val fact = Lam(Var("f"),
//                      Lam(Var("n"),
//                          Cond(IsZero(Var("n")),
//                               Num(1),
//                               (Mul(Var("n"),
//                                      App(Var("f"),
//                                          Sub(Var("n"),
//                                              Num(1))))))))
//       eval(App(
//             Lam(Var("f"),
//                 App(Var("f"),Num(1))),
//             App(
//               Y,
//               fact)),emptyEnv) should equal(Success(Num(3)))
//     }
// 	*/ 
//     /*
//     it("Y combinator"){
//       /*
//        * Y = λf.((λx.f (λy. (x x) y))
//        *         (λx.f (λy. (x x) y)))
//        * Y = λf.((λx.f (x x)) (λx.f (x x)))
//        */ 
//       val Y = Lam(Var("f"),
//                   App(
//                     Lam(Var("x"),
//                         App(Var("f"),
//                             App(Var("x"),Var("x")))),
//                     Lam(Var("x"),
//                         App(Var("f"),
//                             App(Var("x"),Var("x"))))))
//       /*
//        * fact = λf.λn.if (n = 0) 1 (n * f(n - 1))
//        */ 
//       val fact = Lam(Var("f"),
//                      Lam(Var("n"),
//                          Cond(IsZero(Var("n")),
//                               Num(1),
//                               (Mul(Var("n"),
//                                      App(Var("f"),
//                                          Sub(Var("n"),
//                                              Num(1))))))))
//       eval(App(
//             Lam(Var("f"),
//                 App(Var("f"),Num(0))),
//             App(
//               Y,
//               fact)),emptyEnv) should equal(Success(Num(3)))
           
                           
//     }
//     */ 

//     it("Church boolean"){
//       val tru = Lam(Var("t"),
//                     Lam(Var("f"),Var("t")))
//       val fls = Lam(Var("t"),
//                     Lam(Var("f"),Var("f")))
//       val conditional = Lam(Var("l"),
//                             Lam(Var("m"),
//                                 Lam(Var("n"),
//                                     App(
//                                       App(Var("l"),
//                                           Var("m")),
//                                       Var("n")))))
//       eval(App(App(App(conditional, tru),
//                    Num(1)),
//                Num(2))) should equal(Success(Num(1)))
//       eval(App(App(App(conditional, fls),
//                    Num(1)),
//                Num(2))) should equal(Success(Num(2)))
      
//     }
//   }
// }
