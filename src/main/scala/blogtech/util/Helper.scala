package blogtech.util

import laika.api.Transform
import laika.parse.markdown.Markdown
import laika.render.HTML

/**
  *
  */
object Helper {
  def toMd5(str: String): String = {
    import java.security.MessageDigest
    val digest = MessageDigest.getInstance("MD5")
    digest.digest(str.getBytes).map("%02x".format(_)).mkString
  }


  def md2Html(mdStr: String) = {
    Transform from Markdown to HTML fromString mdStr toString
  }
}
