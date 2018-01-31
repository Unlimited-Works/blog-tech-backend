package blogtech.core

import lorance.scall.{Auth, Config, Password, SessionPool}
import org.junit.Test

/**
  *
  */
class GitOpsTest {
  val sysSession = new SessionPool(Auth("localhost", None, 22, Password("xxx")), Config(10, 5, 2))
  val gitOps = new GitOps(sysSession)

  @Test
  def getFile(): Unit = {
    println(gitOps.getFile("blog-tech", "build.sbt"))
  }
}
