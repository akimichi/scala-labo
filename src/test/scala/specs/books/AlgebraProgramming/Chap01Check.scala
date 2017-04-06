package AlgebraProgramming

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import org.scalacheck.{Gen}
import org.scalacheck.Prop.forAll


/*
 * Chapter 01
 * 
 * 
 */ 
class Chap01Check extends FunSpec with ShouldMatchers with GeneratorDrivenPropertyChecks {

  describe("1.4"){
    sealed abstract class Tree[+A] {
      def foldt[B] : (A=>B,(B,B)=>B) => B
      def map[B](f:A=>B) = {
        val ff : A => Tip[B] = {(a:A) => Tip(f(a)) }
        val gg : (Tree[B],Tree[B]) => Tree[B] = {(a:Tree[B],b:Tree[B]) => Bin(a,b) }
        foldt(ff,gg)
        // val ff : A => Tip[B] = {(a:A) =>
        //   Tip(f(a))
        // }
        // val gg : (Tree[B],Tree[B]) => Tree[B] = {(a:Tree[B],b:Tree[B]) =>
        //   Bin(a,b)
        // }
        // foldt(ff, gg)
      }
      def size : Int = {
        val one : A => Int = {a:A => 1}
        val plus = (a:Int, b:Int) => a + b
        foldt(one,plus)
      }
      def depth : Int = {
        val zero : A => Int = {a:A => 0}
        // val succ : Int => Int = (a:Int) => a + 1
        // val bmax : (Int,Int) => Int = (a,b) => {
        //   if(a <= b) b
        //   else a
        // }
        val g : (Int,Int) => Int = (a,b) => {
          if(a <= b) b + 1
          else a + 1
        }
        foldt(zero, g)
      }
    }
    case class Tip[A](value:A) extends Tree[A] {
      def foldt[B] : (A=>B,(B,B)=>B) => B = { (f:A=>B,g:(B,B)=>B) => 
        f(value)
      }
    }
    case class Bin[A](left:Tree[A], right:Tree[A]) extends Tree[A] {
      def foldt[B] : (A=>B,(B,B)=>B) => B = { (f:A=>B,g:(B,B)=>B) =>
        g(left.foldt(f,g) , right.foldt(f,g))
      }
    }
    // def foldt[A,B] : (A=>B,(B,B)=>B) => Tree[A] => B = { (f:A=>B,g:(B,B)=>B) => { tree:Tree[A] => 
    //   tree match {
    //     case Tip(a) => f(a)
    //     case Bin(x,y) => g(foldt(f,g) x, foldt(f,g) y)
    //   }
    // }}
    

    it("Treeを生成する"){
      val tree = Bin(Tip(0), Bin(Tip(1),Tip(2)))
      tree.left should equal(Tip(0))
    }
    it("mapでTreeを変換する"){
      val tree = Bin(Tip(0), Bin(Tip(1),Tip(2)))
      val converted = tree.map {a:Int =>
        a.toString
      }
      converted should equal(Bin(Tip("0"),Bin(Tip("1"),Tip("2"))))
    }
    it("size"){
      val tree = Bin(Tip(0), Bin(Tip(1),Tip(2)))
      tree.size should equal(3)
    }
    it("depth"){
      val tree = Bin(Tip(0), Bin(Tip(1),Tip(2)))
      tree.depth should equal(2)
    }
    
  }

}


