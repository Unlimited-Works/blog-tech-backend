package blogtech.core

import lorance.scall
import lorance.scall.{Cmd, SessionPool}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  *
  */
class GitOps(terminal: SessionPool) {
  def getFile(userName: String, filePathAndName: String, commit: String = "HEAD"): Future[Either[scall.Error, String]] = {
    terminal.execAsync(Cmd(s"cd ${BlogConfig.gitEnv.path}/$userName/blog.git;git --no-pager show HEAD:$filePathAndName"))
  }

  def getPublicFilesPath(userName: String): Future[Either[scall.Error, List[String]]] = {
    terminal
      .execAsync(Cmd(s"""cd ${BlogConfig.gitEnv.path}/$userName/blog.git; git ls-files --with-tree HEAD | grep -v "private*""""))
      .map(_.map(x => x.split('\n').toList))
  }

  def getAllFilesPath(userName: String): Future[Either[scall.Error, List[String]]] = {
    terminal
      .execAsync(Cmd(s"""cd ${BlogConfig.gitEnv.path}/$userName/blog.git; git ls-files --with-tree HEAD"""))
      .map(_.map(x => x.split('\n').toList))
  }

  /**
    * create git bare repo
    */
  def initRepo(userName: String): Future[Either[scall.Error, String]] = {
    val cmd = s"cd ${BlogConfig.gitEnv.path}; mkdir ./$userName; git init --bare ./blog.git"
    terminal.execAsync(Cmd(cmd))

  }

}
