package test

import org.junit.Test
import laika.api.Transform
import laika.parse.markdown.Markdown
import laika.render.{HTML, HTMLWriter}
import laika.tree.Elements.{Emphasized, RenderFunction}

/**
  *
  */
class LaiKaMarkDownTest {
  val input =
    """
      |# Learning Idris(01)
      |## Setup environment
      |在Mac上使用homebrew可以简单的安装Idris, `brew install idris`， 终端输入`idris`即可进入REPL。
      |```
      |     ____    __     _
      |    /  _/___/ /____(_)____
      |    / // __  / ___/ / ___/     Version 1.1.1
      |  _/ // /_/ / /  / (__  )      http://www.idris-lang.org/
      | /___/\__,_/_/  /_/____/       Type :? for help
      |
      |Idris is free software with ABSOLUTELY NO WARRANTY.
      |For details type :warranty.
      |idris: Network.Socket.bind: resource busy (Address already in use)
      |Idris>
      |```
      |
      |### 写一个helloworld
      |- 创建一个`hello.idr`文件
      |
      |```
      |module Main
      |
      |main : IO ()
      |main = putStrLn "Hello world"
      |```
      |
      |- 使用idris编译hello.idr成名字为hello的可执行文件：
      |```
      |$ idris hello.idr -o hello
      |$ ./hello
      |"Hello world"
      |```
    """.stripMargin

  @Test
  def renderToHtml(): Unit = {
//    val input = "some *text* example"

    val result = Transform from Markdown.strict to HTML fromString input toString

    println(result)
  }


//  def customRender(): Unit = {
//    val open = """<em class="big">"""
//    val close = "</em>"
//
//    val renderer: HTMLWriter => RenderFunction = { out =>
//    { case Emphasized(content, _) => out << open << content << close }
//    }
//  }

  @Test
  def javaMd(): Unit = {
    import org.commonmark.node._
    import org.commonmark.parser.Parser
    import org.commonmark.renderer.html.HtmlRenderer

    val parser = Parser.builder().build()
    val document = parser.parse(input)
    val renderer = HtmlRenderer.builder().build()
    println(renderer.render(document))  // "<p>This is <em>Sparta</em></p>\n"
  }
}
