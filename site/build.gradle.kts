import com.github.gradle.node.npm.task.NpmTask
import java.net.URI
import buildsrc.config.*

plugins {
  buildsrc.convention.base
  com.github.`node-gradle`.node
  buildsrc.convention.`knit-files`
}

description = "builds the kxs-ts-gen website"

dependencies {
  knitDocs(projects.docs.code)
}

tasks.assemble {
  dependsOn(updateSiteKnitDocs)
  dependsOn(tasks.npmInstall)
}

val docusaurusRun by tasks.registering(NpmTask::class) {
  group = "documentation"
  args("run start")

  dependsOn(tasks.assemble)
}

val docusaurusBuild by tasks.registering(NpmTask::class) {
  group = "documentation"
  args("run build")

  dependsOn(tasks.assemble)
}

val updateSiteKnitDocs by tasks.registering(Sync::class) {
  from(configurations.knitDocs) {
    // fix all the relative links to non-markdown files
    filter { line ->
      line.replace(Regex("""\((?<file>(?:.\/|..\/)\S+[^.md])\)""")) { match ->
        val (file) = match.destructured
        val uri = URI
          .create("https://github.com/adamko-dev/kotlinx-serialization-typescript-generator/blob/main/docs/$file")
          .normalize()
        "($uri)"
      }
    }
  }
  into(layout.projectDirectory.dir("docs/examples"))

}

tasks.clean {
  delete(layout.projectDirectory.dir(".docusaurus"))
  delete(layout.projectDirectory.dir("node_modules"))
}
