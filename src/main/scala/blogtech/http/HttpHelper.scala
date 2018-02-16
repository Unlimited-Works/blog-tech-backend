package blogtech.http

import blogtech.core.JWTHelper
import blogtech.util.dao.userDao
import cats.effect.IO
import org.http4s.{Request, Response}
import org.http4s.dsl.io._
import org.http4s.headers.Cookie


trait HttpHelper {
  def checkUserExistInDbOr404(userName: String, onSuccess: userDao.UserData => IO[Response[IO]]): IO[Response[IO]] = {
    userDao.getUserByName(userName).flatMap{
      case None =>
        NotFound()
      case Some(userData) =>
        onSuccess(userData)
    }
  }

  def getUserNameFromReq(req: Request[IO]) = {
    val cookieOpt = req.headers.get(Cookie)

    //get jwt from cookie
    val userName = cookieOpt.flatMap { cki =>
      val jwtCki = cki.values.find(c => {
        c.name == HttpConstants.JwtCookie
      })

      val uname = jwtCki.flatMap { jwtc =>
        val token = jwtc.content
        JWTHelper.userName(token)
      }

      uname
    }

    userName
  }

//  def checkUserIsOwner
}

object HttpHelper extends HttpHelper
