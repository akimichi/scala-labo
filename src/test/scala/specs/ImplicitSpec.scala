import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * implicitの基本的な用法を示す
 */
class ImplicitSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("implicitのスコープについて") {
    describe("コンパニオンオブジェクト companion object は implicit のスコープになる") {
      it("classとのコンパニオン"){
	class  Desired(val content:String)
	object Desired {
	  implicit val hidden = new Desired("this is what I wanted")
	}
	import Desired._
	object test {
          def invoke(arg:String)(implicit desired:Desired):String = {
            desired.content
          }
	}
	test.invoke("implicit by companion") should equal("this is what I wanted")
      }
      // it("traitとのコンパニオン"){
      // 	trait CompanionWithTrait {
      // 	  implicit val abstractMember:Int
	  
      // 	  val negate:Int = - abstractMember
      // 	}
      // 	object CompanionWithTrait {
      // 	  implicit override val abstractMember:Int = 1
      // 	} extends CompanionWithTrait
      // 	// import CompanionWithTrait._
      // 	CompanionWithTrait.abstractMember should equal(1)
      // 	CompanionWithTrait.negate should equal(1)
      // }
    }
  }
  describe("implicitlyを用いた単純なケースにおいて") {

    implicit val int_value:Int = 111
    implicit val long_value:Long = 999
    implicit val double_value:Double = 123.456
      
    it("implicitlyを使用して現時点の暗黙の値を参照する") {
      object test {
        def calc:Long = {
          implicitly[Int] + implicitly[Long]
        }
      }
      test.calc should equal(111+999)
    }

    it("implicitlyを使用して、case class の引数に現時点の暗黙の値を渡す") {
      case class TestResult(val int_value:Int, val long_value:Long, val double_value:Double)
      object test {
        def calc:TestResult = TestResult(implicitly[Int], implicitly[Long], implicitly[Double])
      }
      val result = test.calc
      result.int_value should equal(111)
      result.long_value should equal(999)
      result.double_value should equal(123.456)
    }
    it("implicityを使用しない場合は、引数で明示的に渡す必要がある") {
      case class TestResult(val int_value:Int, val long_value:Long, val double_value:Double)
      object test {
        def calc()(implicit int_value:Int, long_value:Long, double_value:Double):TestResult = TestResult(int_value, long_value, double_value)
      }
      val result = test.calc()
      result.int_value should equal(111)
      result.long_value should equal(999)
      result.double_value should equal(123.456)
    }
  }
  describe("objectの内部に入れ子にされたtraitを implicitの対象とする") {
    object Outer {
      trait Inner {
        val content:String
      }
      implicit def newInner = new Inner {
        val content = "the content inside Inner"
      }
    }
    it("Scala for the impatient,p.99 の例") {
      import Outer._
      implicitly[Outer.Inner].content should equal("the content inside Inner")
    }
  }
  describe("implicit function parameter を利用して暗黙変換を実行する") {
    it("Scala for the Impatient,p.310の例") {
      object test {
        def smaller[T](a:T,b:T)(implicit order:T => Ordered[T]):T = {
          if(order(a) < b) a else b /* order関数で Ordered への暗黙変換が実施されている */
        }
      }
      test.smaller(1,2) should equal(1)
    }    
  }
  describe("implicitlyとtypeclassを用いて") {
    trait Type[A] {
      val value:A
    }
    class AIntType extends Type[Int] {
      val value:Int = 3
    }
    
    object test {
      def calc[A : Type]:Type[A] = {
        val implicitly_generated_value = implicitly[Type[A]]
        implicitly_generated_value
      }
    }
    it("暗黙のうちに型に該当する変数を生成する") {
      implicit val a_int_type:AIntType = new AIntType
      test.calc.value should equal(3)
    }
  }
  describe("typeclassを用いて DataMapper,DomainModelを模倣する") {
    object test {
      
      trait DataMapper[+T <: DomainModel[_]] {
        def service_name:String
      }
      trait DomainModel[+T <: Any] {
        type TDataMapper <: DataMapper[DomainModel[_]]
        def save()(implicit data_mapper:TDataMapper):Unit
      }
      
      case class ADataMapper(val service_name:String) extends DataMapper[ADomainModel]
      case class BDataMapper(val service_name:String) extends DataMapper[BDomainModel]
      
      case class ADomainModel(val field:String) extends DomainModel[String] {
        type TDataMapper = ADataMapper
        def save()(implicit data_mapper:TDataMapper):Unit = {
          println(data_mapper.service_name)
        }
      }
      case class BDomainModel(val field:String) extends DomainModel[Int] {
        type TDataMapper = BDataMapper
        def save()(implicit data_mapper:TDataMapper):Unit = {
          println(data_mapper.service_name)
        }
      }
      implicit val a_data_mapper = ADataMapper("a_data_mapper")
      implicit val b_data_mapper = BDataMapper("b_data_mapper")
    }
    
    it("implictな DataMapperで実験する") {
     import test._
      val a_domain_model = ADomainModel("adomain_model_field")
      a_domain_model.save().isInstanceOf[Unit] should equal(true)
    }
  }
  describe("typeclassを用いて DataMapper,DomainModelを模倣する(その2)") {
    object test {
      trait DataMapper[+T <: DomainModel[_]] {
        def service_name:String
      }
      trait DomainModel[+T <: Any] {
        type TDataMapper <: DataMapper[DomainModel[_]]
        def save():Unit
      }
      
      case class ADataMapper(val service_name:String) extends DataMapper[ADomainModel]
      case class ADomainModel(val field:String) extends DomainModel[String] {
        type TDataMapper = ADataMapper
        def save():Unit = {
          val data_mapper = implicitly[ADataMapper]
          println(data_mapper.service_name)
        }
      }
      implicit val a_data_mapper = ADataMapper("a_data_mapper")
    }
    
    it("implicty[ADataMapper]で DataMapperを発見できる") {
     import test._
      val a_domain_model = ADomainModel("adomain_model_field")
      a_domain_model.save().isInstanceOf[Unit] should equal(true)
    }
  }
  describe("typeclassを用いて DataMapper,DomainModelを模倣する(その3)") {
    object test {
      trait DataMapper[+T <: DomainModel[_]] {
        def service_name:String
      }
      trait DomainModel[+T <: Any] {
        type TDataMapper <: DataMapper[DomainModel[_]]
        def save():Unit
      }
      /** 各種DataMapperクラスの定義 */
      case class ADataMapper(val service_name:String) extends DataMapper[ADomainModel]
      case class BDataMapper(val service_name:String) extends DataMapper[BDomainModel]
      /** 各種DomainModelクラスの定義 */      
      case class ADomainModel(val field:String) extends DomainModel[String] {
        type TDataMapper = ADataMapper
        def save():Unit = {
          val data_mapper = implicitly[ADataMapper]
          println(data_mapper.service_name)
        }
      }
      case class BDomainModel(val field:String) extends DomainModel[Int] {
        type TDataMapper = BDataMapper
        /**
         * この saveメソッドには引数がない。
         * にもかかわらず、内部で implicitly を呼びだすことで複数の implicit な DataMapperインスタンスを起動している。* 
         */ 
        def save():Unit = {
          val data_mapper = implicitly[ADataMapper]
          println(data_mapper.service_name)
          val another_data_mapper = implicitly[BDataMapper]
          println(another_data_mapper.service_name)
        }
      }
      implicit val a_data_mapper = ADataMapper("A_DATA_MAPPER")
      implicit val b_data_mapper = BDataMapper("B_DATA_MAPPER")
    }
    
    it("BDomainModel.saveメソッド中でimplicty[ADataMapper]とimplicty[BDataMapper]とで複数の DataMapperを発見できる") {
     import test._
      val b_domain_model = BDomainModel("b_domain_model_field")
      /**
       * saveメソッドはUnit返しなのでテスト結果からは直接はわかりにくいが、sbtコンソール上にて
       *  A_DATA_MAPPER
       *  B_DATA_MAPPER
       * と続けて印字されていることから、BDomainModel.save()メソッド中で implicity[ADataMapper]とimplicity[BDataMapper]とがそれぞれ適切なDataMapperインスタンスを発見できていることがわかる。
       */
      b_domain_model.save().isInstanceOf[Unit] should equal(true)
    }
  }


}

