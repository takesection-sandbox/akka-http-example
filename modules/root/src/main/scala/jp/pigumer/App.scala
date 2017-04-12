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

class OneTimePassword extends Actor {

  override def receive = {
    case _ => {
      val now: Long = Instant.now().getEpochSecond
      val pincode = OneTimePasswordAlgorithm("test".getBytes(), now)
      sender ! s"""{"message":"${pincode}"}"""
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
      pathEndOrSingleSlash {
        get {
          complete {
            val future: Future[String] = (otp ? "foo").mapTo[String]
            future.map(s => HttpEntity(ContentTypes.`application/json`, s))
          }
        }
      }
    }

    Http().bindAndHandle(route, "0.0.0.0", 8080)
  }

}
