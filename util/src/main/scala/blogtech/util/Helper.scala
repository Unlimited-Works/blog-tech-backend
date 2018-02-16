package blogtech.util

/**
  *
  */
object Helper {
  def toMd5(str: String): String = {
    import java.security.MessageDigest
    val digest = MessageDigest.getInstance("MD5")
    digest.digest(str.getBytes).map("%02x".format(_)).mkString
  }

}
