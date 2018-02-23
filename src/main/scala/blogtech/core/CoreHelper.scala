package blogtech.core

//import laika.api.Transform
//import laika.parse.markdown.Markdown
//import laika.render.HTML
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

/**
  *
  */
object CoreHelper {
  val parser = Parser.builder().build()
  val renderer = HtmlRenderer.builder().build()

  def md2Html(mdStr: String) = {
//    Transform from Markdown to HTML fromString mdStr toString
    val document = parser.parse(mdStr)
    renderer.render(document)
  }
}
