package blogtech.http

import org.http4s.{Header, HttpService}
import cats.effect.IO
import org.http4s.dsl.Http4sDsl
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

/**
  * login and register API
  */
object Login extends Http4sDsl[IO] with Service {

  override val service: HttpService[IO] = HttpService[IO] {
    case GET -> Root  =>
      //return login page
      Ok("login page")
        .putHeaders(Header("Content-Type", "text/html"))
    case POST -> Root / "login.json" =>
      Ok(compact(render("status" -> 200)))

  }
}
