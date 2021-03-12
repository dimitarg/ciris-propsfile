import Dependencies._

ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.github.dimitarg"
ThisBuild / organizationName := "example"

ThisBuild / resolvers += Resolver.bintrayRepo("dimitarg", "maven")

ThisBuild / testFrameworks += new TestFramework("weaver.framework.TestFramework")


lazy val root = (project in file("."))
  .settings(
    name := "ciris-propsfile",
    libraryDependencies ++= Seq(
      "is.cir"              %% "ciris"                % "1.2.1",
      "io.github.dimitarg"  %%  "weaver-test-extra"   % "0.4.2"   % "test"
    )
  )



// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
