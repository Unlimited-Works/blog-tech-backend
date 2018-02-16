package blogtech.http

import blogtech.core.CoreHelper
import blogtech.util.Helper
import cats.effect.IO
import monix.eval.Task
import org.http4s.dsl.Http4sDsl
import org.http4s.{Header, HttpService, Request, Response}
import monix.execution.Scheduler.Implicits.global
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

/**
  * http://{host}/{username}
  */
object Article extends Http4sDsl[IO] with Service {
  override val service: HttpService[IO] = HttpService[IO] {
    // html page
    case req @ GET -> username /: "article" /: filePath
      if filePath.lastOption.fold(false)(_.endsWith(".json")) =>

      def doLogic = {
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

      }
      checkCanView(req, filePath, username, doLogic)

    case req @ GET -> username /: "article" /: filePath  =>
      def doLogic = {
        val filPathStr = filePath.toString.tail //ignore '/' at beginning
        val articleContent = {
          blogtech.core.gitOps.getFile(username, filPathStr)
        }

        def tmplate(content: String, dealContent: String => String) = {
          s"""<!doctype html>
             |<html lang="en">
             |  <head>
             |    <meta charset="utf-8"/>
             |    <title>The HTML5 Herald</title>
             |  </head>
             |  <body>
             |    ${dealContent(content)}
             |  </body>
             |</html>
             """.stripMargin
        }

        Task.fromFuture(articleContent).map{
          case Right(content) if filPathStr.endsWith(".md") =>
            tmplate(content, CoreHelper.md2Html)
          case Right(content) =>
            tmplate(content, x => x)
          case Left(error) =>
            tmplate(error.toString, x => x)
        }
          .toIO
          .flatMap(x =>
            Ok(x).putHeaders(Header("Content-Type", "text/html"))
          )

      }

      checkCanView(req, filePath, username, doLogic)

  }

  def checkCanView(req: Request[IO], filePath: Path, userName: String, isSuccess: => IO[Response[IO]]) = {
    // 是否是private文件夹下的文件
    val isPrivate = filePath.startsWith(Path("private"))

    //是否有权限浏览
    val canView = isPrivate match {
      case true =>
        val userNameOpt = HttpHelper.getUserNameFromReq(req)
        userNameOpt.fold(false)(_ == userName)
      case false =>
        true
    }

    canView match {
      case true =>
        isSuccess
      case false =>
        NotFound()
    }

  }
}
