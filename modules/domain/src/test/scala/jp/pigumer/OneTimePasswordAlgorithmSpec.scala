package jp.pigumer

import org.specs2.mutable.Specification
import OneTimePasswordAlgorithm._

class OneTimePasswordAlgorithmSpec extends Specification {

  val secret = "12345678901234567890"

  "OneTimePasswordAlgorithm" should {

    "hmacSha1" in {
      val key = secret.getBytes
      val counter: Array[Byte] = 0l

      val bytes = OneTimePasswordAlgorithm.hmacSha1(key, counter)

      bytes.get(0) must_== 0xcc.toByte
    }

    "truncate" in {
      val key = secret.getBytes
      val counter: Array[Byte] = 0l

      val otp = for {
        bytes <- OneTimePasswordAlgorithm.hmacSha1(key, counter)
        otp <- OneTimePasswordAlgorithm.truncate(bytes)
      } yield otp

      otp.get must_== "755224"

      val s = OneTimePasswordAlgorithm.toString(123)
      s must_== "000123"
    }
  }
}
