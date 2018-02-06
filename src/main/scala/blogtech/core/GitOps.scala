package blogtech.core

import lorance.scall
import lorance.scall.{Cmd, SessionPool}

import scala.concurrent.Future

/**
  *
  */
class GitOps(terminal: SessionPool) {
  def getFile(userName: String, filePathAndName: String, commit: String = "HEAD"): Future[Either[scall.Error, String]] = {
    terminal.execAsync(Cmd(s"cd ${BlogConfig.gitEnv.path}/$userName/blog.git;git --no-pager show HEAD:$filePathAndName"))
  }

  def getPublicFilesPath(userName: String): Future[Either[scall.Error, String]] = {
    terminal.execAsync(Cmd(s"""cd ${BlogConfig.gitEnv.path}/$userName/blog.git; git ls-files --with-tree HEAD | grep -v "private*""""))
  }

  def getAllFilesPath(userName: String): Future[Either[scall.Error, String]] = {
    terminal.execAsync(Cmd(s"""cd ${BlogConfig.gitEnv.path}/$userName/blog.git; git ls-files --with-tree HEAD"""))
  }
}
