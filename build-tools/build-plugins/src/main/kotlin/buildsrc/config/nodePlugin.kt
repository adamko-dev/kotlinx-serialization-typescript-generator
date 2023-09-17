package buildsrc.config

import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

fun NpmTask.args(values: String) {
  args.set(parseSpaceSeparatedArgs(values))
}
