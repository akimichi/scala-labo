import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Listの基本的な用法を示す
 */
class ListSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("Listに対して") {
    it("List#sizeで長さを取得する") {
      List(1,2,3,4,5,6,7,8,9,10).size should equal(10)
    }
    it("List#takeで先頭から一部を取得する") {
      val list = List(1,2,3,4,5,6,7,8,9,10)
      list.take(2) should equal(List(1,2))
    }
    it("List#dropで先頭から指定された個数分を除去する") {
      val list = List(1,2,3,4,5,6,7,8,9,10)
      list.drop(2) should equal(List(3,4,5,6,7,8,9,10))
    }
    it("List#dropWhileで先頭から条件に合致する分を除去する") {
      List(1,2,3,4,5,6,7,8,9,10).dropWhile{item => 
	item < 3
      } should equal(List(3,4,5,6,7,8,9,10))
      List("one", "two", "three").dropWhile{item =>
	item.startsWith("o")
      } should equal(List("two", "three"))
      "////data".dropWhile{item => item == '/'} should equal("data")
    }
    it("List#splitAtでリストの途中で分割する") {
      List(1,2,3,4,5,1,2).span(_ < 3) should equal {
        (List(1, 2),List(3, 4, 5, 1, 2))
      }
    }
    it("List#reverseで逆順を得る"){
      val list = (1 to 10).toList
      list.reverse should equal {
        List(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
      }
    }
    it("List#foldLeftで集約する") {
      val list = (1 to 10).toList
      val sum = list.foldLeft(0) {(a,b) => a + b}
      sum should equal(55)

      val zipped_list = list.zip(list) // ((1,1),(2,2),(3,3)...)
      zipped_list.foldLeft((0,0)) {(accum,pair) => (accum._1 + pair._1, accum._2 + pair._2)} should equal((55,55))
    }
    it("zip と drop で隣りあわせの要素のペアを持つリストを作る") {
      val list = List(1,2,3,4,5,6,7,8,9,10)
      list drop(1) should equal (List(2,3,4,5,6,7,8,9,10))
      list.zip(list drop(1)) should equal(List((1,2), (2,3), (3,4), (4,5), (5,6), (6,7), (7,8), (8,9), (9,10)))
    }
    it("List#sortedメソッドでListをソートする") {
      val list = List(9,5,7,4,2,6,3,8,1,10)
      list.sorted should equal(List(1,2,3,4,5,6,7,8,9,10))
    }
    it("List#sortWithメソッドでListをソートする") {
      val list = List("20120301","20120101","20120201")
      list.sortWith{(a,b) => a < b } should equal(List("20120101", "20120201", "20120301"))
    }
    it("List#findメソッドはリストを検索して条件に合致する最初の要素を返す") {
      val list = List(1,2,3,4,5,6,7,8,9,10)
      list.find(_ % 2 == 0) should equal(Some(2))
    }
    it("List#containsメソッドはリストに該当する要素があるかどうかを返す") {
      val list = List(1,2,3,4,5,6,7,8,9,10)
      list.contains(1) should equal(true)
    }
    it("List#filterメソッドはリストを検索して条件に合致する最初の要素を返す") {
      val list = List(1,2,3,4,5,6,7,8,9,10)
      list.filter{item => 
	item % 2 == 0
      } should equal(List(2, 4, 6, 8, 10))
    }
    it("List#existsメソッドはリストを検索して条件に合致する最初の要素があるかどうかを調べる") {
      val list = List("foo","bar","gus")
      list.exists(_.startsWith("f")) should equal(true)
    }
    it("List#forallメソッドはリストを検索して全ての要素が条件に合致するかどうかを調べる") {
      val list = List("foo","bar","gus")
      list.forall(_.length == 3) should equal(true)
    }
    describe("Listをmatchする") {
      it("Listについて、個数を指定して match させる"){
        val list = List(1,2,3,4,5,6,7,8,9,10)
        list match {
          case list @ List(one,two,three,_,_,_,_,_,_,_) => {
            one should equal(1)
            list.head should equal(one)
          }
          case Nil => fail()
          case _ => fail()
        }
      }
      it("Listについて、パターンで match させる"){
        List(1,2,3,4,5,6,7,8,9,10) match {
          case head::tail => {
            head should equal(1)
            tail should equal(List(2,3,4,5,6,7,8,9,10))
          }
          case _ => fail()
        }
        List(1) match {
          case head::Nil => {
            head should equal(1)
          }
          case head::tail => {
            fail()
          }
          case _ => fail()
        }
      }
    }
    it("List#distictで一意の要素のみに変換する") {
      List(1,2,3,2,1).distinct should equal(List(1, 2, 3))
      (List(3,2,1,2,3) ::: List(2,3,1,3,1)).distinct should equal(List(3, 2, 1))
    }
    it("valで、immutable なリストに要素を追加する") {
      val list = List(1,2,3)
      val new_list = 0 :: list
      new_list.isInstanceOf[collection.immutable.List[_]] should equal(true)
      new_list(0) should equal(0)
    }
    it("varと::=演算子を用いて、immutable なリストに要素を追加する(c.f. Programming in Scala,sec.17.4)") {
      var list = List(1,2,3)
      list ::= 0
      list.isInstanceOf[collection.immutable.List[_]] should equal(true)
      list(0) should equal(0)
    }
    it("varと:::=演算子を用いて、immutable なリストに要素を追加する(c.f. Programming in Scala,sec.17.4)") {
      var list = List(1,2,3)
      list :::= List(0)
      list.isInstanceOf[collection.immutable.List[_]] should equal(true)
      list(0) should equal(0)
    }
    it("List#collectメソッドでListの一部の要素のみを置換する"){
      val list = List(1,2,3)
      val new_list = list.collect {
        case i if i == 2 => 0
        case j => j
      }
      new_list should equal(List(1,0,3))
    }
    it("List#zipメソッドで2つのListをマージしてtupleを得る"){
      val list1 = List(1,2,3)
      val list2 = List("1","2","3")
      val zipped = list1.zip(list2)
      zipped(0) should equal((1,"1"))
    }
  }
  describe("List[Pair[String,String]]に対して") {
    /* http://www.openehr.org/svn/specification/TAGS/Release-1.0.1/publishing/its/XML-schema/index.html */
    val rm_types:List[Pair[String,String]] = List(("LOCATABLE" -> "CONTENT_ITEM"),
                                                  ("CONTENT_ITEM" -> "ENTRY"),
                                                  ("ENTRY" -> "CARE_ENTRY"),
                                                  ("CARE_ENTRY" -> "ACTION"),
                                                  ("LOCATABLE" -> "ACTIVITY"),
                                                  ("ENTRY" -> "ADMIN_ENTRY"),
                                                  ("CARE_ENTRY" -> "EVALUATION"),
                                                  ("LOCATABLE" -> "GENERIC_ENTRY"),
                                                  ("CARE_ENTRY" -> "INSTRUCTION"),
                                                  ("CARE_ENTRY" -> "OBSERVATION"),
                                                  ("CONTENT_ITEM" -> "SECTION"))
    it("ENTRY は OBSERVATION のスーパータイプである") {
      def find_subtyping(ancestor:String, descendent:String, population:List[Pair[String,String]]):Boolean = {
        if(population.exists{(pair) =>
          pair == (ancestor, descendent)
        })
          true
        else {
          val list = for{
            (supertype, subtype) <- population if supertype == ancestor
          } yield {
            find_subtyping(subtype,descendent,population)
          }
          list.exists((x:Boolean) => x == true )
        }
      }
      find_subtyping("CARE_ENTRY", "OBSERVATION", rm_types) should equal(true)
      find_subtyping("ENTRY", "OBSERVATION", rm_types) should equal(true)
      find_subtyping("LOCATABLE", "INSTRUCTION", rm_types) should equal(true)
      find_subtyping("OBSERVATION", "LOCATABLE", rm_types) should equal(false)
    }
      /*                       
      val answer:List[Pair[String,String]] = for{
        (supertype_as_entry, subtype_of_entry) <- rm_types if supertype_as_entry == "ENTRY"
        (supertype_of_observation, subtype_as_observation) <- rm_types if subtype_as_observation == "OBSERVATION"
        pair <- rm_types if (supertype_of_observation, subtype_of_entry) == pais
      } yield {
        // (supertype, subtype)
        
      }
      */                       
  }
}


