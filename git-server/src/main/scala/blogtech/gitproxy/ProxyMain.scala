package blogtech.gitproxy

import cats.effect.IO
import fs2.StreamApp
import fs2.StreamApp.ExitCode
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext.Implicits.global

/**
  *
  */
object ProxyMain extends StreamApp[IO] {
  /**
    * load config
    */

  /**
    * check and init environment:
    *   - git repos path
    *
    */

  //get all service
  val services: HttpService[IO] = blogtech.gitproxy.http.Http.httpService
  def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(8082, "0.0.0.0")
      .mountService(services, "/")
      .serve

}
