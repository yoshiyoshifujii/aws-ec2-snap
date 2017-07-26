import Dependencies._

lazy val root = (project in file(".")).
  settings(
    name := "aws-snap",
    scalaVersion := "2.12.2",
    libraryDependencies ++= rootDeps,
    assemblyJarName in assembly := s"${name.value}-assembly.jar"
  )
