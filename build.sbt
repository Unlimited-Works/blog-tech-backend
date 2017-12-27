
scalacOptions ++= Seq("-Ypartial-unification")

val dottyVersion = "0.5.0-RC1"
val scalacVersion = "2.12.4"
val Http4sVersion = "0.18.0-M5"
val Specs2Version = "4.0.0"
val LogbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    name := "blog-tech",
    version := "0.1",
    scalaVersion := scalacVersion

//    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.specs2"     %% "specs2-core"          % Specs2Version % "test",
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion
    )
  )

//cancelable in Scope.Global := true

//fork := true
