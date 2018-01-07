package blogtech

import cats.effect.IO
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp

/**
  *
  */
object Main extends StreamApp[IO] {
  /**
    * load config
    */

  /**
    * check and init environment:
    *   - git repos path
    *
    */

  //get all service
  val services = blogtech.http.Http.httpService

  def stream(args: List[String], requestShutdown: IO[Unit]) =
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .mountService(services, "/")
      .serve

}
