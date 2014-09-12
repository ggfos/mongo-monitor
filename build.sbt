name := "mongo-monitor"

version := "1.0"

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "2.10.1"

libraryDependencies += "com.sun.mail" % "javax.mail" % "1.4.5"

libraryDependencies += "com.typesafe" % "config" % "1.2.1"

libraryDependencies += "commons-httpclient" % "commons-httpclient" % "3.1"

unmanagedBase := baseDirectory.value / "unmanaged_lib"