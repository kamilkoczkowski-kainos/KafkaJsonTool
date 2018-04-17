name := "kafka_json_tool"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.42"

libraryDependencies += "net.liftweb" %% "lift-json" % "3.2.0"

resolvers += "Apache Staging" at "https://repository.apache.org/content/groups/staging/"
resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

//libraryDependencies += "org.apache.kafka" % "kafka_2.11.1" % "1.0.0"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.11"

libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11"

libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "0.10.2.2"

val elastic4sVersion = "5.4.0"

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-tcp" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-streams" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-circe" % elastic4sVersion,
  //  "org.apache.logging.log4j" % "log4j-api" % "2.9.1",
  //  "org.apache.logging.log4j" % "log4j-core" % "2.9.1",
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test"
)

libraryDependencies += "org.scalaj" % "scalaj-http_2.11" % "2.3.0"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api"       % "1.7.7",
  "org.slf4j" % "jcl-over-slf4j"  % "1.7.7"
).map(_.force())

libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-jdk14")) }
