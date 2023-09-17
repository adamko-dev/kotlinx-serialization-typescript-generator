package buildsrc.convention

import buildsrc.config.KxsTsGenBuildSettings
import java.time.Duration
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  base
}

if (project != rootProject) {
  project.group = rootProject.group
  project.version = rootProject.version
}

extensions.create<KxsTsGenBuildSettings>(KxsTsGenBuildSettings.NAME)

tasks.withType<AbstractArchiveTask>().configureEach {
  // https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
  isPreserveFileTimestamps = false
  isReproducibleFileOrder = true
}

tasks.withType<Test>().configureEach {
  timeout.set(Duration.ofMinutes(10))

  testLogging {
    // don't log console output - it's too noisy
    showCauses = false
    showExceptions = false
    showStackTraces = false
    showStandardStreams = false
    events(
      // only log test outcomes
      TestLogEvent.PASSED,
      TestLogEvent.FAILED,
      TestLogEvent.SKIPPED,
      // TestLogEvent.STARTED,
      // TestLogEvent.STANDARD_ERROR,
      // TestLogEvent.STANDARD_OUT,
    )
  }
}
