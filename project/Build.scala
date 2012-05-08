import sbt._

import Keys._
import Project.Initialize

object StartScriptBuild extends Build {
    lazy val root =
        Project("root", file("."), settings = rootSettings)

    lazy val rootSettings = Defaults.defaultSettings ++
        Seq(sbtPlugin := true,
            organization := "com.typesafe.startscript",
            name := "xsbt-start-script-plugin",
            scalacOptions := Seq("-unchecked", "-deprecation"),

            // to release, bump major/minor/micro as appropriate,
            // drop SNAPSHOT, tag and publish.
            // add snapshot back so git master is a SNAPSHOT.
            // when releasing a SNAPSHOT to the repo, bump the micro
            // version at least.
            // Also, change the version number in the README.md
            // Versions and git tags should follow: http://semver.org/
            // except using -SNAPSHOT instead of without hyphen.

            version := "0.5.2-gs",
            libraryDependencies <++= sbtVersion {
		(version) =>
		    Seq("org.scala-sbt" %% "io" % version % "provided",
			"org.scala-sbt" %% "logging" % version % "provided",
			"org.scala-sbt" %% "process" % version % "provided")
            },

            // publish stuff
    publishTo   <<= (version) { version: String =>
      val nexus = "http://nexus.generalsensing.com/content/repositories/"
      if (version.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "snapshots/")
      else                                   Some("releases"  at nexus + "releases/")
    },
            // publishTo <<= (version) { v =>
            //     import Classpaths._
            //     Option(if (v endsWith "SNAPSHOT") typesafeSnapshots else typesafeResolver)
            // },
            publishMavenStyle := true,
            credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"))
}
