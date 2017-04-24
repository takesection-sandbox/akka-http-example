package jp.pigumer

import java.time.Instant

import akka.actor.{Actor, ActorLogging}

class OneTimePassword extends Actor with ActorLogging {

  override def receive = {
    case "error" => sender ! Left(new RuntimeException())
    case key: String => {
      log.info(s"start: $key")

      val now: Long = Instant.now().getEpochSecond / 30
      val result = OneTimePasswordAlgorithm(key.getBytes(), now).fold(
        t => Left(t),
        r => Right(s"""{"message":"${r}"}""")
      )

      log.info(s"end: $key")
      sender ! result
    }
  }

}
