val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name                                   := "pbay-scala",
    version                                := "0.1.0-SNAPSHOT",
    scalaVersion                           := scala3Version,
    exportJars                             := true,
    assembly / assemblyJarName             := "pbay-scala.jar",
    libraryDependencies += "org.scalameta" %% "munit"    % "0.7.29" % Test,
    libraryDependencies += "com.lihaoyi"   %% "requests" % "0.7.1",
    libraryDependencies += "com.lihaoyi"   %% "upickle"  % "2.0.0",
    libraryDependencies += "com.lihaoyi"   %% "ujson"    % "1.3.15"
  )
