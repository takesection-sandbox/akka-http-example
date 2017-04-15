package jp.pigumer

import akka.http.scaladsl.model.headers.{HttpChallenge, OAuth2BearerToken}
import akka.http.scaladsl.server.directives.{AuthenticationDirective, AuthenticationResult}

import scala.concurrent.Future
import akka.http.scaladsl.server.Directives._

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
}
