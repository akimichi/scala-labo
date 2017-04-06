package test
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import scala.util.matching.Regex

import scala_seminar._

class Chapter13Spec extends FunSpec with ShouldMatchers with helpers {

  
  describe("sec 13.1: The Main Collections Traits"){
    describe("コレクション階層"){
      info("http://docs.scala-lang.org/overviews/collections/overview.html")
      info("全てのコレクションは Iterable を mixin している")      
    }
    it("Iterable trait は iteratorメソッドでイテレータを取得できる"){
      val collection = Iterable(1,2,3)
      val iterator = collection.iterator
      while(iterator.hasNext) {
        iterator.next should be > 0
      }
    }
    it("Seqは順序付きコレクションである"){
      val collection = Seq(1,2,3)
      collection.apply(1) should equal(2)
    }
    it("Setは順序なしコレクションである"){
      val collection = Set(1,2,3)
      collection.contains(1) should equal(true)
    }
    it("コレクション型は、それぞれの companion object の apply でインスタンスを生成できる"){
      Iterable(0xFF,0xFF00,0xFF0000) should be {
        anInstanceOf[Iterable[Int]]
      }
      import java.awt.Color
      Set(Color.RED, Color.GREEN, Color.BLUE)  should be {
        anInstanceOf[Set[AnyRef]]
      }
      info("Uniform Creation Principleを呼ぶ")
    }
  }
  describe("sec 13.2: Mutable and Immmutable Collections"){
    it("明示しなれば、デフォルトでは immutable なコレクションとなる"){
      scala.collection.Map("Hello" -> 42) should be {
        anInstanceOf[scala.collection.immutable.Map[String,Int]]
      }
    }
    it("scala,Predefはデフォルトでインポートされている"){
      /*
       *
       * object Predef {
       *   type Set[T] = scala.collection.immutable.Set[T]
       *   type Map[K, V] = scala.collection.immutable.Map[K, V]
       *   val Set = scala.collection.immutable.Set
       *   val Map = scala.collection.immutable.Map
       *       ...
       */ 

      List(1,2,3) should be {
        anInstanceOf[scala.collection.immutable.List[Int]]
      }
    }
    it("import scala.collection.mutable の用法"){
      import scala.collection.mutable
        Map("Hello" -> 42) should be {
          anInstanceOf[scala.collection.immutable.Map[String,Int]]
        }
        mutable.Map("Hello" -> 42) should be {
          anInstanceOf[scala.collection.mutable.Map[String,Int]]
        }
    }
    it("immutable なコレクションへの操作"){
      val numbers = Set(1,2,3)
      val newNumbers = numbers + 9
      
      System.identityHashCode(numbers) should not equal(System.identityHashCode(newNumbers))
      
      def digits(n:Int) : Set[Int] = {
        if(n < 0) digits(-n)
        else if(n < 10) Set(n)
        else digits(n / 10) + (n % 10)
      }
      // println(digits(210))
      digits(210) should equal {
        Set(0,1,2)
      }
    }
  }
  describe("sec 13.3: Sequences"){
    it("Vectorはランダムアクセス可能なシーケンスである"){
      val vector = Vector.range(0,1e7.toInt)
      vector(1e7.toInt -1) should equal(1e7.toInt -1)
      // val vector = Vector(List.range(0,1e7.toInt): _*)
      // vector(1e7.toInt -1) should equal(1e7.toInt -1)
      /*
      time {
        Vector.range(0,1e7.toInt).apply(1e7.toInt -1)
      }
      time {
        List.range(0,1e7.toInt).apply(1e7.toInt -1)
      }
      */
    }
    it("Range"){
      val rangeTo = 1 to 9
      rangeTo should equal{
         Range(1, 10)
      }
      val rangeUntil = 1 until 10
      rangeUntil should equal{
         Range(1, 10)
      }
    }
  }
  describe("sec 13.4: Lists"){
    describe("Listの基本演算"){
      val list = 1 :: 2 :: Nil
      list should equal {
        List(1,2)
      }
      list.head should equal{
        1
      }
      list.tail should equal{
        List(2)
      }
      list.tail should equal{
        2 :: Nil
      }
      list.tail.tail should equal{
        Nil
      }
      it("::演算子は右結合である"){
        (9 :: 4 :: 2 :: Nil) should equal {
          (9 :: (4 :: (2 :: Nil)))
        }
        (9 :: 4 :: 2 :: Nil) should equal {
          (Nil.::(2).::(4).::(9))
        }
      }
    }
    it("sumを計算する"){
      val digits = List(4,2)
      def sum(lst:List[Int]) : Int = lst match {
        case Nil => 0
        case h :: t => h + sum(t)
      }
      sum(List(9,4,2)) should equal(15)
      List(9,4,2).sum should equal(15)
    }
  }
  describe("sec 13.5: Mutable Lists"){
    info("省略する")
  }
  describe("sec 13.6: Sets"){
    it("Setは要素の重複を排除する"){
      (Set(2,0,1) + 1) should equal{
        Set(2,0,1)
      }
    }
    it("linked hash set は挿入の順番を記憶している"){
      val weekdays = scala.collection.mutable.LinkedHashSet("Mo","Tu","We","Th","Fr")
      weekdays.toList should equal(List("Mo","Tu","We","Th","Fr"))
    }
    it("bit set"){
      info("ビット集合 (BitSet) は大きい整数のビットを小さな整数のコレクションを使って表す。")
      val bits = scala.collection.immutable.BitSet.empty
      val newBits = bits + 3 + 4 + 4
      newBits(3) should equal(true)
      newBits(5) should equal(false)
    }
  }
  describe("sec 13.7: Operators for Adding or Removing Elements"){
    it("要素を追加するメソッド"){
      Vector(1,2,3) :+ 5 should equal(Vector(1,2,3,5))
      1 +: Vector(1,2,3) should equal(Vector(1,1,2,3))
      Vector(1,2,3) ++ Vector(4,5,6) should equal(Vector(1,2,3,4,5,6))
    }
    describe("コレクションの更新"){
      it("mutableなコレクションを更新する"){
        val numbers = scala.collection.mutable.ArrayBuffer(1,2,3)
        val oldHashCode = System.identityHashCode(numbers)
        numbers += 5
        
        oldHashCode should equal{
          System.identityHashCode(numbers)
        }
      }
      it("immutableなコレクションを更新する"){
        var numbers = scala.collection.immutable.Set(1,2,3)
        val oldHashCode = System.identityHashCode(numbers)
        numbers += 5
        
        oldHashCode should not equal{
          System.identityHashCode(numbers)
        }
      }
      it("要素を削除する"){
        (Set(1,2,3) - 2) should equal {
          Set(1,3)
        }
      }
      it("コレクション同士を加算する"){
        (Set(1,2,3) ++ Set(3,4,5)) should equal {
          Set(1,2,3,4,5)
        }
        (List(1,2,3) ++ List(3,4,5)) should equal {
          List(1,2,3,3,4,5)
        }
      }
    }

  }
  describe("sec 13.8: Common Methods"){
    describe("Iterableのメソッド"){
      val iterable:Iterable[Int] = Iterable(1,2,3)
      iterable.takeRight(1) should equal(List(3))
      iterable.dropRight(2) should equal(List(1))
      iterable.reduceLeft{(accum,item) => accum + item} should equal(6)
    }
    describe("Seqのメソッド"){
      val seq = Seq(1,2,3)
      it("+: 演算子でSeqに要素を追加する"){
        (0 +: seq) should equal{
          Seq(0,1,2,3)
        }
      }
      it("Seq#find(p: (A) ⇒ Boolean): Option[A] を使う") {
        seq.find { item => item % 2 == 0 } should equal(Some(2))
      }
      it("mkStringで文字列を作成する") {
        seq.mkString  should equal("123")
        seq.mkString(",")  should equal("1,2,3")
      }
      it("mapで要素を変換する"){
        val serializedEntries = seq map(_.toString)
        "[ " + (serializedEntries mkString ", ") + " ]" should equal("[ 1, 2, 3 ]")
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
  describe("sec 13.9: Mapping a Function"){
    it("mapを使う"){
      val names = List("Peter","Paul","Mary")
      names.map(_.toUpperCase) should equal {
        List("PETER","PAUL","MARY")
      }
      val result = for {
        n <- names
      } yield n.toUpperCase
      result  should equal {
        List("PETER","PAUL","MARY")
      }
    }
    describe("flatMapを使う"){
      val names = List("Peter","Paul","Mary")
      def ulcase(s:String) = Vector(s.toUpperCase(), s.toLowerCase())
      names.map(ulcase) should equal {
        List(Vector("PETER", "peter"), Vector("PAUL", "paul"), Vector("MARY", "mary"))
      }
      names.flatMap(ulcase) should equal {
        List("PETER", "peter","PAUL", "paul","MARY", "mary")
      }
      
      // Some(1) flatMap { x => Some(x)} should equal{
      //   Some(1)
      // }
      // None flatMap { x => None} should equal(None)
    }
    describe("for表現とmap,flatMapの関係"){
      info("c.f. Scalaスケーラブルプログラミング第2版,p.444")
      it("ジェネレータが1個の場合は、mapに変換される"){
        val names = List("Peter","Paul","Mary")
        (for{
          name <- names
        } yield name.toUpperCase) should equal {
          names.map{name => name.toUpperCase}
        }
      }
      it("ジェネレータが2個の場合は、flatMapに変換される"){
        val result = for {
          i <- List(1,2,3)
          j <- List(7,8,9)
        } yield 10*i+j
        result should equal {
          List(17, 18, 19, 27, 28, 29, 37, 38, 39) 
        }
        result should equal {
          List(1,2,3).flatMap{ i =>
            for {
              j <- List(7,8,9)
            } yield 10*i+j
          }
        }
      }
    }    
    it("collectを使う"){
      info("collect は部分関数 partial function を受け取る")
      "-3+4".collect {
        case '+' => 1
        case '-' => -1
      } should equal {
        Vector(-1,1)
      }
    }
  }
  describe("sec 13.10: Reducing,Folding and Scanning"){
    info("このセクションでは、二項演算を用いた反復処理を扱かう")
    it("reduceLeft,reduceRight") {
      List(1,7,2,9).reduceLeft{(current,subsequent) => current - subsequent} should equal {
        -17 // ((1 - 7) - 2) - 9
      }
      List(1,7,2,9).reduceRight{(current,subsequent) => current - subsequent} should equal {
        -13 // 1 - (7 - (2 - 9)
      }
      List(1,7,2,9).reduceRight{(current,subsequent) => current + subsequent} should equal {
        List(1,7,2,9).reduceLeft{(current,subsequent) => current + subsequent}
      }
    }
    it("foldLeft") {
      List(1,7,2,9).foldLeft(0){(current,subsequent) => current - subsequent} should equal {
        -19 // (((0 - 1) - 7) - 2) - 9
      }
    }
    describe("foldRight") {
      List(1,7,2,9).foldRight(0){(current,subsequent) => current - subsequent} should equal {
        -13 // (1 - (7 - (2 - (9 - 0))))
      }
      (1 :: 2 :: 3 :: List.empty[Int]).foldRight(List.empty[Int]){(head,tail) => head :: tail} should equal {
        List(1,2,3)
      }
    }
    describe("reduceLeft と foldLeft の違い"){
      info("reduceLeft[B >: A] (f: (B, A) => B): B")
      info("foldLeft[B]        (z: B) (f: (B, A) => B): B")
      List(1,7,2,9).reduceLeft{(current,subsequent) => current + subsequent} should equal {
        List(1,7,2,9).foldLeft(0){(current,subsequent) => current + subsequent}
      }
      List(1,7,2,9).reduceLeft{(current,subsequent) => current - subsequent} should not equal {
        List(1,7,2,9).foldLeft(0){(current,subsequent) => current - subsequent}
      }
    }
    describe("scanLeft/scanRight は反復処理の中間結果もあわせて返す") {
      (1 to 10).scanLeft(0){_ + _} should equal {
        Seq(0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55)
      }
      (1 to 10).scanRight(0){_ + _} should equal {
        Seq(55, 54, 52, 49, 45, 40, 34, 27, 19, 10, 0)
      }
    }
  }
  describe("sec 13.11: Zipping"){
    it("zipメソッドは2つのコレクションを要素ごとに対にする"){
      val prices = List(5.0,20.0,9.95)
      val quantities = List(10,2,1)
      prices.zip(quantities) should equal {
        List((5.0,10), (20.0,2), (9.95,1))
      }
      prices.zip(quantities).map{(item) =>
        val (price, quantity) = item
        price * quantity
      }.sum should equal {
        99.95
      }
      List(5.0,20.0,9.95) zip List(10,2) should equal {
        List((5.0,10), (20.0,2))
      }
    }
    it("zipAll"){
      List(5.0,20.0,9.95) zipAll(List(10,2), 0.0,1) should equal {
        List((5.0,10), (20.0,2), (9.95,1))
      }
    }
    it("zipWithIndexは、2つ目の要素にインデックスがはいる"){
      "Scala".zipWithIndex should equal {
        Seq(('S',0), ('c',1), ('a',2), ('l',3), ('a',4))
      }
      "Scala".zipWithIndex.max._2 should equal(3)

      val ListWithIndex = for{
        (i, x) <- List('A','B','C').zipWithIndex
      } yield {
        i + " : " + x
      }
      ListWithIndex should equal{
        List("A : 0", "B : 1", "C : 2") 
      }
      
    }
  }
  describe("sec 13.12: Iterators"){
    it("iteratorメソッドでイタレータを得る"){
      val list = List.range(1,100000)
      for {
        element <- list.iterator
      } {
        element should be > 0
      }
    }
    // it("イタレータは、巨大なコレクションの走査に有効である"){
    //   import scala.io._
    //   val iterator = try {
    //     Source.fromFile("src/test/resources/10Gen_Venter_SNV.gvf","UTF-8")
    //   } catch {
    //     case ex:Throwable => throw ex
    //   }
    //   val lines:Iterator[String] = iterator.getLines
    //   for {
    //     line <- lines
    //   } {
    //     val cells = line.split(',')
    //     // println(cells(3),cells(6))
    //     // if(cells(3) == "RUS") {
    //     //   if(cells(6) == "ISO 3166-2:RU")
    //     //     fail("")
    //     // }
    //   }
    //   iterator.close()
    // }
  }
  describe("sec 13.13: Streams"){
    describe("無限ストリームは遅延評価される不変なデータ構造である"){
      info("#::でストリームを生成する")
      def numsFrom(n:BigInt) : Stream[BigInt] = n #:: numsFrom(n + 1)
      val stream:Stream[BigInt] = numsFrom(1) // 1 #:: 2 #:: 3 #:: 4 #:: 5 #:: ....
      it("Stream.take"){
        stream.take(2) should equal{
          1 #:: 2 #:: Stream.empty
        }
        stream.take(1).head should equal(1)
      }
      it("Stream.forceで評価を強制する"){
        stream.take(5).force should equal{
          1 #:: 2 #:: 3 #:: 4 #:: 5 #:: Stream.empty
        }
        stream.take(5) should equal{
          1 #:: 2 #:: 3 #:: 4 #:: 5 #:: Stream.empty
        }
        numsFrom(1).take(5).force should equal{
          1 #:: 2 #:: 3 #:: 4 #:: 5 #:: Stream.empty
        }
      }
      it("squares"){
        val squares = numsFrom(1).map{x => x * x}
        squares.take(5).force should equal {
          Stream(1, 4, 9, 16, 25)
        }
      }
      it("Stream.indices: Range "){
        numsFrom(1).take(5).indices should equal(0 to 4)
      }
      it("Stream.init: Stream "){
        numsFrom(1).take(5).init should equal(1 #:: 2 #:: 3 #:: 4 #:: Stream.empty)
      }
      it("Stream.last "){
        numsFrom(1).take(5).last should equal(5)
      }
      // it("Stream.mkString"){
      //   stream.mkString should equal("12345")
      //   stream.mkString(",") should equal("1,2,3,4,5")
      // }
      // it("toStreamで通常のコレクションを Stream型に変換できる"){
      //   import scala.io._
      //   val source = try {
      //     Source.fromFile("src/test/resources/10Gen_Venter_SNV.gvf","UTF-8")
      //   } catch {
      //     case ex:Throwable => throw ex
      //   }
      //   val lines:Stream[String] = source.getLines.toStream
      //   lines(6) should equal {
      //     "# J. Craig Venter"
      //   }
      //   lines.find{line:String =>
      //     val pattern:Regex = """^(chr\d+).*;Reference_seq=([ATGC]);Genotype=heterozygous;$""".r
      //     line match {
      //       case pattern(chr,ref) => {
      //         if(chr == "chr1" && ref == "G") true
      //         else false
      //       }
      //       case _ => false
      //     }
      //   } should equal{
      //     Some("chr1	Celera	SNV	814932	814932	.	+	.	ID=1103675000193;Variant_seq=C,G;Reference_seq=G;Genotype=heterozygous;")
      //   }
      //   source.close()

      // }
      it("Stream.find"){
        stream.find(_ == 3) should equal(Some(3))
      }
      it("Stream.contains(elem: Any): Boolean "){
        stream.contains(3) should equal(true)
      }
      it("streamによるfibonacci数列の実装"){
        lazy val fibs: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map(n => {
          n._1 + n._2
        })
        fibs.take(5).last should equal(3)
      }
      it("streamによる mapReduce"){
        object test {
          def mapReduce[T](map: T => T, reduce:(T,T) => T, zero:T)(stream:Stream[T]): T = {
            if(stream.isEmpty == true)
              zero
            else
              reduce(map(stream.head), mapReduce(map, reduce, zero)(stream.tail))
          }
          def product(stream:Stream[BigInt]):BigInt = mapReduce[BigInt](x => x, (x,y) => x*y, 1)(stream)
          def sum(stream:Stream[BigInt]):BigInt = mapReduce[BigInt](x => x, (x,y) => x+y, 0)(stream)
        }
        import test._

        product(numsFrom(1).take(100)) should equal(BigInt("93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000"))
      }
    }
  }
  describe("sec 13.14: Lazy Views"){
    info("lazy view を使うことで、通常のコレクションを遅延評価できる")
    val powers = (0 until 1000).view map(scala.math.pow(10,_))
    powers(100) should equal {
      scala.math.pow(10,100)
    }
    info("処理を連結する際に、中間結果が生成されないという利点がある")
    (0 to 1000).view.map(scala.math.pow(10,_)).map(1 / _).force.last should equal{
      0 // 1/10^i
    }
  }
  describe("sec 13.15: Interoperability with Java Collections"){
    describe("JavaConversionsでjavaと連携する際は、暗黙型変換が実施される"){
      import scala.collection.JavaConversions._
      val props : scala.collection.mutable.Map[String,String] = System.getProperties()
      props("com.horstmann.scala") = "impatient"
      props("com.horstmann.scala") should equal("impatient")
    }

    describe("JavaConvertersでjavaと連携する"){
      /*
       * JavaConvertersオブジェクトのメソッドをインポートしておくと、変換メソッド（asScalaやasJava）が呼び出せるようになる。
       * すなわち、変換は明示的に行う必要がある。
       */ 
      info("http://www.scala-lang.org/api/current/index.html#scala.collection.JavaConverters$")
      import scala.collection.JavaConverters._
      
      it("asScalaメソッドで JavaのMapをScalaのMapに変換する"){
        val props = System.getProperties().asScala
        props("java.runtime.name") should not equal("")
      }
    }
  }
  describe("sec 13.16: Threadsafe Collections"){
    info("並列処理は、本章の紹介されたスレッド安全な可変コレクションよりも、不変コレクションをもとに actor で処理するほうが望ましい")
  }
  describe("sec 13.17: Parallel Collections"){
    val doubled = for{
      i <- (0 until 10000).par
    } yield i * 2
    doubled(100) should equal {
      200
    }
    List("to be or", "not to", "be").par.flatMap(_.split("""\s+""")).foldLeft(Map[String, Int]())((m,s) => m + (s -> (m.getOrElse(s, 0)+1)))

    it("parによる mapReduce"){
      object test {
        def mapReduce[T](mapper: T => T, reducer:(T,T) => T, zero:T)(seq:Seq[T]): T = {
          seq.par.map(mapper).foldLeft(zero)(reducer)
        }
        def product(stream:Stream[BigInt]):BigInt = mapReduce[BigInt](x => x, (x,y) => x*y, 1)(stream)
        def sum(stream:Stream[BigInt]):BigInt = mapReduce[BigInt](x => x, (x,y) => x+y, 0)(stream)
      }
      import test._
      def numsFrom(n:BigInt) : Stream[BigInt] = n #:: numsFrom(n + 1)
      product(numsFrom(1).take(100)) should equal(BigInt("93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000"))
    }
  }
}

