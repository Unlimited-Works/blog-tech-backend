import lorance.scall._
import DevOpsCommon._
import sbt.Keys._

object DevOps {
  test2 := {
    println(deployHost)
    println(deployPassword)
    Unit
  }

  lazy val deployToServer = {
    deploy2server := {
      println("start publish ...")
      //  compile.value
      // variable
      val targetDir = target.value
      val projectName = name.value
      val projectVersion = version.value
      val mainClassPathStr = mainClassPath.value

      case class Context(targetDir: String, projectName: String, projectVersion: String, mainClassPathStr: String)
      println(Context(targetDir.getAbsolutePath, projectName, projectVersion, mainClassPathStr))

      implicit val hostLevel: HostLevel = lorance.scall.HostLevel(0)

      val sshLocal = new Terminal(Auth(
        host = localDeployHost,
        name = Some(localDeployUser),
        port = 22,
        key = Password(localDeployPassword)
      ))

      val sshRemote = new Terminal(Auth(
        host = deployHost,
        name = Some(deployUser),
        port = 22,
        key = Password(deployPassword)
      ))
      val sshRemoteSession = new SessionTerminal(Auth(
        host = deployHost,
        name = Some(deployUser),
        port = 22,
        key = Password(deployPassword)
      ), Config(10, 4, 2))

      //ensure directory exist
      println(s"mkdir ~/$projectName: ")
      println(s"mkdir ~/$projectName: " + sshRemote.exc(Cmd(s"mkdir ~/$projectName")))

      val zipFileName = s"$projectName-$projectVersion.zip"
      val scpRemoteCmd = Cmd(s"sshpass -p '$deployPassword' " +
        s"scp ${targetDir.getPath}/universal/$zipFileName $deployUser@$deployHost:~/$projectName/")
      println("scp executable zip to remote: ")
      println("scp to remote: " + sshLocal.exc(scpRemoteCmd).right.get)
      println(s"cd ~/$projectName: " + sshRemote.exc(Cmd(s"cd ~/$projectName")).right.get)

      //remove old package
      println(s"rm -rf ./$projectName-$projectVersion: " + sshRemote.exc(Cmd(s"rm -rf ./$projectName-$projectVersion")))

      //kill old process
      println(s"kill old process: " + sshRemote.exc(Cmd(s"kill `jcmd |awk '/$mainClassPathStr/{print $$1}'`")))

      Thread.sleep(4 * 1000)

      println(s"unzip ./$zipFileName: " + sshRemote.exc(Cmd(s"unzip ./$zipFileName")).right.get)
      println(s"nohup ./$projectName-$projectVersion/bin/$projectName &")

      println(sshRemoteSession.exc(Cmd(s"cd ~/$projectName; nohup ./$projectName-$projectVersion/bin/$projectName > /dev/null 2>&1 &")).right.get)

      sshLocal.disconnect()
      sshRemote.disconnect()
      println("completed ...")

      Unit
    }
  }
}