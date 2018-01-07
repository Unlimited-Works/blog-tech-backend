package blogtech

import io.circe.Json
import org.http4s.HttpService
import cats.effect.IO
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp

/**
  *
  */
object Main extends StreamApp[IO] with Http4sDsl[IO] {
  val services = blogtech.http.Http.httpService

  def stream(args: List[String], requestShutdown: IO[Unit]) =
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .mountService(services, "/")
      .serve

}
