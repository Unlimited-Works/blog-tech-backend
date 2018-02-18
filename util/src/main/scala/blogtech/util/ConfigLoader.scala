package blogtech.util

import java.io.File

import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}

/**
  * load config to global
  */
object ConfigLoader {
  private val fileConf = ConfigFactory.parseFile(new File("./application.conf"))
  private val online = ConfigFactory.parseResourcesAnySyntax("online")
  private val local = ConfigFactory.parseResourcesAnySyntax("local")
  private val develop = ConfigFactory.parseResourcesAnySyntax("application") //application is also develop environment in this project
  private val default = ConfigFactory.load() //default environment

  private val myConfig = fileConf.withFallback(local).withFallback(online).withFallback(develop).resolve()
  val combinedConfig: Config = myConfig.withFallback(default)

  println("===========BlogConfigBegin=============")
  printConf(myConfig)
  println("===========BlogConfigEnd===============")

  def prefixConfig(prefix: String, srcConfig: Config) = ConfigFactory.parseString(prefix).withFallback(srcConfig)
  def printConf(config: Config): Unit = println(config.root().render(ConfigRenderOptions.concise().setFormatted(true).setJson(true)))


}
