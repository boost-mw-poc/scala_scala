package scala.build

import sbt._
import sbt.Keys.{ artifact, dependencyClasspath, moduleID, resourceManaged }

object ScaladocSettings {

  // when this changes, the integrity check in HtmlFactory.scala also needs updating
  val webjarResources = Seq(
    "org.webjars" % "jquery" % "4.0.0",
    "org.webjars.npm" % "panzoom__panzoom" % "4.5.1"
  )

  def extractResourcesFromWebjar = Def.task {
    def isWebjar(s: Attributed[File]): Boolean =
      s.get(artifact.key).isDefined && s.get(moduleID.key).exists(mid => mid.organization == "org.webjars" || mid.organization == "org.webjars.npm")
    val dest = (resourceManaged.value / "webjars").getAbsoluteFile
    IO.createDirectory(dest)
    val classpaths = (Compile / dependencyClasspath).value
    val files: Seq[File] = classpaths.filter(isWebjar).flatMap { classpathEntry =>
      val jarFile = classpathEntry.data
      IO.unzip(jarFile, dest)
    }
    (files ** "*.min.js").get
  }

}
