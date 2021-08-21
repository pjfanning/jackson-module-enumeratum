import sbt._
import Keys._
import sbtrelease.ReleasePlugin._

lazy val jacksonModuleEnumeratum = (project in file("."))
  .settings(
    name := "jackson-module-enumeratum",
    organization := "com.github.pjfanning",
    ThisBuild / scalaVersion := "2.13.6",
    ThisBuild / crossScalaVersions := Seq("2.12.14", "2.13.6"),

    sbtPlugin := false,

    scalacOptions ++= Seq("-deprecation", "-Xcheckinit", "-encoding", "utf8", "-g:vars", "-unchecked", "-optimize"),
    Test / parallelExecution := true,
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
      "com.beachape" %% "enumeratum" % "1.7.0",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.13.0-rc1",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.0-rc1" % Test,
      "org.scalatest" %% "scalatest" % "3.2.9" % Test
    ),

    // enable publishing the main API jar
    publishArtifact := true,

    // build.properties
    Compile / resourceGenerators += Def.task {
      val file = (Compile / resourceManaged).value / "com" / "github" / "pjfanning" / "enumeratum" / "build.properties"
      val contents = "version=%s\ngroupId=%s\nartifactId=%s\n".format(version.value, organization.value, name.value)
      IO.write(file, contents)
      Seq(file)
    }.taskValue,

    ThisBuild / githubWorkflowTargetTags ++= Seq("v*"),
    ThisBuild / githubWorkflowPublishTargetBranches := Seq(
      RefPredicate.Equals(Ref.Branch("main")),
      RefPredicate.Equals(Ref.Branch("swagger-1.5")),
      RefPredicate.StartsWith(Ref.Tag("v"))
    ),

    ThisBuild / githubWorkflowPublish := Seq(
      WorkflowStep.Sbt(
        List("ci-release"),
        env = Map(
          "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
          "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
          "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
          "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
        )
      )
    )
  )

