import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }



/**
 * Tree の基本的な用法を示す
 */
class BioSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll with scala_labo.helpers {
  import projects.scala.labo._

  // def fixture = new {
  //   val list = (1 to 100).toList
  //   val vector = Vector(1,2,3,4,5)
  // }

  describe("バイオメディカル解析実習第3回課題(別解)"){
    trait NA {
      abstract class Base
      case object A extends Base
      case object T extends Base
      case object G extends Base
      case object C extends Base
    }
    trait Codon extends NA {
      abstract class Base(val first:NA#Base, val second:NA#Base, val third:NA#Base) {
	def name:String = this.getClass.getSimpleName
	override def toString = name

	final override def equals(other: Any) = {
          val that = other.asInstanceOf[this.type]
          if (that == null) false
          else true
	}
      }

      case class STOP(override val first:NA#Base, override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
      abstract class Amino(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
      case class Phe(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Leu(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Ser(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Pro(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class His(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Gln(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Arg(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Asn(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Lys(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Val(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Ala(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Asp(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Glu(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Gly(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Tyr(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Cys(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Trp(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Ile(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Met(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
      case class Thr(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Amino(first,second,third)
    }
    object bio extends NA with Codon {
      def to_amino(first:NA#Base)(second:NA#Base)(third:NA#Base):Codon#Base = {
	val codon:Tuple3[NA#Base,NA#Base,NA#Base] = (first, second, third)
	codon match {
          case (T,T,T) => Phe(first, second, third)
          case (T,T,C) => Phe(first, second, third)
          case (T,T,A) => Leu(first, second, third)
          case (T,T,G) => Leu(first, second, third)
          case (T,C,_) => Ser(first, second, third)
          case (T,A,T) => Tyr(first, second, third)
          case (T,A,C) => Tyr(first, second, third)
          case (T,A,A) => STOP(first, second, third)
          case (T,A,G) => STOP(first, second, third)
          case (T,G,T) => Cys(first, second, third)
          case (T,G,C) => Cys(first, second, third)
          case (T,G,A) => STOP(first, second, third)
          case (T,G,G) => Trp(first, second, third)
          case (C,T,_) => Leu(first, second, third)
          case (C,C,_) => Pro(first, second, third)
          case (C,A,T) => His(first, second, third)
          case (C,A,C) => His(first, second, third)
          case (C,A,A) => Gln(first, second, third)
          case (C,A,G) => Gln(first, second, third)
          case (C,G,_) => Arg(first, second, third)
          case (A,T,T) => Ile(first, second, third)
          case (A,T,C) => Ile(first, second, third)
          case (A,T,A) => Ile(first, second, third)
          case (A,T,G) => Met(first, second, third)
          case (A,C,_) => Thr(first, second, third)
          case (A,A,T) => Asn(first, second, third)
          case (A,A,C) => Asn(first, second, third)
          case (A,A,A) => Lys(first, second, third)
          case (A,A,G) => Lys(first, second, third)
          case (A,G,T) => Ser(first, second, third)
          case (A,G,C) => Ser(first, second, third)
          case (A,G,A) => Arg(first, second, third)
          case (A,G,G) => Arg(first, second, third)
          case (G,T,_) => Val(first, second, third)
          case (G,C,_) => Ala(first, second, third)
          case (G,A,T) => Asp(first, second, third)
          case (G,A,C) => Asp(first, second, third)
          case (G,A,A) => Glu(first, second, third)
          case (G,A,G) => Glu(first, second, third)
          case (G,G,_) => Gly(first, second, third)
          case _ => throw new Exception()
	}
      }    
    }

    import bio._
    val (t,a,g,c) = (bio.T,bio.A, bio.G,bio.C)
    it("to_amino") {
      to_amino(t)(t)(t) should equal(bio.Phe(t,t,t))
      to_amino(t)(t)(t) should equal{
	to_amino(t)(t)(c)
      }
    }
    describe("for内包でコドンの組み合わせを探る"){
      val all_patterns = for {
    	first <- Seq(t,a,g,c)
    	second <- Seq(t,a,g,c)
    	third <- Seq(t,a,g,c)
      } yield {
    	to_amino(first)(second)(third)
      }
      it("コドンの全ての組み合わせの個数は64個である"){
	all_patterns.length should equal(64)
      }
      it("STOPコドンの組み合わせは3個である"){
	all_patterns.filter{i => 
	  i.isInstanceOf[Codon#STOP]
	}.length should equal(3)
      }
      it("アミノ酸の種類は20個である"){
	all_patterns.filter{i => 
	  i.isInstanceOf[Codon#Amino]
	}.groupBy{_.name}.map{_._2.head}.toList.length should equal(20)
      }
    }
    describe("コドンの第1ポジションが変わってもアミノ酸が変わらない全ての場合を列挙する"){
      val answer_with_empty_list = for {
    	second <- List(t,a,g,c)
    	third <- List(t,a,g,c)
      } yield {
	val aminos = for {
	  first <- List(t,a,g,c)
	} yield {
	  to_amino(first)(second)(third)
	}
	val groupByAminoName = aminos.groupBy{x=>x.name}.filter{case (_,lst) => lst.size > 1 }.values
	for {
	  mapping <- groupByAminoName
	  pair <- mapping
	} yield pair
      }
      val final_answers = for {
      	codon_list <- answer_with_empty_list
      	if codon_list.nonEmpty
      } yield {
	for {
	  codon <- codon_list
	} yield ((codon.first, codon.second, codon.third))
      }
      final_answers should equal{
	List(List((T,T,A), (C,T,A)), List((T,T,G), (C,T,G)), List((A,G,A), (C,G,A)), List((A,G,G), (C,G,G)))
      }
      final_answers.foreach{ codons =>
      	//val codon = ((answer.first, answer.second, answer.third))
      	println(codons)
      }
    }
  }
  describe("バイオメディカル解析実習第3回課題"){
  trait NA {
    abstract class Base
    case class A() extends Base {
      override def toString = "a"
    }
    case class T() extends Base {
      override def toString = "t"
    }
    case class G() extends Base {
      override def toString = "g"
    }
    case class C() extends Base {
      override def toString = "c"
    }
  }

  trait Codon extends NA {
    abstract class Base(val first:NA#Base, val second:NA#Base, val third:NA#Base) {
      def name:String = this.getClass.getSimpleName
      override def toString = name

      final override def equals(other: Any) = {
        val that = other.asInstanceOf[this.type]
        if (that == null) false
        else true
      }
    }

    case class STOP(override val first:NA#Base, override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    abstract class Amino(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Phe(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Leu(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Ser(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Pro(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class His(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Gln(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Arg(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Asn(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Lys(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Val(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Ala(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Asp(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Glu(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Gly(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Tyr(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Cys(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Trp(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Ile(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Met(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
    case class Thr(override val first:NA#Base,override val second:NA#Base,override val third:NA#Base) extends Base(first,second,third)
  }

  object bio extends NA with Codon {
    def to_amino(first:NA#Base)(second:NA#Base)(third:NA#Base):Codon#Base = {
      first match {
 	case thymine:NA#T => {
  	  second match {
  	    case thymine:NA#T => {
  	      third match {
  		case thymine:NA#T => Phe(first,second,third)
  		case cytosine:NA#C => Phe(first,second,third)
  		case adenine:NA#A => Leu(first,second,third)
  		case guanine:NA#G => Leu(first,second,third)
  	      }
  	    }
  	    case cytosine:NA#C => Ser(first,second,third)
  	    case adenine:NA#A => {
  	      third match {
  		case thymine:NA#T => Tyr(first,second,third)
  		case cytosine:NA#C => Tyr(first,second,third)
  		case adenine:NA#A => STOP(first,second,third)
  		case guanine:NA#G => STOP(first,second,third)
  	      }
  	    }
  	    case guanine:NA#G => {
  	      third match {
  		case thymine:NA#T => Cys(first,second,third)
  		case cytosine:NA#C => Cys(first,second,third)
  		case adenine:NA#A => STOP(first,second,third)
  		case guanine:NA#G => Trp(first,second,third)
  	      }
  	    }
  	  }
	}
 	case cytosine:NA#C => {
  	  second match {
  	    case thymine:NA#T => Leu(first,second,third)
  	    case cytosine:NA#C => Pro(first,second,third)
  	    case adenine:NA#A => {
  	      third match {
  		case thymine:NA#T => His(first,second,third)
  		case cytosine:NA#C => His(first,second,third)
  		case adenine:NA#A => Gln(first,second,third)
  		case guanine:NA#G => Gln(first,second,third)
  	      }
  	    }
  	    case guanine:NA#G => Arg(first,second,third)
  	  }
	}
 	case adenine:NA#A => {
  	  second match {
  	    case thymine:NA#T => {
  	      third match {
  		case thymine:NA#T => Ile(first,second,third) 
  		case cytosine:NA#C => Ile(first,second,third)
  		case adenine:NA#A => Ile(first,second,third)
  		case guanine:NA#G => Met(first,second,third)
  	      }
  	    }
  	    case cytosine:NA#C => Thr(first,second,third)
  	    case adenine:NA#A => {
  	      third match {
  		case thymine:NA#T => Asn(first,second,third)
  		case cytosine:NA#C => Asn(first,second,third)
  		case adenine:NA#A => Lys(first,second,third)
  		case guanine:NA#G => Lys(first,second,third)
  	      }
  	    }
  	    case guanine:NA#G => {
  	      third match {
  		case thymine:NA#T => Ser(first,second,third)
  		case cytosine:NA#C => Ser(first,second,third)
  		case adenine:NA#A => Arg(first,second,third)
  		case guanine:NA#G => Arg(first,second,third)
  	      }
  	    }
  	  }
	}
 	case guanine:NA#G => {
    	  second match {
    	    case thymine:NA#T => Val(first,second,third)
    	    case cytosine:NA#C => Ala(first,second,third)
    	    case adenine:NA#A => {
    	      third match {
    		case thymine:NA#T => Asp(first,second,third)
    		case cytosine:NA#C => Asp(first,second,third)
    		case adenine:NA#A => Glu(first,second,third)
    		case guanine:NA#G => Glu(first,second,third)
    	      }
    	    }
    	    case guanine:NA#G => Gly(first,second,third)
    	  }
	}
      }
    }
  }


    import bio._

    it("to_amino") {
      val (t,a,g,c) = (bio.T(),bio.A(), bio.G(),bio.C())
      to_amino(t)(t)(t) should equal(bio.Phe(t,t,t))
      to_amino(t)(t)(t) should equal{
	to_amino(t)(t)(c)
      }
    }
    describe("for表現でコドンの組み合わせを探る"){
      it("全ての組み合わせを探る"){
	val possible_aminos = for {
    	  first <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
    	  second <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
    	  third <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
	} yield {
    	  to_amino(first)(second)(third)
	}
	possible_aminos.length should equal(64)
	//possible_aminos.distinct.length should equal(21)
      }

      it("1番目の塩基のみが未定の場合の組み合わせを探る"){
	val first_free = for {
    	  second <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
    	  third <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
	} yield {
    	  to_amino(_:NA#Base)(second)(third)
	}
	first_free.length should equal(16)
	val codon_patterns = for {
    	  to_amino <- first_free
	} yield {
    	  val codons = for {
    	    first <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
    	  } yield {
    	    to_amino(first)
    	  }
	  codons
	}
	// println(codon_patterns)
	// val distinct_codons = for {
	//   codon_pattern <- codon_patterns
	// } yield codon_pattern.groupBy{_.name}.map{_._2.head}
	
	val distinct_codons = for {
	  codon_pattern <- codon_patterns
	} yield codon_pattern.groupBy{_.name}.map{_._2.head}
	// distinct_codons should equal(Nil)
	distinct_codons.filter{_.toList.length > 1}.length should equal(16)
	distinct_codons.filter{_.toList.length == 4}.length should equal(12)
      }
      it("コドンの第1ポジションが変わってもアミノ酸が変わらない全ての場合を列挙する"){
	val (t,a,g,c) = (bio.T(),bio.A(), bio.G(),bio.C())
	val all_combinations:List[List[Codon#Base]] = for {
    	  second <- List(t,a,g,c)
    	  third <- List(t,a,g,c)
	} yield {
	  for {
	    first <- List(t,a,g,c)
	  } yield {
	    to_amino(first)(second)(third)
	  }
	}
	val duplicatedItems = for {
	  combinations <- all_combinations
	} yield {
	  combinations.groupBy{x=>x.name}.filter{case (_,lst) => lst.size > 1 }.values
	}
	val answer = for {
	  same_aminos <- duplicatedItems.filter{maplike => maplike.nonEmpty}
	  same_amino <- same_aminos
	  amino <- same_amino
	} yield {
	  ((amino.first, amino.second, amino.third))
	}
	answer should equal(List(((t, t, a)),
				  ((c, t, a)),
				  ((t, t, g)),
				  ((c, t, g)),
				  ((a, g, a)),
				  ((c, g, a)),
				  ((a, g, g)),
				  ((c, g, g))))
	     
      }
      it("2番目の塩基のみが未定の場合の組み合わせを探る"){
	val second_free = for {
    	  first <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
    	  third <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
	} yield {
    	  to_amino(first)(_:NA#Base)(third)
	}
	second_free.length should equal(16)
	val codon_patterns = for {
    	  to_amino <- second_free
	} yield {
    	  val codons = for {
    	    second <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
    	  } yield {
    	    to_amino(second)
    	  }
	  codons
	}
	val distinct_codons = for {
	  codon_pattern <- codon_patterns
	} yield codon_pattern.groupBy{_.name}.map{_._2.head}
	//distinct_codons should equal(Nil)
	distinct_codons.filter{_.toList.length > 1}.length should equal(16)
	distinct_codons.filter{_.toList.length == 4}.length should equal(15)
      }
      it("3番目の塩基のみが未定の場合の組み合わせを探る"){
	val third_free = for {
    	  first <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
    	  second <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
	} yield {
    	  to_amino(first)(second) _
	}
	third_free.length should equal(16)

	val codon_patterns = for {
    	  to_amino <- third_free
	} yield {
    	  val codons = for {
    	    third <- Seq(bio.T(),bio.A(), bio.G(),bio.C())
    	  } yield {
    	    to_amino(third)
    	  }
	  codons
	}
	val distinct_codons = for {
	  codon_pattern <- codon_patterns
	} yield codon_pattern.groupBy{_.name}.map{_._2.head}
	//distinct_codons should equal(Nil)
	distinct_codons.filter{_.toList.length > 1}.length should equal(8)
	distinct_codons.filter{_.toList.length == 4}.length should equal(0)
      }
    }
  }
}




