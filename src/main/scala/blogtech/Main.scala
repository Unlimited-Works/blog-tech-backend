package blogtech

import blogtech.core.BlogConfig
import cats.effect.IO
import fs2.StreamApp
import fs2.StreamApp.ExitCode
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder
import scala.concurrent.ExecutionContext.Implicits.global

/**
  *
  */
object Main extends StreamApp[IO] {

   /**
    * check and init environment:
    *   - git repos path
    */
  BlogConfig

  //get all service
  val services: HttpService[IO] = blogtech.http.Http.httpService

  def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(BlogConfig.httpEnv.port, "0.0.0.0")
      .mountService(services, "/")
      .serve

}
