import sbt._
import sbt.Keys._

// import org.ensime.sbt.Plugin.Settings.ensimeConfig
// import org.ensime.sbt.util.SExp._
import java.io.{PrintWriter}

object LaboProjectBuild extends Build {

  // lazy val continue = project("Continue", "Continue", new DefaultSpdeProject(_) with AutoCompilerPlugins {
  //   val continuations = compilerPlugin("org.scala-lang.plugins" % "continuations" % "2.9.1")
  //   override def compileOptions = CompileOption("-P:continuations:enable") :: super.compileOptions.toList
  // })

  lazy val laboProject = Project(
    id = "scala-labo",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Labo Project",
      organization := "labo",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.11.8",
      // scalaVersion := "2.11.1",
	  autoCompilerPlugins := true,
	  // addCompilerPlugin("org.scala-lang.plugins" %% "scala-continuations-plugin" % "1.0.2"),
      // add other settings here
      /** scalacOptions
       *  -unchecked
       *  -deprecation
       *  -Xprint:typer 暗黙変換の情報を表示する
       *  -Xcheckinit   継承関係における予測しがたいスーパークラスの初期化をチェックする(c.f. Scala for the Impatient,p.93)
       */
      //scalacOptions ++= Seq("-unchecked", "-deprecation","-Xcheckinit", "-P:continuations:enable"),
      scalacOptions ++= Seq("-unchecked", "-deprecation","-Xcheckinit"),
      resolvers ++= Seq("Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
                        "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                        "releases"  at "http://oss.sonatype.org/content/repositories/releases",
                        "Maven Repository" at "http://repo1.maven.org/maven2/",
                        "repo.novus rels" at "http://repo.novus.com/releases/",
                        "repo.novus snaps" at "http://repo.novus.com/snapshots/"),
      // libraryDependencies += "org.scala-lang.plugins" % "continuations" % "2.9.1",
	  // lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.2"
	  
      libraryDependencies ++= Seq (
		"com.typesafe.akka" %% "akka-actor" % "2.4.0",
		"com.typesafe.akka" %% "akka-testkit" % "2.4.0",
		"org.specs2" %% "specs2-core" % "3.6.5" % "test",
		"org.scalatest" %% "scalatest" % "2.2.4" % "test",
		"org.scalacheck" %% "scalacheck" % "1.12.5" % "test",
		"joda-time" % "joda-time" % "2.4",
		"org.joda" % "joda-convert" % "1.8.1", //*2
		"junit" % "junit" % "4.12" % "test",
		"com.novocode" % "junit-interface" % "0.11" % "test",
		"org.scalaz" %% "scalaz-core" % "7.1.5",
		"org.scalaz.stream" %% "scalaz-stream" % "0.8",
		"org.mongodb" %% "casbah" % "2.7.1",
		"com.gilt" %% "jerkson" % "0.6.8",
		"com.googlecode.kiama" %% "kiama" % "1.8.0",
		"com.github.nscala-time" %% "nscala-time" % "1.4.0",
        "org.typelevel" %% "cats" % "0.4.0",
		"org.scalaj" %% "scalaj-time" % "0.8",
		"com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3",
        "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
		"org.mongodb" % "mongo-java-driver" % "3.1.0",
		"com.chuusai" %% "shapeless" % "2.2.5",
		"org.json4s" %% "json4s-native" % "3.3.0",
		"net.liftweb" %% "lift-json" % "2.6.2",
		// compilerPlugin("org.scala-lang.plugins" % "continuations" % scalaVersion.value),
		"org.scala-lang.plugins" %% "scala-continuations-library" % "1.0.2"
      ),
      shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " },
	  /* 以下は sbt console の起動時に実行される */
      initialCommands in console := {"""
        scala.tools.nsc.interpreter.replProps.power.enable
        import System.{currentTimeMillis => now}
        def time[T](f: => T): T = {
          val start = now
          try { f } finally { println("Elapsed: " + (now - start)/1000.0 + " s") }
        }
        import scala.sys.process._
        def find(dir:String = ".", file:String ="*.scala") =
          Seq("find", dir, "-type","f", "-name",file) 
        def grep(pattern:String) =
          Process("xargs grep -ni %s".format(pattern))
        println("Usage: find() #| grep(\"pattern\") ")
        """}
      /* ENSIMEの設定 */
      // ensimeConfig := sexp(
      //   key(":only-include-in-index"), sexp(
      //     "src\\..*"
      //   ),
      //   key(":formatting-prefs"), sexp(
      //     key(":alignParameters"),true,
      //     key(":indentSpaces"),3
      //   )
      // )
    )
  )
}
