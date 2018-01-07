package blogtech.http

import io.circe.Json
import org.http4s.HttpService
import cats.effect.IO
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

/**
  * login and register API
  */
object Login extends Http4sDsl[IO] with Service {
  override val service: HttpService[IO] = HttpService[IO] {
    case GET -> Root / "hello" / name =>
      Ok(Json.obj("message" -> Json.fromString(s"Hello, ${name}")))
  }
}
