package jp.pigumer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

object App {

  def main(args: Array[String]) {
    implicit val system = ActorSystem("example")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route = {
      pathEndOrSingleSlash {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Hello World</h1>"))
        }
      }
    }

    Http().bindAndHandle(route, "0.0.0.0", 8080)
  }

}
