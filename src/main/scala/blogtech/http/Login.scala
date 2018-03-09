package blogtech.http

import blogtech.core.JWTHelper
import org.http4s.{Header, HttpDate, HttpService, Response}
import cats.effect.IO
import cats.implicits._
import monix.eval.Task
import org.http4s.dsl.Http4sDsl
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.slf4j.LoggerFactory
import monix.execution.Scheduler.Implicits.global

/**
  * login and register API
  */
object Login extends Http4sDsl[IO] with Service {
  private val logger = LoggerFactory.getLogger(getClass)

  private implicit val format: DefaultFormats = org.json4s.DefaultFormats

  case class LoginReq(account: String, password: String)
  case class RegisterReq(email: String, userName: String, password: String)

  override val service: HttpService[IO] = HttpService[IO] {
    case GET -> Root  =>
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
          |      user name: <input type="text" name="fname" />
          |      password: <input type="password" name="lname" />
          |      <button type="button" id="login-btn">Login</button>
          |  </body>
          |
          |
          |  <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
          |  <script language="JavaScript">
          |    $(document).ready(function(){
          |        $("button").click(function(){
          |             var name = $(":text").val();
          |             var pwd =  $(":password").val();
          |             $("#login-btn").prop('disabled', true);
          |        //send ajax
          |        $.ajax({
          |            type: "POST",
          |            url: "/login.json",
          |            data: JSON.stringify({
          |               account: name,
          |               password: pwd
          |            }),
          |            dataType: "json",
          |            contentType: "application/json; charset=utf-8",
          |            success: function(data,status){
          |              if (data.status === 200) {
          |                window.location.replace("/" + name);
          |              } else {
          |                 alert("login fail: " + json)
          |              }
          |            }
          |       });
          |    });
          |
          |    })
          |    /**
          |    	//functions
          |	var verifyFunc = function(){
          |			var accountValue = $('#inputEmail').val();
          |			var passwordValue = $('#inputPassword').val();
          |
          |			$(".ajax-btn-verify").prop('disabled', true);
          |			$(".ajax-btn-verify").text('正在登录...');
          |			$.post({
          |				url: verifyApi,
          |				data: {
          |					account: accountValue,
          |					password: passwordValue
          |				},
          |				success: function(data,status){
          |						var json = jQuery.parseJSON(data);
          |						console.log("Data: " + data + "\nStatus: " + status)
          |						if (json.result === 200) {
          |              rememberMeFunc(accountValue, passwordValue, function(){
          |                window.location.replace("/blog/index");
          |                //alert("window.location.replace('/blog/index');")
          |              });
          |						}
          |						else if (json.result === 400) {
          |							$("#signin-tip").text(json.msg);
          |							$(".ajax-btn-verify").text('登录');
          |							$(".ajax-btn-verify").prop('disabled', false);
          |						}
          |				},
          |				xhrFields: {
          |					 withCredentials: true
          |				},
          |				crossDomain: true
          |			});
          |    };
          |    **/
          |  </script>
          |</html>
        """.stripMargin)
        .putHeaders(Header("Content-Type", "text/html"))
    case req @ POST -> Root / "login.json" =>
      val userData = req.as[String]

      val rst = userData.flatMap{jsonStr =>
        val loginData = parse(jsonStr).extract[LoginReq]

        val verifyLogin = blogtech.core.userOps.verifyUserPassword(loginData.account, loginData.password)
        verifyLogin flatMap {
          case true =>
            Ok(compact(render("status" -> 200)))
              .addCookie(
                HttpConstants.JwtCookie,
                JWTHelper.create(JWTHelper.PayLoad(loginData.account)),
                Some(HttpDate.unsafeFromEpochSecond(System.currentTimeMillis() / 1000L + 60 * 60 * 24 * 365L))
              ).putHeaders(Header("Content-Type", "application/json"))

          case false =>
            Ok(compact(render(
              ("status" -> 400) ~
              ("reason" -> "user_not_exist_or_password_not_match")
            ))).putHeaders(Header("Content-Type", "application/json"))

        }
      }

      rst
    case req @ POST -> Root / "register.json" =>
      val response = for{
        registerStr <- req.as[String]
        registerData = parse(registerStr).extract[RegisterReq]
        //to Executor pool use a custom block pool
        createRepo <- Task.fromFuture(blogtech.core.gitOps.initRepo(registerData.userName)).toIO
      } yield {
        createRepo match {
          case Left(error) =>
            IO(Ok(compact(render(
              ("status" -> 400) ~
                ("reason" -> s"create repo fail: $error")
            ))).putHeaders(Header("Content-Type", "application/json")))
          case Right(_) =>
            for(
              rst <- blogtech.core.userOps
                      .createAccount(registerData.userName, registerData.email, registerData.password)
            ) yield {
              rst match {
                case None =>
                  Ok(compact(render(
                    ("status" -> 400) ~
                      ("reason" -> "user_has_exist")
                  ))).putHeaders(Header("Content-Type", "application/json"))
                case Some(oid) =>
                  Ok(compact(render(
                    ("status" -> 200) ~
                      ("oid" -> oid)
                  ))).putHeaders(Header("Content-Type", "application/json"))
              }
            }
        }
      }

      response.flatten.flatten
  }

}
