package blogtech.http

import blogtech.core.gitOps
import cats.effect.IO
import monix.eval.Task
import org.http4s.{Header, HttpService}
import org.http4s.dsl.Http4sDsl
import monix.execution.Scheduler.Implicits.global
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

/**
  * http://{host}/{username}
  */
object Overview extends Http4sDsl[IO] with Service {
  override val service: HttpService[IO] = HttpService[IO] {
    case req @ GET -> Root / username
         if username.endsWith(".json") =>
      def doLogic = {
        //get jwt from cookie
        val userNameOpt = HttpHelper.getUserNameFromReq(req)

        // 1. is owner - show all file
        // 2. is others - show only non-private
        val dirListFur = (userNameOpt match {
          case Some(uname) if uname == username => //owner
            gitOps.getAllFilesPath(username)
          case _ => //others
            gitOps.getPublicFilesPath(username)
        }).map(_.right.get)

        val rst = Task.fromFuture(dirListFur).toIO.flatMap { dirList =>
          def toUrlHref(dir: String) = s"""/$username/article/$dir"""

          val urlDirList = dirList.map(toUrlHref)

          Ok(
            compact(render(("status" -> 200) ~ ("data" -> urlDirList)))
          ).putHeaders(Header("Content-Type", "application/json"))

        }
        rst
      }

      HttpHelper.checkUserExistInDbOr404(username, _ => doLogic)

    // html page
    case req @ GET -> Root / username =>
      def doLogic = {
        //get jwt from cookie
        val userNameOpt = HttpHelper.getUserNameFromReq(req)

        // 1. is owner - show all file
        // 2. is others - show only non-private
        val dirListFur = (userNameOpt match {
          case Some(uname) if uname == username => //owner
            gitOps.getAllFilesPath(username)
          case _ => //others
            gitOps.getPublicFilesPath(username)
        }).map(_.right.get)

        val rst = Task.fromFuture(dirListFur).toIO.flatMap { dirList =>
          def toUrl(dir: String) = s"""<p><a href=/$username/article/$dir>$dir</a></p>"""

          val urlDirList = dirList.map(toUrl)

          Ok(
            s"""<!doctype html>
               |<html lang="en">
               |  <head>
               |    <meta charset="utf-8"/>
               |    <title>The HTML5 Herald</title>
               |  </head>
               |  <body>
               |    ${urlDirList.mkString("\n")}
               |  </body>
               |</html>
           """.stripMargin
          ).putHeaders(Header("Content-Type", "text/html"))

        }
        rst
      }

      HttpHelper.checkUserExistInDbOr404(username, _ => doLogic)
  }

}
