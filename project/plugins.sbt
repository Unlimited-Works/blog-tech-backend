//addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.1.7")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.3")

libraryDependencies ++= Seq(
  "com.scalachan" %% "scall" % "0.7.3",
  "com.typesafe"    % "config"                % "1.3.1"

)
