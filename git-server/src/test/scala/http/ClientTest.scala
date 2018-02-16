package http

import cats.effect.IO
import org.junit.Test

import org.http4s.Uri
import org.http4s.client.blaze._
import org.http4s.client.Client

/**
  *
  */
class ClientTest {
  val httpClient: Client[IO] = Http1Client[IO]().unsafeRunSync

  @Test
  def hello(): Unit = {
    def hello(name: String): IO[String] = {
      val target = Uri.uri("http://localhost:28080/hello") / name
      httpClient.expect[String](target)
    }

    println(hello("foo").unsafeRunSync)
  }

  @Test
  def proxy(): Unit = {
//    val httpClient: Client[IO] = Http1Client[IO]()
//
//    for {
//      client <- httpClient
//      newAuthority = req.uri.authority.map(_.copy(host = RegName("scala-lang.org"), port = Some(80)))
//      proxiedReq = req.withUri(req.uri.copy(authority = newAuthority))
//      response <- client.fetch(proxiedReq)(IO.pure(_))
//    } yield {
//
//    }
  }

  @Test
  def scalalangTest(): Unit = {
    val target = Uri.uri("http://scala-lang.org")
    val rst =  httpClient.expect[String](target)

    println(rst.unsafeRunSync())
  }
}
