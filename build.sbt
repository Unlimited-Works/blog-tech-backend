
scalacOptions ++= Seq("-Ypartial-unification")

val dottyVersion = "0.5.0-RC1"
val scalacVersion = "2.12.4"
val Http4sVersion = "0.18.0-M8"
val Specs2Version = "4.0.0"
val LogbackVersion = "1.2.3"
val doobieVersion = "0.5.0-M13"

lazy val root = (project in file("."))
  .settings(
    name := "blog-tech",
    version := "0.1",
    scalaVersion := scalacVersion
  )
  .settings(
    libraryDependencies ++= Seq(
      //http4s
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.specs2"     %% "specs2-core"          % Specs2Version % "test",

      //doobie
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2"   % doobieVersion,

      //test
      "junit" % "junit" % "4.12" % Test,

      //others
      "ch.qos.logback"  %  "logback-classic" % LogbackVersion,
      "com.scalachan" %% "scall" % "0.7.0",
      "com.typesafe" % "config" % "1.3.1",


    )
  )

//cancelable in Scope.Global := true

//fork := true
