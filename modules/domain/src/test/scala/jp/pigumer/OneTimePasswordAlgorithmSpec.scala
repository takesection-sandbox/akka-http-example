package jp.pigumer

import jp.pigumer.OneTimePasswordAlgorithm._
import org.specs2.mutable.Specification

class OneTimePasswordAlgorithmSpec extends Specification {

  val secret = "12345678901234567890"

  "OneTimePasswordAlgorithm" should {

    "hmacSha1" in {
      val key = secret.getBytes
      val counter: Array[Byte] = 0l

      val bytes = OneTimePasswordAlgorithm.hmac(key, counter)

      bytes.get(0) must_== 0xcc.toByte
    }

    "truncate" in {
      val key = secret.getBytes
      val counter: Array[Byte] = 0l

      val otp = for {
        bytes <- OneTimePasswordAlgorithm.hmac(key, counter)
        otp <- OneTimePasswordAlgorithm.truncate(bytes)
      } yield otp

      otp.get must_== "755224"

      val s = OneTimePasswordAlgorithm.toString(123)
      s must_== "000123"
    }

    "otp" in {
      val key = secret.getBytes
      val counter = 0l

      OneTimePasswordAlgorithm.otp(key, counter).get must_== "755224"
    }
  }
}
