import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }

/*
 * Types and Programming Languages
 * 
 */ 
// class UntypedSpec extends FunSpec with ShouldMatchers with UntypedLambda {
//   import evaluator._
//   describe("lambda") {
//     it("terms"){
//       Lam(Var("x"), App(Succ(),Var("x"))).e should equal{
//         App(Succ(),Var("x"))
//       }
//     }

//     it("eval"){
//       eval(App(Succ(),Num(1)),emptyEnv) should equal(2)
//       eval(Cond(True,Num(1),Num(2)),emptyEnv) should equal(1)
//       eval(App[Int,Int](Lam(Var("x"),
// 							App(Succ(),Var("x"))),Num(1)),emptyEnv) should equal(2)
//       eval(App[Int,Int](Lam(Var("x"), App(Succ(),Var("x"))),Num(1)),emptyEnv.extend(Var("y"), Num(2))) should equal(2)
//     }
//     it("id"){
//       type T = Int
//       val id = Lam[T,T](Var("x"), Var("x"))
//       val app = App(id,Num(1))
//       eval(app,emptyEnv) should equal{
//         1
//       }
//     }

//     it("arithmetic"){
//       /*
//        *  (λx.(λy.x*y))(2)(3)
//        */ 
//       type T = Int
//       val app = App[T,T](
//                     App(Lam(Var("x"),
//                             Lam(Var("y"),
//                                 Times(Var("x"),Var("y")))),Num(2)),Num(3))
//       eval(app,emptyEnv) should equal{
//         6
//       }
//     }
//     describe("Church boolean"){
//       // tru = λt.λf.t
//       val id = Lam(Var("x"), Var("x"))
//       val tru = Lam(Var("t"),
//                     Lam(Var("f"),Var("t")))
//       // test = λl.(λm.(λn. ((l m) n)))
//       /*
//       val test = Lam(Var("l"),
//                      Lam(Var("m"),
//                          Lam(Var("n"),
//                              App(
//                                App[Boolean,Int](Var("l"),
//                                                 Var("m")),
//                                Var("n")))))
//       */
//       // val tru = Lam[Boolean,Boolean](Var("t"),
//       //                                Lam(Var("f"),App(id,Var("t"))))
//     }
//     // it("fixed point"){
//     //   type T = Int
//     //   val square = Lam[T,T](Var("x"), Var("x"))
//     //   val app = App(id,Num(1))
//     //   eval(app,emptyEnv) should equal{
//     //     1
//     //   }
//     // }
//   }
// }

    

