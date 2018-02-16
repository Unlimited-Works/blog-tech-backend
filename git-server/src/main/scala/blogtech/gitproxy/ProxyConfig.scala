package blogtech.gitproxy

/**
  *
  */
object ProxyConfig {
  import blogtech.util.ConfigLoader._

  private val proxyDataConf = combinedConfig.getConfig("blogtech.gitProxy.proxyData")

  val proxyData = ProxyData(
    proxyDataConf.getString("host"),
    proxyDataConf.getInt("port")
  )

}

case class ProxyData(host: String, port: Int)