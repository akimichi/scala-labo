

// trait UntypedLambda {
//   trait env extends terms {
//     // Environments:
//     abstract class Env {
//       def apply[a](v : Var[a]): a
//       def extend[a](v : Var[a], x : a) = new Env {
//         def apply[b](w: Var[b]): b = w match {
//           case _: v.type => x // v eq w, hence a = b
//           case _ =>  Env.this.apply(w)
//         }}}
//     object emptyEnv extends Env {
//       def apply[a](x : Var[a]): a = throw new Error("not found") 
//     }
//   }
//   trait evaluator extends env with terms {
//     def eval[a](t : Term[a], env : Env): a
//   }
//   object evaluator extends evaluator {
//     // Evaluation:
//     def eval[a](t : Term[a], env : Env): a = t match {
//       case v : Var[b] => env(v) // a = b
//       case bool : Bool => bool.value
//       case n : Num => n.value // a = Int
//       case i : Succ => { y : Int => y + 1 } // a = Int=>Int
//       case f : Lam[b, c] => { y : b => eval(f.e, env.extend(f.x, y)) } // a = b=>c
//       case a : App[b, c] => eval(a.f, env)(eval(a.e, env)) // a = c
//       case cond : Cond[b,c] => {
//         eval(cond.pred,env) match {
//           case value:Boolean => {
//             if(value)
//               eval(cond.tru,env)
//             else
//               eval(cond.fls,env)
//           }
//           case otherwise => {
//             throw new Exception
//           }
//         }
//       }
//       case cons : Cons[a] => {x:a => { xs:List[a] =>
//         x :: xs
//       }}
//       case times : Times[b,c] => {
//         // l * r
//         eval(times.l,env) match {
//           case lvalue:Int => {
//             eval(times.r,env) match {
//               case rvalue:Int => lvalue * rvalue
//               case _ => throw new Exception
//             }
//           }
//           case otherwise => throw new Exception
//         }
//       }
//       case and : And[b,c] => {l:b => {r:c =>
//         eval(and.l,env) match {
//           case lvalue:Boolean => {
//             eval(and.r,env) match {
//               case rvalue:Boolean => lvalue && rvalue
//               case _ => throw new Exception
//             }
//           }
//           case otherwise => throw new Exception
//         }
//       }}
//     }
//   }
//   // Terms:
//   trait terms {
//     trait Term[T] {
//       def fv():Set[String]
//       def isClosed:Boolean = fv().isEmpty
//     }
//     case class Var[a] (val name : String) extends Term[a] {
//       def fv():Set[String] = Set(name)
//     }
//     case class Lam[b, c] (x : Var[b],e: Term[c]) extends Term[b => c] {
//       def fv():Set[String] = e.fv() - x.name
//     }

//     case class App[b, c] (val f : Term[b => c], val e : Term[b]) extends Term[c] {
// //    case class App[b, c] (val f : Term[b => c], val e : Term[b]) extends Term[b => c] {
// //    case class App[a,b,c] (val f : Term[a], val e : Term[b]) extends Term[c] {
//       def fv():Set[String] = f.fv() ++ e.fv()
//     }
//     case class Cond[b,c] (val pred:Term[b], val tru:Term[c], val fls:Term[c] ) extends Term[c] {
//       def fv():Set[String] = Set.empty[String]
//     }

//     trait Primitive[T] extends Term[T] {
//       def fv():Set[String] = Set.empty[String]
//     }
//     class Bool(val value : Boolean) extends Primitive[Boolean]
//     case object True extends Bool(true)
//     case object False extends Bool(false)
//     case class Num(val value : Int) extends Primitive[Int]
//     case class Succ() extends Term[Int => Int] {
//       def fv():Set[String] = Set.empty[String]
//     }
//     // case class Times() extends Term[Int => Int => Int]
//     case class Times[b,c](l:Term[b], r:Term[c]) extends Term[Int] {
//       def fv():Set[String] = Set.empty[String]
//     }
//     case class Cons[a]() extends Term[a => List[a] => List[a]] {
//       def fv():Set[String] = Set.empty[String]
//     }
//     // case class Cons[a](val head:a, val tail:List[a]) extends Term[a => List[a] => List[a]]
    
//     // case class True[b,c](val t:Term[b], val f:Term[c]) extends Term[b => c => b]
//     // case class False[b,c](val t:Term[b], val f:Term[c]) extends Term[b => c => c]
//     // case class And[b,c,d](val l:Term[b], val r:Term[c]) extends Term[b => c => d] /** λb.λc.((b c) fls) */
//     // case class Not() extends Term[Bool => Bool]
//     /** λb.λc.((b c) fls) */
//     case class And[b,c](l:Term[b], r:Term[c]) extends Term[b => c => Boolean] {
//       def fv():Set[String] = Set.empty[String]
//     }
//     // case class And[b,c]() extends Term[b => c => Boolean] /** λb.λc.((b c) fls) */

//     object Term {
//       def fv[T : Term]():Set[String] = implicitly[Term[T]].fv()
//       // implicit object VarTerm extends Term[Int] {
//       //   def fv(t:Int):String = t.toString
//       // }
//     }
//   }



// }
