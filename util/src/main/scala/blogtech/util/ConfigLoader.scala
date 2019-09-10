package blogtech.util

import java.io.File

import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}
import org.slf4j.LoggerFactory

/**
  * load config to global
  */
object ConfigLoader {
  private val logger = LoggerFactory.getLogger(getClass)

  private val fileConf = ConfigFactory.parseFile(new File("./application.conf"))
  private val online = ConfigFactory.parseResourcesAnySyntax("online")
  private val local = ConfigFactory.parseResourcesAnySyntax("local")
  private val develop = ConfigFactory.parseResourcesAnySyntax("application") //application is also develop environment in this project
  private val default = ConfigFactory.load() //default environment

  private val myConfig = fileConf.withFallback(local).withFallback(online).withFallback(develop).resolve()
  val combinedConfig: Config = myConfig.withFallback(default)

  logger.info("===========BlogConfigBegin=============")
  printConf(myConfig)
  logger.info("===========BlogConfigEnd===============")

  def prefixConfig(prefix: String, srcConfig: Config) = ConfigFactory.parseString(prefix).withFallback(srcConfig)
  def printConf(config: Config): Unit = logger.info(config.root().render(ConfigRenderOptions.concise().setFormatted(true).setJson(true)))
}
