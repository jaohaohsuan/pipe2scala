
lazy val cpJarsForDocker = taskKey[Unit]("prepare for building Docker image")

val akkaV = "2.5.1"

lazy val root = (project in file(".")).settings(
  name := "pipe2scala",
  version := "1.0",
  scalaVersion := "2.11.8",
  exportJars := true,
  libraryDependencies  ++= Seq(
      "com.typesafe.akka" %% "akka-cluster" % akkaV,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaV,
      "com.typesafe.akka" %% "akka-actor" % akkaV,
      "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-cluster-metrics" % akkaV,
      "ch.qos.logback" % "logback-classic" % "1.1.3"
  ),
  cpJarsForDocker := {

    val dockerDir = (target in Compile).value / "docker"

    val jar = (packageBin in Compile).value
    IO.copyFile(jar, dockerDir / "app" / jar.name)

    (dependencyClasspath in Compile).value.files.foreach { f => IO.copyFile(f, dockerDir / "libs" / f.name )}

    (mainClass in Compile).value.foreach { content => IO.write( dockerDir / "mainClass", content ) }

    IO.copyFile(baseDirectory.value / "Dockerfile", dockerDir / "Dockerfile")
  }
)

fork in run in Global := true
        