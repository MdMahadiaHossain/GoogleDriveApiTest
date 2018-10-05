name := """GoogleDriveApiTest"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.6")

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
// rest api
libraryDependencies += ws
libraryDependencies += ehcache
// google Api
libraryDependencies += "com.google.apis" % "google-api-services-drive" % "v2-rev326-1.25.0"
libraryDependencies += "com.google.api-client" % "google-api-client" % "1.23.0"
libraryDependencies += "com.google.oauth-client" % "google-oauth-client-jetty" % "1.23.0"



