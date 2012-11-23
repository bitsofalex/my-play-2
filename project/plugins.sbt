// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// The sbt-cucumber-plugin repository
resolvers += "Templemore Repository" at "http://templemore.co.uk/repo"

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.1-RC1")

// Use xsbt-cucumber-plugin
addSbtPlugin("templemore" %% "sbt-cucumber-plugin" % "0.7.0")