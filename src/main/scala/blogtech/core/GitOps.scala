package blogtech.core

import lorance.scall.{Cmd, SessionTerminal}

/**
  *
  */
class GitOps(terminal: SessionTerminal) {
  def getFile(userName: String, filePathAndName: String, commit: String = "HEAD") = {
    terminal.exc(Cmd(s"cd ${BlogConfig.gitEnv.path}/$userName;git --no-pager show HEAD:$filePathAndName"))
  }
}
