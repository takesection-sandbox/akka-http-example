package jp.pigumer.boot

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat, RootJsonReader}

case class Foo(id: String, name: String)

object FooJson extends DefaultJsonProtocol with SprayJsonSupport {

  implicit object JsonFormat extends RootJsonReader[Foo] {

    override def read(json: JsValue): Foo = {
      json.asJsObject.getFields("id", "name") match {
        case Seq(JsString(id), JsString(name)) => Foo(id, name)
        case _ => throw new RuntimeException()
      }
    }
  }
}