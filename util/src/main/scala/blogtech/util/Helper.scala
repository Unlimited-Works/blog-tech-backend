package blogtech.util

/**
  *
  */
object Helper {
//  def toMd5(str: String): String = {
//    import java.security.MessageDigest
//    val digest = MessageDigest.getInstance("MD5")
//    digest.digest(str.getBytes).map("%02x".format(_)).mkString
//  }

  def toSHA256(str: String): String = {
    import java.nio.charset.StandardCharsets
    import java.security.MessageDigest
    import java.util.Base64
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(str.getBytes(StandardCharsets.UTF_8))
    val encoded = Base64.getEncoder.encodeToString(hash)
    encoded
  }

}
