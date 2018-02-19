package blogtech.core


import com.typesafe.config.Config

/**
  *
  */
object BlogConfig {
  import blogtech.util.ConfigLoader._

  private val gitConfig: Config = combinedConfig.getConfig("blogtech.git")
  private val sshConfig: Config = combinedConfig.getConfig("blogtech.ssh")

  val gitEnv = GitEnv(gitConfig.getString("path"))
  val sshEnv = SSHEnv(
    sshConfig.getString("host"),
    sshConfig.getString("user"),
    sshConfig.getString("password")
  )

  val httpEnv = HttpEnv(combinedConfig.getInt("blogtech.http.port"))

}

case class GitEnv(path: String)
case class SSHEnv(host: String, user: String, password: String)
case class HttpEnv(port: Int)
