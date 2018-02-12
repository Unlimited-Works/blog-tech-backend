package blogtech.http

import cats.effect.IO
import monix.eval.Task
import org.http4s.dsl.Http4sDsl
import org.http4s.{Header, HttpService}
import monix.execution.Scheduler.Implicits.global
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

/**
  * http://{host}/{username}
  */
object Article extends Http4sDsl[IO] with Service {
  override val service: HttpService[IO] = HttpService[IO] {
    // html page
    case GET -> username /: "article" /: filePath
      if filePath.lastOption.fold(false)(_.endsWith(".json")) =>

      val articleContent = {
        val filPathStr = filePath.toString.tail //ignore '/' at beginning
        blogtech.core.gitOps.getFile(username, filPathStr.take(filPathStr.length - 5)) //remove ".json"
      }

      Task.fromFuture(articleContent)
        .map{
          case Right(content) =>
            compact(render(("result" -> 200) ~ ("data" -> content)))
          case Left(error) =>
            compact(render(("result" -> 400) ~ ("data" -> error.toString)))
        }
        .toIO
        .flatMap(x =>
          Ok(x).putHeaders(Header("Content-Type", "application/json"))
        )

    case GET -> username /: "article" /: filePath  =>
      val articleContent = {
        val filPathStr = filePath.toString.tail //ignore '/' at beginning
        blogtech.core.gitOps.getFile(username, filPathStr)
      }

      def tmplate(content: String) = {
        s"""<!doctype html>
           |<html lang="en">
           |  <head>
           |    <meta charset="utf-8"/>
           |    <title>The HTML5 Herald</title>
           |  </head>
           |  <body>
           |    <p>$content</p>
           |  </body>
           |</html>
             """.stripMargin
      }

      Task.fromFuture(articleContent).map{
        case Right(content) =>
          tmplate(content)
        case Left(error) =>
          tmplate(error.toString)
      }
      .toIO
      .flatMap(x =>
        Ok(x).putHeaders(Header("Content-Type", "text/html"))
      )

  }
}
