package blogtech.http

import java.util.Date

import blogtech.util.JWTHelper
import blogtech.core.gitOps
import cats.effect.IO
import com.auth0.jwt.exceptions.JWTVerificationException
import monix.eval.Task
import org.http4s.{Header, HeaderKey, HttpService, Response}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Cookie
import org.http4s.util.CaseInsensitiveString
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import collection.convert.ImplicitConversions._
import monix.execution.Scheduler.Implicits.global

/**
  * http://{host}/{username}
  */
object Overview extends Http4sDsl[IO] with Service {
  import com.auth0.jwt.JWT
  import com.auth0.jwt.algorithms.Algorithm
  val expireAt = System.currentTimeMillis() + 30 * 60 * 1000
  val userId = "userid0"
  val algorithm = Algorithm.HMAC256("sec")

//  val x = JWT.create
//    .withHeader(Map("typ" -> "JWT"))
//    .withExpiresAt(new Date(expireAt))
//    .withClaim("userId", userId)
//    .sign(algorithm)

  val verifier = JWT.require(algorithm).build
//  val y = verifier.verify(x)

  override val service: HttpService[IO] = HttpService[IO] {
    // html page
    case req @ GET -> Root / username =>
      //todo check username exist in database, return 404 if not exist


      val cookieOpt = req.headers.get(Cookie)

      //get jwt from cookie
      val userInfo = cookieOpt.flatMap{cki =>
        val jwtCki = cki.values.find(c => {
          c.name == Http.jwtCookie
        })

        val uname = jwtCki.flatMap{jwtc =>
          val token = jwtc.content
          JWTHelper.userName(token)
        }

        uname
      }

      // 1. is owner - show all file
      // 2. is others - show only non-private
      val dirListFur = (userInfo match {
        case _ => //test owner
          gitOps.getAllFilesPath(username)
        case Some(uname) if uname == username => //owner
          gitOps.getAllFilesPath(username)
        case _ => //others
          gitOps.getPublicFilesPath(username)
      }).map(x => x.right.get.split('\n').toList)

      val rst = Task.fromFuture(dirListFur).toIO.flatMap{dirList =>
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

//    // public blog list
//    case GET -> Root / username / "api" / "article" / "list.json" =>
//      val dirList: List[String] = ???
//      Ok(compact(render(
//        ("status " -> 200) ~
//        ("data" -> dirList)
//      ))).putHeaders(Header("Content-Type", "text/json"))


  }
}
