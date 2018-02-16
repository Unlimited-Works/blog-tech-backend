package blogtech.core

import lorance.scall.{Auth, Config, Password, SessionPool}
import org.junit.Test

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  *
  */
class GitOpsTest {
  val sysSession = new SessionPool(Auth(BlogConfig.sshEnv.host,
    Some(BlogConfig.sshEnv.user),
    22,
    Password(BlogConfig.sshEnv.password)),
    Config(10, 5, 2)
  )

  val gitOps = new GitOps(sysSession)

  @Test
  def getFile(): Unit = {
    println(gitOps.getFile("blog-tech", "build.sbt"))
  }

  @Test
  def getHanziFile(): Unit = {
    println(Await.result(gitOps.getFile("lorance", "hello.md"), 15 seconds))
  }
}
