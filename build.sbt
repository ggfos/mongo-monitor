name := "mongo-monitor"

version := "1.0"

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "2.10.1"

libraryDependencies += "com.sun.mail" % "javax.mail" % "1.4.5"

unmanagedBase := baseDirectory.value / "unmanaged_lib"