ThisBuild / organization := "com.yalingunayer.hexagonal"
ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "3.1.0"

lazy val hexagonal = project
  .in(file("."))
  .aggregate(coreapi)

lazy val coreapi = project in file("./core-api")
