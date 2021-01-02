import sbt._
import Keys._
import sbtrelease.ReleasePlugin._

lazy val jacksonModuleEnumeratum = (project in file("."))
  .settings(
    name := "jackson-module-enumeratum",
    organization := "com.github.pjfanning",
    crossScalaVersions := Seq("2.12.12", "2.13.4"),
    scalaVersion := "2.13.4",

    sbtPlugin := false,

    publishArtifact in (Compile, packageDoc) := false,
    scalacOptions ++= Seq("-deprecation", "-Xcheckinit", "-encoding", "utf8", "-g:vars", "-unchecked", "-optimize"),
    parallelExecution := true,
    parallelExecution in Test := true,
    homepage := Some(new java.net.URL("https://github.com/pjfanning/jackson-module-enumeratum/")),
    description := "A library for serializing/deserializing enumeratum enums using Jackson.",

    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    ),

    licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),

    scmInfo := Some(
      ScmInfo(
        url("https://github.com/pjfanning/jackson-module-enumeratum"),
        "scm:git@github.com:pjfanning/jackson-module-enumeratum.git"
      )
    ),

    developers := List(
      Developer(id="pjfanning", name="PJ Fanning", email="", url=url("https://github.com/pjfanning"))
    ),

    libraryDependencies ++= Seq(
      "com.beachape" %% "enumeratum" % "1.6.1",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.0",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.3" % Test
    ),

    // enable publishing the main API jar
    publishArtifact in (Compile, packageDoc) := true,

    // build.properties
    resourceGenerators in Compile += Def.task {
      val file = (resourceManaged in Compile).value / "com" / "github" / "pjfanning" / "enumeratum" / "build.properties"
      val contents = "version=%s\ngroupId=%s\nartifactId=%s\n".format(version.value, organization.value, name.value)
      IO.write(file, contents)
      Seq(file)
    }.taskValue
  )

