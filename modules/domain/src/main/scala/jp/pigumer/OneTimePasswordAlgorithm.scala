package jp.pigumer

import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import scala.util.Try

case class Algorithm(algorithm: String)

trait `Hash-basedMessageAuthenticationCode` {

  def hmac(key: Array[Byte], text: Array[Byte])(implicit algorithm: Algorithm): Try[Array[Byte]] = Try {
    val hmac = Mac.getInstance(algorithm.algorithm)
    val keySpec = new SecretKeySpec(key, "RAW")
    hmac.init(keySpec)
    hmac.doFinal(text)
  }
}

/**
  * @see https://tools.ietf.org/html/rfc4226
  */
object OneTimePasswordAlgorithm extends `Hash-basedMessageAuthenticationCode` {

  implicit val algorithm = Algorithm("HmacSHA1")

  // Truncate(HMAC(Key, Counter))
  def otp(key: Array[Byte], counter: Long): Try[String] = {
    for {
      hmac <- hmac(key, counter)
      truncated <- truncate(hmac)
    } yield truncated
  }
  
  def truncate(hs: Array[Byte]): Try[String] = Try {
    val offset = hs(19) & 0xF
    val binary: Array[Byte] = Seq[Byte](
      (hs(offset) & 0x7f).toByte,
      hs(offset + 1),
      hs(offset + 2),
      hs(offset + 3)).toArray
    val decimal = ByteBuffer.wrap(binary).getInt
    toString(decimal)
  }

  def toString(decimal: Int): String = {
    val otp = decimal % 1000000
    "%06d".format(otp)
  }

  implicit def toBytes(counter: Long): Array[Byte] =
    ByteBuffer.allocate(8).putLong(counter).array()
}
