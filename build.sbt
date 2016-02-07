name := "mcl"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

// add cache so we only load the XML once (test this by removing file)
libraryDependencies ++= Seq(
	cache
)

// add xml library
// libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.3"
