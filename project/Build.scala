import sbt._
import xerial.sbt.Pack._
import xerial.sbt.Pack.packSettings

object Build extends sbt.Build {

  lazy val root = Project(
    id = "mongo-monitor",
    base = file("."),
    settings = packSettings ++
      Seq(
        packMain := Map("mongo-monitor" -> "com.ggfos.mongo.monitor.Monitor"),
        packGenerateWindowsBatFile := false,
        packExpandedClasspath := false
      )
  )
}