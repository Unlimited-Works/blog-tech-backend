package blogtech.util

import com.typesafe.config.Config

/**
  *
  */
object UtilConfig {
  import blogtech.util.ConfigLoader._

  val dbConfig: Config = combinedConfig.getConfig("blogtech.util.db")
  val dbEnv = DbEnv(
    dbConfig.getString("host"),
    dbConfig.getInt("port"),
    dbConfig.getString("database"),
    dbConfig.getString("user"),
    dbConfig.getString("password")
  )
}

case class DbEnv(host: String, port: Int, database: String, user: String, password: String)
