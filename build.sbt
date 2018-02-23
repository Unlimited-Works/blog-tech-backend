import DevOps._
import DevOpsCommon._

Global / scalacOptions ++= Seq("-Ypartial-unification")

val scalacVersion = "2.12.4"
val Http4sVersion = "0.18.0"
val Specs2Version = "4.0.0"
val LogbackVersion = "1.2.3"
val doobieVersion = "0.5.0"

lazy val httpLibs = Seq(
  //http4s
  "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
  //      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
  "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
  "org.specs2"     %% "specs2-core"          % Specs2Version % "test",

)
lazy val doobieLibs = Seq(
  //doobie
  "org.tpolecat" %% "doobie-core"     % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.tpolecat" %% "doobie-specs2"   % doobieVersion,
)
lazy val testLib = "junit" % "junit" % "4.12" % Test

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    name := "blog-tech",
    version := "0.1",
    scalaVersion := scalacVersion
  )
  .settings(
    libraryDependencies ++=
      httpLibs
      ++: Seq(
        "com.scalachan" %% "scall" % "0.7.1",
        "org.json4s" %% "json4s-native" % "3.6.0-M2",
        "io.monix" %% "monix" % "3.0.0-M3",
        "com.auth0" % "java-jwt" % "3.3.0",
        "org.planet42" %% "laika-core" % "0.7.5",
        "com.atlassian.commonmark" % "commonmark-ext-gfm-tables" % "0.11.0",
      )
      :+ testLib

  )
  .settings(
    mainClassPath := "blogtech.Main",
    deployToServer,
  )
  .dependsOn(util)
  .aggregate(util)

lazy val `git-server` = (project in file("git-server"))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    name := "git-server",
    version := "0.1",
    scalaVersion := scalacVersion
  )
  .settings(
    libraryDependencies ++=
      httpLibs
      :+ testLib

  )
  .settings(
    mainClassPath := "blogtech.gitproxy.ProxyMain",
    deployToServer,
  )
  .dependsOn(util)
  .aggregate(util)

lazy val util = (project in file("util"))
  .settings(
    name := "util",
    version := "0.1",
    scalaVersion := scalacVersion
  )
  .settings(
    libraryDependencies ++=
      doobieLibs
      ++: Seq(
        //others
        "ch.qos.logback"  %  "logback-classic"      % LogbackVersion,
        "com.typesafe"    % "config"                % "1.3.1",

        "org.http4s"      %% "http4s-blaze-client"  % Http4sVersion,
      )
      :+ testLib
  )

cancelable in Scope.Global := true
parallelExecution := false
fork := true
