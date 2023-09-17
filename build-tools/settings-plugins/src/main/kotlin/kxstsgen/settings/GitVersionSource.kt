package kxstsgen.settings

import javax.inject.Inject
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.*
import org.gradle.process.ExecSpec

@Suppress("UnstableApiUsage")
abstract class GitVersionSource : ValueSource<String, GitVersionSource.Parameters> {

  abstract class Parameters @Inject constructor(
    private val providers: ProviderFactory,
  ) : ValueSourceParameters {

    abstract val projectRootDir: DirectoryProperty

    fun exec(configure: ExecSpec.() -> Unit): Provider<String> {
      return projectRootDir.flatMap { projectRootDir ->
        providers.exec {
          workingDir(projectRootDir)
          isIgnoreExitValue = true
          configure()
        }.standardOutput.asText.map { it.trim() }
      }
    }

    abstract val gitDescribe: Property<String>
    abstract val currentBranchName: Property<String>
    abstract val currentCommitHash: Property<String>
  }

  override fun obtain(): String {
    val described = parameters.gitDescribe.get()
    val branch = parameters.currentBranchName.orNull

    val detached = branch.isNullOrBlank()

    return if (!detached) {
      "$branch-SNAPSHOT"
    } else {
      val descriptions = described.split("-")
      val head = descriptions.singleOrNull() ?: ""
      if (head.matches(semverRegex)) {
        head
      } else {
        // fall back to using the git commit hash
        parameters.currentCommitHash.getOrElse("unknown")
      }
    }
  }

  companion object {
    private val semverRegex = Regex("""v[0-9]+\.[0-9]+\.[0-9]+""")
  }
}
