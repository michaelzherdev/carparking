name := "carparking"

version := "1.0"

lazy val `carparking` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

libraryDependencies ++= Seq( jdbc, "com.typesafe.play" %% "anorm" % "2.5.1" )

libraryDependencies += guice

libraryDependencies += "com.h2database" % "h2" % "1.4.196"

libraryDependencies += evolutions

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"