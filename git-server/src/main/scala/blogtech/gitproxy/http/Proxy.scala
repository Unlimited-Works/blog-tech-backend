package blogtech.gitproxy.http

import blogtech.gitproxy.ProxyConfig
import org.http4s.{Header, Headers, HttpService, Status}
import cats.effect.IO
import org.http4s.Uri.{Authority, RegName}
import org.http4s.client.blaze.Http1Client
import org.http4s.dsl.Http4sDsl
import org.http4s.util.CaseInsensitiveString

object Proxy extends Http4sDsl[IO] with Service {
  private val httpClient = Http1Client[IO]()//.map(_.toHttpService)

  val newHost = ProxyConfig.proxyData.host
  val port = ProxyConfig.proxyData.port

  override val service: HttpService[IO] = HttpService[IO] {
    case req =>
//      val request = req
//      println(s"request: $request")
//      println(s"request attributes: ${request.attributes.entries}")
//      println(s"request body: ${
//        val x = request.body.fold(List.empty[Byte]) { case (acc, str) => str :: acc }.
//          compile.fold(List.empty[Byte]){case (a, b) => {
//            a ::: b
//          }
//        }
//        new String( x.unsafeRunSync.toArray)}")
//      println(s"request headers: ${request.headers}")
//      println(s"request httpVersion: ${request.httpVersion}")
//      println(s"request method: ${request.method}")

      def doClient = {
        for {
          client <- httpClient
          newAuthority = Authority(host = RegName(newHost), port = Some(port))
          newHeaders: Headers = {
            val filterHeaders = req.headers.filterNot{h =>
              h.name == CaseInsensitiveString("Connection") ||
                h.name == CaseInsensitiveString("Keep-Alive") ||
                h.name == CaseInsensitiveString("Proxy-Authenticate") ||
                h.name == CaseInsensitiveString("Proxy-Authorization") ||
                h.name == CaseInsensitiveString("TE") ||
                h.name == CaseInsensitiveString("Trailer") ||
                h.name == CaseInsensitiveString("Transfer-Encoding") ||
                h.name == CaseInsensitiveString("Upgrade")

            }

            filterHeaders.put(Header("Host", newHost))
          }
          proxiedReq = req.withUri(req.uri.copy(authority = Some(newAuthority)))
            .withHeaders(newHeaders)
          response <- {
            client.fetch(proxiedReq)(x => IO.pure(x))
          }
        } yield {
          response
        }
      }
      //header中有Authorization字段，验证值是否有效（通过请求的路径和账号密码这三个字段验证）
      //header无Authorization字段时，返回一个：
        /**
          * Header("WWW-Authenticate", """Basic realm="Secure Area"""")
          * Status(401)
          */
      req.headers.get(CaseInsensitiveString("Authorization")) match {
        case Some(Header(_, authValue)) =>
//          println("Some(header) - ")
          val base64 = authValue.split(" ").last
          val decodeBasr64 = new String(java.util.Base64.getDecoder.decode(base64.getBytes))
          val List(userName, password) = decodeBasr64.split(":").toList

          //path:
          val userPath = req.uri.path.split("/").apply(1)
//          println("deal_Authorization: " + (base64, decodeBasr64, userName, password, userPath))

          if(userName == userPath) {
            val verifyPassword = {
              blogtech.util.dao.userDao.getUserByName(userName).flatMap{
                case None =>
                  Ok().withStatus(Status(401))
                case Some(userDt) =>
                  if(blogtech.util.Helper.toMd5(password) == userDt.password)
                    doClient
                  else
                    Ok().withStatus(Status(401))
              }
            }
            verifyPassword
//            if(password == "admin")
////            verify success. and get real data form proxy
//              doClient
//            else
//              Ok().withStatus(Status(401))
          } else {
            //verify fail
            Ok().withStatus(Status(401))
          }
        case None => //not contains authentication header info
//          println("None(header) - ")

          //response server need a authentication
          Ok().putHeaders(Header("WWW-Authenticate", """Basic realm="Secure Area""""))
              .withStatus(Status(401))
      }

  }
}
