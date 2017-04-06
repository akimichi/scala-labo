package org.example.call.from.java


// このscalaで実装したコードを、CallScalaFunctionFromJavaTestでJavaから呼び出す

case class SampleEntity(val id: Int, val data: String)

trait Service1 {
  def function1(param: Option[SampleEntity]): Either[Exception, String] = {
    param match {
      case Some(value) => Right(value.data)
      case None => Left(new Exception("Exception : value=None"))
    }
  }
}


trait Service2 {
  def function2(param: SampleEntity): Option[String] = {
    if ( param.data != null ) {
      Some(param.data)
    } else {
      None
    }
  }
}

object SampleService extends Service1 with Service2