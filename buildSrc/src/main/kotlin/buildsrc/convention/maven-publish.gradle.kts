package buildsrc.convention

plugins {
  `maven-publish`
}

plugins.withType(JavaPlugin::class.java) {
  publishing {
    publications {
      create<MavenPublication>("mavenJava") {
        from(components["java"])
      }
    }
  }
}

tasks
  .matching { it.name in listOf("publish", "publishToMavenLocal") }
  .configureEach {
    doLast {
      logger.lifecycle("[${this.name}] ${project.group}:${project.name}:${project.version}")
    }
  }
