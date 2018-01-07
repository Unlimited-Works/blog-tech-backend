package blogtech.http

import cats.effect.IO
import io.circe.Json
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

/**
  *
  */
object Login2 extends Http4sDsl[IO] with Service {
  override val service: HttpService[IO] = HttpService[IO] {
    case GET -> Root / "hello2" / name =>
      Ok(Json.obj("message" -> Json.fromString(s"Hello2, ${name}")))
  }
}
