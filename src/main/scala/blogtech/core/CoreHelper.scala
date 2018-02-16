package blogtech.core

import laika.api.Transform
import laika.parse.markdown.Markdown
import laika.render.HTML

/**
  *
  */
object CoreHelper {
  def md2Html(mdStr: String) = {
    Transform from Markdown to HTML fromString mdStr toString
  }
}
