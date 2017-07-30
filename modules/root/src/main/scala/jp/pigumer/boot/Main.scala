package jp.pigumer.boot

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import jp.pigumer.OneTimePassword
import jp.pigumer.common.{auth, exceptionHandler}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.collection.immutable.Seq

trait AkkaApplication {
  import FooJson._

  val log = LoggerFactory.getLogger(this.getClass)

  def createActorSystem = ActorSystem("example")

  implicit val system = createActorSystem
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout: Timeout = 5 seconds

  val otp = system.actorOf(Props[OneTimePassword])

  val corsSettings = CorsSettings.defaultSettings.copy(
    allowedMethods = Seq(GET, POST, HEAD, PUT, DELETE, OPTIONS)
  )

  def route: Route = {
    handleExceptions(exceptionHandler) {
      pathEndOrSingleSlash {
        auth { account =>
          cors(corsSettings) {
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

  def bindAndHandle = Http().bindAndHandle(route, "0.0.0.0", 8080)
}

object Main extends App {
  val log = LoggerFactory.getLogger(this.getClass)

  val app = new AkkaApplication {}
  val binding = app.bindAndHandle

  implicit val system = app.system
  implicit val executionContext = app.executionContext

  Runtime.getRuntime.addShutdownHook(new Thread {
    override def run() {
      binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
      log.info("Shutting down...")
    }
  })

}