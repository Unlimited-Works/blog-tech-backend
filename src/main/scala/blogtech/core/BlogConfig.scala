package blogtech.core

import java.io.File

import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}

/**
  *
  */
object BlogConfig {
  private val fileConf = ConfigFactory.parseFile(new File("./application.conf"))
  private val online = ConfigFactory.parseResourcesAnySyntax("online")
  private val local = ConfigFactory.parseResourcesAnySyntax("local")
  private val develop = ConfigFactory.parseResourcesAnySyntax("application") //application is also develop environment in this project
  private val default = ConfigFactory.load() //default environment

  private val myConfig = fileConf.withFallback(online).withFallback(local).withFallback(develop).resolve()
  private val combinedConfig = myConfig.withFallback(default)

  val gitConfig: Config = combinedConfig.getConfig("blogtech.git")
  val gitEnv = GitEnv(gitConfig.getString("path"))

  def prefixConfig(prefix: String, srcConfig: Config) = ConfigFactory.parseString(prefix).withFallback(srcConfig)
  def printConf(config: Config): Unit = println(config.root().render(ConfigRenderOptions.concise().setFormatted(true).setJson(true)))

  println("===========BlogConfigBegin=============")
  printConf(myConfig)
  println("===========BlogConfigEnd===============")
}

case class GitEnv(path: String)