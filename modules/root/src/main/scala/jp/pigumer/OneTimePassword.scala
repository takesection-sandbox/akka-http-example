package jp.pigumer

import java.time.Instant

import akka.actor.SupervisorStrategy.Escalate
import akka.actor.{Actor, OneForOneStrategy}

class OneTimePassword extends Actor {

  override def supervisorStrategy = OneForOneStrategy() {
    case _ => Escalate
  }

  override def receive = {
    case "foo" => throw new RuntimeException()
    case _ => {
      val now: Long = Instant.now().getEpochSecond
      val pincode = OneTimePasswordAlgorithm("test".getBytes(), now).fold(
        t => throw t,
        r => sender ! s"""{"message":"${r}"}"""
      )
    }
  }
}
