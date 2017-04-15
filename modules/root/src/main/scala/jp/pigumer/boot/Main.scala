package jp.pigumer.boot

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, handleExceptions, parameters, pathEndOrSingleSlash}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import jp.pigumer.OneTimePassword
import jp.pigumer.common.{auth, exceptionHandler}

import scala.concurrent.Future
import scala.concurrent.duration._
import akka.pattern._

object Main {

  def main(args: Array[String]) {

    implicit val system = ActorSystem("example")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    implicit val timeout: Timeout = 5 seconds

    val otp = system.actorOf(Props[OneTimePassword])

    val route = {
      auth { account =>
        cors() {
          handleExceptions(exceptionHandler) {
            pathEndOrSingleSlash {
              get {
                parameters('key) { key =>
                  {
                    val future: Future[String] = (otp ? key).mapTo[String]
                    complete {
                      future.map(s =>
                        HttpEntity(ContentTypes.`application/json`, s))
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    Http().bindAndHandle(route, "0.0.0.0", 8080)
  }

}
