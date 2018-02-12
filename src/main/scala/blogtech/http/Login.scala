package blogtech.http

import blogtech.util.JWTHelper
import org.http4s.{Header, HttpDate, HttpService, Response}
import cats.effect.IO
import cats.implicits._
import org.http4s.dsl.Http4sDsl
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.slf4j.LoggerFactory

/**
  * login and register API
  */
object Login extends Http4sDsl[IO] with Service {
  private val logger = LoggerFactory.getLogger(getClass)

  private implicit val format: DefaultFormats = org.json4s.DefaultFormats

  case class LoginReq(account: String, password: String)
  case class RegisterReq(email: String, userName: String, password: String)

  override val service: HttpService[IO] = HttpService[IO] {
    case GET -> Root  =>
      //return login page
      Ok("login page")
        .putHeaders(Header("Content-Type", "text/html"))
    case req @ POST -> Root / "login.json" =>
      val userData = req.as[String]

      val rst = userData.flatMap{jsonStr =>
        val loginData = parse(jsonStr).extract[LoginReq]
        logger.info("login data - " + loginData)

        val verifyLogin = blogtech.core.userOps.verifyUserPassword(loginData.account, loginData.password)
        verifyLogin flatMap {
          case true =>
            Ok(compact(render("status" -> 200)))
              .addCookie(
                Http.jwtCookie,
                JWTHelper.create(JWTHelper.PayLoad(loginData.account)),
                Some(HttpDate.unsafeFromEpochSecond(System.currentTimeMillis() / 1000L + 60 * 60 * 24 * 365L))
              ).putHeaders(Header("Content-Type", "application/json"))

          case false =>
            Ok(compact(render(
              ("status" -> 400) ~
              ("reason" -> "user_not_exist_or_password_not_match")
            ))).putHeaders(Header("Content-Type", "application/json"))

        }
      }

      rst
    case req @ POST -> Root / "register.json" =>
      val response: IO[IO[Response[IO]]] = for{
        registerStr <- req.as[String]
        registerData = parse(registerStr).extract[RegisterReq]
        rst <- blogtech.core.userOps
              .createAccount(registerData.userName, registerData.email, registerData.password)

      } yield {
        rst match {
          case None =>
            Ok(compact(render(
              ("status" -> 400) ~
                ("reason" -> "user_has_exist")
            ))).putHeaders(Header("Content-Type", "application/json"))
          case Some(oid) =>
            Ok(compact(render(
              ("status" -> 200) ~
                ("oid" -> oid)
            ))).putHeaders(Header("Content-Type", "application/json"))
        }
      }

      response.flatten
  }

}
