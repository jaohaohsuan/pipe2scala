
lazy val cpJarsForDocker = taskKey[Unit]("prepare for building Docker image")

val akkaV = "2.4.17"

lazy val root = (project in file(".")).settings(
  name := "pipe2scala",
  version := "1.0",
  scalaVersion := "2.11.8",
  exportJars := true,
  libraryDependencies  ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaV
  ),
  cpJarsForDocker := {

    clean.value

    val dockerDir = (target in Compile).value / "docker"

    val jar = (packageBin in Compile).value
    IO.copyFile(jar, dockerDir / "app" / jar.name)

    (dependencyClasspath in Compile).value.files.foreach { f => IO.copyFile(f, dockerDir / "libs" / f.name )}

    (mainClass in Compile).value.foreach { content => IO.write( dockerDir / "main", content ) }

    IO.copyFile(baseDirectory.value / "Dockerfile", dockerDir / "Dockerfile")
  }
)
        