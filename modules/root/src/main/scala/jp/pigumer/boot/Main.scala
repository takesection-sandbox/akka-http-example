package jp.pigumer.boot

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import jp.pigumer.OneTimePassword
import jp.pigumer.common.{auth, exceptionHandler}
import org.slf4j.LoggerFactory
import spray.json._

import scala.concurrent.duration._

object Main {
  import FooJson._

  val log = LoggerFactory.getLogger(this.getClass)

  implicit val system = ActorSystem("example")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout: Timeout = 5 seconds

  val otp = system.actorOf(Props[OneTimePassword])

  def route: Route = {
    handleExceptions(exceptionHandler) {
      pathEndOrSingleSlash {
        auth { account =>
          cors() {
            get {
              parameters('key) { key =>
                complete {
                  (otp ? key).mapTo[Either[Throwable, String]].map[ToResponseMarshallable] {
                    case Left(t) => throw t
                    case Right(s) => HttpEntity(ContentTypes.`application/json`, s)
                  }
                }
              }
            } ~ post {
              entity(as[Foo]) { foo =>
                complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, foo.name))
              }
            }
          }
        }
      }
    }
  }

  def main(args: Array[String]) {

    log.info("main")

    Http().bindAndHandle(route, "0.0.0.0", 8080)
  }

}
