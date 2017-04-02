package jp.pigumer

import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import scala.util.Try

trait HMAC {

  def hmac(key: Array[Byte], text: Array[Byte]): Try[Array[Byte]]

}

trait `HMAC-SHA1` extends HMAC {

  override def hmac(key: Array[Byte], text: Array[Byte]): Try[Array[Byte]] = Try {
    val hmac = Mac.getInstance("HmacSHA1")
    val keySpec = new SecretKeySpec(key, "RAW")
    hmac.init(keySpec)
    hmac.doFinal(text)
  }
}

trait `HMAC-SHA256` extends HMAC {

  override def hmac(key: Array[Byte], text: Array[Byte]): Try[Array[Byte]] = Try {
    val hmac = Mac.getInstance("HmacSHA256")
    val keySpec = new SecretKeySpec(key, "RAW")
    hmac.init(keySpec)
    hmac.doFinal(text)
  }
}

trait `HMAC-SHA512` extends HMAC {

  override def hmac(key: Array[Byte], text: Array[Byte]): Try[Array[Byte]] = Try {
    val hmac = Mac.getInstance("HmacSHA512")
    val keySpec = new SecretKeySpec(key, "RAW")
    hmac.init(keySpec)
    hmac.doFinal(text)
  }
}

/**
  * @see https://tools.ietf.org/html/rfc4226
  */
object OneTimePasswordAlgorithm extends `HMAC-SHA1` {

  // Truncate(HMAC-SHA1(Key, Counter))
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
