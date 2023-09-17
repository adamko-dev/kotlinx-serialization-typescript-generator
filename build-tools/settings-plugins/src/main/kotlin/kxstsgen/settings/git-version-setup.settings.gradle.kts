package kxstsgen.settings

val gitVersion = providers.of(GitVersionSource::class) {
  parameters {
    projectRootDir.set(rootDir)
    gitDescribe.convention(exec {
      commandLine(
        "git",
        "describe",
        "--always",
        "--tags",
        "--dirty=-DIRTY",
        "--broken=-BROKEN",
        "--match=v[0-9]*\\.[0-9]*\\.[0-9]*",
      )
    })
    currentBranchName.convention(exec {
      commandLine(
        "git",
        "branch",
        "--show-current",
      )
    })
    currentCommitHash.convention(exec {
      commandLine(
        "git",
        "rev-parse",
        "--short",
        "HEAD",
      )
    })
  }
}

gradle.allprojects {
  plugins.apply(GitVersionPlugin::class)
  extensions.add<GitVersion>("gitVersion", GitVersion(gitVersion))
}
