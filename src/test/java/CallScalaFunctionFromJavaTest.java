

//import static org.junit.Assert.*;
//import org.junit.Test;

//import scala.Either;
//import scala.None$;
//import scala.Option;

//import org.example.call.from.java.*;

// public class CallScalaFunctionFromJavaTest {

// //  テストが呼び出しているscalaのサンプルコード(CallFromJavaSample.scala)
// //
// //  case class SampleEntity(val id: Int, val data: String)
// //
// //  trait Service1 {
// //    def function1(param: Option[SampleEntity]): Either[Exception, String] = {
// //      param match {
// //        case Some(value) => Right(value.data)
// //        case None => Left(new Exception("Exception : value=None"))
// //      }
// //    }
// //  }
// //
// //  trait Service2 {
// //    def function2(param: SampleEntity): Option[String] = {
// //      if ( param.data != null ) {
// //        Some(param.data)
// //      } else {
// //        None
// //      }
// //    }
// //  }
// //
// //  object SampleService extends Service1 with Service2
// //

//     //
//     // IDEのエディタ上ではシンボルの解決がうまくいかず、エラーとして検出されるかもしれない
//     //

//     @Test
//     public void testCaseClass() throws Exception {

//         // case class SampleEntity の apply を使う
//         // scalaで同等なコードは以下のとおり
//         // val entity = SampleEntity(0, "abc")
//         SampleEntity entity = SampleEntity$.MODULE$.apply(0, "abc");

//         System.out.println("case class SampleEntity の apply を使う");
//         assertEquals(entity.id(), 0);
//         assertEquals(entity.data(), "abc");
//     }

//     @Test
//     public void testOptionSome() throws Exception {

//         SampleEntity entity = SampleEntity$.MODULE$.apply(0, "abc");

//         // Option[T]型の値(Some)を作る
//         // scalaで同等なコードは以下のとおり
//         // val param = Some(entity)
//         Option<SampleEntity> param = Option.apply(entity);

//         System.out.println("Option[T]型の値(Some)を作る");
//         assertEquals(param.get(), entity);
//     }

//     @Test
//     public void testCallFunctionInObject() throws Exception {

//         // trait を mixin した object の関数を呼び出す
//         // scalaで同等なコードは以下のとおり
//         // val result = SampleService.function1(Some(SampleEntity(0, "abc")))
//         Either<Exception,String> result = SampleService$.MODULE$.function1(Option.apply(SampleEntity$.MODULE$.apply(0, "abc")));

//         System.out.println("trait を mixin した object の関数を呼び出す");
//         assertEquals(result.isRight(), true);
//     }

//     @Test
//     public void testGetValueFromEitherRight() throws Exception {

//         // Either の Right から値を取り出す
//         // scalaで同等なコードは以下のとおり
//         // val param = Some(SampleEntity(0, "abc"))
//         // val result = SampleService.function1(param)
//         // val result_1_value: String = result_1 match {
//         //     case Right(value) => value
//         //     case Left(_) => null
//         // }
//         Option<SampleEntity> param = Option.apply(SampleEntity$.MODULE$.apply(0, "abc"));
//         Either<Exception,String> result = SampleService$.MODULE$.function1(param);
//         String result_value = null;
//         if( result.isRight() ) {
//             result_value = result.right().get();
//         }

//         System.out.println("Either の Right から値を取り出す");
//         assertEquals(param.get().data(), result_value);
//     }

//     @Test
//     public void testOptionNone() throws Exception {

//         // None値を作る
//         // scalaで同等なコードは以下のとおり
//         // val value: Option[SampleEntity] = None
//         Option<SampleEntity> value = None$.apply((SampleEntity) null);


//         System.out.println("None値を作る");
//         assertEquals(value.isEmpty(), true);
//     }

//     @Test
//     public void testGetValueFromEitherLeft() throws Exception {

//         // Either の Left から値を取り出す
//         // scalaで同等なコードは以下のとおり
//         // val result = SampleService.function1(None)
//         // val result_value: Exception = result match {
//         //     case Right(_) => null
//         //     case Left(value) => value
//         // }
//         Either<Exception,String> result = SampleService$.MODULE$.function1( None$.apply((SampleEntity)null) );
//         Exception result_value = null;
//         if( result.isLeft() ) {
//             result_value = result.left().get();
//         }

//         System.out.println("Either の Left から値を取り出す");
//         assertEquals(result_value.getMessage(), "Exception : value=None");
//     }

//     @Test
//     public void testGetValueFromOptionSome() throws Exception {

//         // 返り値の型が Option[T] の Some(t:T) を受け取る
//         // scalaで同等なコードは以下のとおり
//         // val result: Option[String] = SampleService.function2( SampleEntity(1, "def") )
//         // val result_value = result.getOrElse[String](null)
//         //    または、
//         // val result_value: String = result match {
//         //   case Some(value) => value
//         //   case None => null
//         // }
//         Option<String> result = SampleService$.MODULE$.function2(SampleEntity$.MODULE$.apply(1, "def"));
//         String result_value = null;
//         if ( !result.isEmpty() ) {
//             result_value = result.get();
//         }

//         System.out.println("返り値の型が Option[T] の Some(t:T) を受け取る");
//         assertEquals(result_value, "def");
//     }

//     @Test
//     public void testGetValueFromOptionNone() throws Exception {

//         // 返り値の型が Option[T] の None を受け取る
//         // scalaで同等なコードは以下のとおり
//         // val result: Option[String] = SampleService.function2( SampleEntity(2, null) )
//         // val result_value: String = result match {
//         //   case Some(value) => null
//         //   case None => "None"
//         // }
//         Option<String> result = SampleService$.MODULE$.function2( SampleEntity$.MODULE$.apply(2, null) );
//         String result_value = null;
//         if ( result.isEmpty() ) {
//             result_value = "None";
//         }

//         System.out.println("返り値の型が Option[T] の None を受け取る");
//         assertEquals(result_value, "None");
//     }
// }
