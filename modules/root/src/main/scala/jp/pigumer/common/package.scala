package jp.pigumer

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.{HttpChallenge, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.{AuthenticationDirective, AuthenticationResult}

import scala.concurrent.Future

package object common {

  case class Account(id: String)

  val auth: AuthenticationDirective[Account] = {
    authenticateOrRejectWithChallenge[OAuth2BearerToken, Account] {
      case Some(OAuth2BearerToken(token)) if token == "123" =>
        Future.successful(AuthenticationResult.success(Account("user")))
      case _ =>
        Future.successful(AuthenticationResult.failWithChallenge(
          HttpChallenge("bearer", None, Map("error" -> "invalid_token")))
        )
    }
  }

  val exceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: Exception => extractUri { uri =>
      complete(HttpResponse(InternalServerError))
    }
  }

}
