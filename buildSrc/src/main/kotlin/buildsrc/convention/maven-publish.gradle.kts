package buildsrc.convention

plugins {
  `maven-publish`
}

//plugins.withType(JavaPlugin::class.java) {
//  publishing {
//    publications {
//      create<MavenPublication>("mavenJava") {
//        from(components["java"])
//      }
//    }
//  }
//}

tasks
  .matching {
    it.name.startsWith(PublishingPlugin.PUBLISH_LIFECYCLE_TASK_NAME)
      && it.group == PublishingPlugin.PUBLISH_TASK_GROUP
  }
  .configureEach {
    doLast {
      logger.lifecycle("[${this.name}] ${project.group}:${project.name}:${project.version}")
    }
  }
