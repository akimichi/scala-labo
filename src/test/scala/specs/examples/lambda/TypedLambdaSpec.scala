import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


// class TypedLambdaSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll with lambda.typedLambda {
//   describe("Lambda評価器は") {
//     it("interpメソッドで式を評価できる"){
//       val term0 = App(Lam(Var("x"), Add(Var("x"), Var("x"))), Add(Const(10), Const(11)))
//       interp(term0,emptyEnv).value should equal{
//         Num(42)
//       }
//     //   interp(App(Succ(),Const(1)),emptyEnv) should equal(2)
//     //   interp(App(Lam(Var[Int]("x"),App(Succ(),Var[Int]("x"))),
//     //            Const(1)), emptyEnv) should equal(2)
//     }
// 	/*
//     it("""Add(Out (Con(41)), (Out (Con(1))))で Output を検証する"""){
//       val term0 = Add(Out (Const(41)), (Out (Const(1))))
//       interp(term0, emptyEnv).output should equal("Num(41); Num(1); ")        
//     }
//     it("""Add(Con(1), Ccc("k", Add(Con(2), App(Var("k"), Con(4)))))"""){
//       val term = Add(Const(1), CCC("k",
//                                    Add(Const(2), App[Int,Unit](Var("k"), Const(4)))))
//       interp(term,emptyEnv).value should equal{
//         Num(42) // should be Num(5)
//       }
//       // showM(interp(term,emptyEnv)) should equal{
//       //   Num(42) // should be Num(5)
//       // }
//     }
// 	*/
//   }
// }

/* original


trait Term[a]
class Var[a] (val name: String) extends Term[a]
class Num (val value: int) extends Term[int]
class Lam[b, c] (val x: Var[b], val e: Term[c]) extends Term[b ⇒ c]
class App[b, c] (val f: Term[b ⇒ c], val e: Term[b])extends Term[c]
class Suc () extends Term[int ⇒ int]
// Environments:
abstract class Env {
def apply[a](v: Var[a]): a
def extend[a](v: Var[a], x: a) = new Env {
def apply[b](w: Var[b]): b = w match {
case : v.type ⇒ x // v eq w, hence a = b
case ⇒ Env.this.apply(w)
}}}
object empty extends Env {
def apply[a](x: Var[a]): a = throw new Error(”not found: ”+x.name) }
// Evaluation:
def eval[a](t: Term[a], env: Env): a = t match {
case v: Var[b] ⇒ env(v) // a = b
case n: Num ⇒ n.value // a = int
case i: Suc ⇒ { y: int ⇒ y + 1 } // a = int⇒int
case f: Lam[b, c]⇒ { y: b ⇒ eval(f.e, env.extend(f.x, y// a = b )) } ⇒c
case a: App[b, c]⇒ eval(a.f, env)(eval(a.e, env)) // a = c
}


*/
