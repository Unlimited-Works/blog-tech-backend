package blogtech.http

import cats.effect.IO
import monix.eval.Task
import org.http4s.dsl.Http4sDsl
import org.http4s.{Header, HttpService}
import monix.execution.Scheduler.Implicits.global

/**
  * http://{host}/{username}
  */
object Article extends Http4sDsl[IO] with Service {
  override val service: HttpService[IO] = HttpService[IO] {
    // html page
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
