package blogtech.http

import blogtech.dao.UserDao
import cats.effect.IO
import org.http4s.Response
import org.http4s.dsl.io._


trait HttpHelper {
  def checkUserExistInDbOr404(userName: String, onSuccess: UserDao.UserData => IO[Response[IO]]): IO[Response[IO]] = {
    UserDao.getUserByName(userName).flatMap{
      case None =>
        NotFound.apply()
      case Some(userData) =>
        onSuccess(userData)
    }
  }
}

object HttpHelper extends HttpHelper
