scalaVersion := "2.11.8"

name := "karaOK"
organization := "com.github.walfie"

androidBuild
platformTarget in Android := "android-23"

libraryDependencies ++= Seq(
  aar("org.macroid" %% "macroid" % "2.0.0-M4")
)

proguardScala in Android := true
proguardOptions in Android ++= Seq(
  "-keep class scala.Dynamic"
)

scalacOptions ++=
  Seq(
    "-deprecation", "-feature", "-unchecked", "-Xlint",
    "-P:wartremover:traverser:macroid.warts.CheckUi"
  ) ++ dependencyClasspath.in(Compile).value.files.map("-P:wartremover:cp:" + _.toURI.toURL)

