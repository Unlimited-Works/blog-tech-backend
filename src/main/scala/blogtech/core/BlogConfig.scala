package blogtech.core


import com.typesafe.config.Config

/**
  *
  */
object BlogConfig {
  import blogtech.util.ConfigLoader._

  val gitConfig: Config = combinedConfig.getConfig("blogtech.git")
  val gitEnv = GitEnv(gitConfig.getString("path"))

  val dbConfig: Config = combinedConfig.getConfig("blogtech.db")
  val dbEnv = DbEnv(
    dbConfig.getString("user"),
    dbConfig.getString("password")
  )

  val sshConfig: Config = combinedConfig.getConfig("blogtech.ssh")
  val sshEnv = SSHEnv(
    sshConfig.getString("host"),
    sshConfig.getString("user"),
    sshConfig.getString("password")
  )

}

case class GitEnv(path: String)
case class SSHEnv(host: String, user: String, password: String)
case class DbEnv(user: String, password: String)
