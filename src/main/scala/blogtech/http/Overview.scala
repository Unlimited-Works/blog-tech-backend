package blogtech.http

import cats.effect.IO
import io.circe.Json
import org.http4s.{Header, HttpService}
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

/**
  * http://{host}/{username}
  */
object Overview extends Http4sDsl[IO] with Service {
  override val service: HttpService[IO] = HttpService[IO] {
    // html page
    case GET -> Root / username =>
      Ok(
        s"""<!doctype html>
           |<html lang="en">
           |  <head>
           |    <meta charset="utf-8"/>
           |    <title>The HTML5 Herald</title>
           |  </head>
           |  <body>
           |    <p>hello, $username</p>
           |    </body>
           |</html>
         """.stripMargin
      ).putHeaders(Header("Content-Type", "text/html"))

    // public blog list
    case GET -> Root / username / "list.json" =>
      Ok(Json.obj(
        "status " -> Json.fromInt(200),
        "data" -> Json.fromString(s"Hello2, $username")
      ))


  }
}
