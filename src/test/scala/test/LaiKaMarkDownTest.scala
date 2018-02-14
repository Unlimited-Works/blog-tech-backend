package test

import org.junit.Test
import laika.api.Transform
import laika.parse.markdown.Markdown
import laika.render.HTML

/**
  *
  */
class LaiKaMarkDownTest {

  @Test
  def renderToHtml(): Unit = {
    val input = "some *text* example"
    val result = Transform from Markdown to HTML fromString input toString

    println(result)
  }


}
