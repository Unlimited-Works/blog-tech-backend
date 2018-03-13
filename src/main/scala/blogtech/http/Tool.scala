package blogtech.http

import blogtech.core.JWTHelper
import blogtech.util.Helper
import cats.effect.IO
import cats.implicits._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.http4s.dsl.Http4sDsl
import org.http4s.{Header, HttpDate, HttpService}
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JString
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.slf4j.LoggerFactory

/**
  * login and register API
  */
object Tool extends Http4sDsl[IO] with Service {
  private val logger = LoggerFactory.getLogger(getClass)

  private implicit val format: DefaultFormats = org.json4s.DefaultFormats

  case class SHAReq(password: String)

  case class RegisterReq(email: String, userName: String, password: String)

  override val service: HttpService[IO] = HttpService[IO] {
    case GET -> Root / "tool" / "sha256" =>
      //todo need login: almost service should be login user

      //return login page
      Ok(
        """
          |<!doctype html>
          |<html lang="zh-cn">
          |  <head>
          |    <meta charset="UTF-8" />
          |    <title>The HTML5 Herald</title>
          |  </head>
          |  <body>
          |      password: <input type="password" name="lname" id="pwd1"/>
          |      ensure password: <input type="password" name="lname" id="pwd2"/>
          |      <button type="button" id="login-btn">get SHA-256</button>
          |  </body>
          |
          |
          |  <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
          |  <script language="JavaScript">
          |    $(document).ready(function(){
          |        $("button").click(function(){
          |          var pwd1 = $("#pwd1").val();
          |          var pwd2 = $("#pwd2").val();
          |          if(pwd1 != pwd2) {
          |            alert("两次输入的字符串不同");
          |            return;
          |          }
          |          $("#sha-btn").prop('disabled', true);
          |          //send ajax
          |          $.ajax({
          |            type: "POST",
          |            url: "/sha256.json",
          |            data: JSON.stringify({
          |               password: pwd1
          |            }),
          |            dataType: "json",
          |            contentType: "application/json; charset=utf-8",
          |            success: function(data,status){
          |              if (data.status === 200) {
          |                alert(data.sha);
          |                $("#sha-btn").prop('disabled', false);
          |              } else {
          |                 alert("refused :" + data.reason);
          |                 $("#sha-btn").prop('disabled', false);
          |              }
          |            }
          |       });
          |    });
          |
          |    })
          |  </script>
          |</html>
        """.stripMargin)
        .putHeaders(Header("Content-Type", "text/html"))
    case req @ POST -> Root / "sha256.json" =>
      //todo need login: almost service should be login user
      val userData = req.as[String]
      val rst = userData.flatMap { jsonStr =>
        val shaData = parse(jsonStr).extract[SHAReq]
        val shaPwd = Helper.toSHA256(shaData.password)
        Ok(compact(render(("status" -> 200) ~ ("sha" -> shaPwd)))).putHeaders(Header("Content-Type", "application/json"))
      }

      rst
  }
}
