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
  .settings(name := "web",
            settings,
            libraryDependencies ++= Seq(
              //deps
            ))
  .dependsOn(
    core
  )

lazy val core = project
  .settings(
    name := "core",
    settings,
    libraryDependencies ++= coreDependencies
  )
  .dependsOn(
    utils,
    persistence
  )

lazy val persistence = project
  .settings(
    name := "persistence",
    settings,
    libraryDependencies ++= persistenceDependencies
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
    )
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    val slf4jV    = "1.7.25"
    val flywayV   = "5.0.7"
    val postgresV = "42.2.2"

    val slf4j = "org.slf4j" % "jcl-over-slf4j" % slf4jV
    val flyway   = "org.flywaydb"   % "flyway-core"    % flywayV

    val postgres = "org.postgresql" % "postgresql" % postgresV
  }

lazy val coreDependencies = Seq(
  dependencies.slf4j
)

lazy val persistenceDependencies = Seq(
  dependencies.postgres,
  dependencies.flyway
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
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)