import java.io.File

import com.typesafe.config.ConfigFactory
import lorance.scall.{Auth, Config, Password, SessionTerminal}
import sbt._

/**
  *
  */
object DevOpsCommon {
  lazy val deploy2server = taskKey[Unit]("将编译后的文件上传发布到远端服务器并运行")
  lazy val mainClassPath = settingKey[String]("主函数的ClassPath")
  lazy val test2 = taskKey[Unit]("hahah test")

  private val buildConfig = ConfigFactory.parseFile(new File("./build.conf")).resolve()
  val deployHost = buildConfig.getString("deploy.host")
  val deployUser = buildConfig.getString("deploy.user")
  val deployKey = buildConfig.getString("deploy.key")

  val localDeployHost = buildConfig.getString("deploy.localSSH.host")
  val localDeployUser = buildConfig.getString("deploy.localSSH.user")
  val localDeployPassword = buildConfig.getString("deploy.localSSH.password")

}
