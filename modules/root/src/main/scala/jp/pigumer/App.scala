package jp.pigumer

import java.time.Instant

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._

import jp.pigumer.common._

import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

class OneTimePassword extends Actor {

  override def receive = {
    case "foo" => sender ! Left(new RuntimeException())
    case _ => {
      val now: Long = Instant.now().getEpochSecond
      val pincode = OneTimePasswordAlgorithm("test".getBytes(), now).fold(
        t => sender ! Left(t),
        r => sender ! Right(s"""{"message":"${r}"}""")
      )
    }
  }
}

object App {

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
                    val future: Future[Either[Throwable, String]] = (otp ? key).mapTo[Either[Throwable, String]]
                    complete {
                      future.map(res =>
                        res.fold(
                          l => throw l,
                          s => HttpEntity(ContentTypes.`application/json`, s)))
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
