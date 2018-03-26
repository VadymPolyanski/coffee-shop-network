name := "coffee-shop-network"

version := "0.1"

organization in ThisBuild := "com.polianskyi.csn"
scalaVersion in ThisBuild := "2.12.3"

// PROJECTS

lazy val csn = project
  .in(file("."))
  .settings(settings)
  .aggregate(
    web,
    core,
    persistence,
    utils
  )

lazy val web = project
  .settings(
    name := "web",
    settings,
    libraryDependencies ++= webDependencies ++ logDependencies
  ).dependsOn(
    core
  )

lazy val core = project
  .settings(
    name := "core",
    settings,
    libraryDependencies ++= webDependencies ++ coreDependencies ++ logDependencies
  )
  .dependsOn(
    utils,
    persistence
  )

lazy val persistence = project
  .settings(
    name := "persistence",
    settings,
    libraryDependencies ++= persistenceDependencies ++ logDependencies
  )
  .dependsOn(
    utils
  )

lazy val utils = project
  .settings(
    name := "utils",
    settings,
    libraryDependencies ++= Seq(
      //deps
    ) ++ logDependencies
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    val slf4jV      = "1.7.5"
    val logbackV    = "1.2.3"
    val flywayV     = "5.0.7"
    val postgresV   = "42.2.2"
    val akkaV       = "10.1.0"
    val akkaStreamV = "2.5.11"
    val scalaTestV  = "2.2.6"

    val logback           = "ch.qos.logback"    % "logback-classic"                     % logbackV
    val slf4j             = "org.slf4j"         % "slf4j-api"                           % slf4jV
    val flyway            = "org.flywaydb"      % "flyway-core"                         % flywayV
    val postgres          = "org.postgresql"    % "postgresql"                          % postgresV
    val akkaStream        = "com.typesafe.akka" %% "akka-stream"                        % akkaStreamV
    val akkaHttp          = "com.typesafe.akka" %% "akka-http"                          % akkaV
    val akkaJson          = "com.typesafe.akka" %% "akka-http-spray-json"               % akkaV
    val scalaTest         = "org.scalatest"     %% "scalatest"                          % scalaTestV % "test"
    val scalaTestSupport  = "org.scalamock"     %% "scalamock-scalatest-support"        % "3.4.2"

  }

lazy val logDependencies = Seq(
  dependencies.slf4j,
  dependencies.logback
)

lazy val coreDependencies = Seq(

)

lazy val webDependencies = Seq(
  dependencies.akkaStream,
  dependencies.akkaHttp,
  dependencies.akkaJson

)

lazy val persistenceDependencies = Seq(
  dependencies.postgres,
  dependencies.flyway,
    dependencies.slf4j

)

// SETTINGS

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val settings = Seq(
  scalacOptions := compilerOptions,
  resolvers := Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)