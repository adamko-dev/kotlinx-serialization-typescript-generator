package dev.adamko.kxstsgen

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSTopDownVisitor
import java.io.OutputStreamWriter
import kotlinx.serialization.SerialInfo

class KxsTsGenProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return KxsTsGenProcessor(environment.codeGenerator, environment.logger)
  }
}


class KxsTsGenProcessor(
  private val codeGenerator: CodeGenerator,
  private val logger: KSPLogger,
) : SymbolProcessor {
  private var invoked = false

  override fun process(resolver: Resolver): List<KSAnnotated> {

    val allFiles = resolver.getAllFiles().map { it.fileName }
    logger.warn(allFiles.toList().joinToString())
    if (invoked) {
      logger.info("Already invoked")
      return emptyList()
    }
    invoked = true

    codeGenerator
      .createNewFile(Dependencies(false), "", "Foo", "d.ts")
      .use { output ->

        OutputStreamWriter(output).use { writer ->

          val visitor = ClassVisitor(codeGenerator, resolver)
          resolver
            .getSymbolsWithAnnotation(TsExport::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .filter {
              when (it.classKind) {
                ClassKind.CLASS,
                ClassKind.ENUM_CLASS,
                ClassKind.OBJECT,
                ClassKind.INTERFACE  -> true
                ClassKind.ANNOTATION_CLASS,
                ClassKind.ENUM_ENTRY -> false
              }
            }
            .forEach {
              logger.info("Visiting $it")
              it.accept(visitor, writer)
            }

        }
      }
    return emptyList()
  }
}

class ClassVisitor(
  private val codeGenerator: CodeGenerator,
  private val resolver: Resolver
) : KSTopDownVisitor<OutputStreamWriter, Unit>() {

  override fun defaultHandler(node: KSNode, data: OutputStreamWriter) {
  }

  override fun visitClassDeclaration(
    classDeclaration: KSClassDeclaration,
    data: OutputStreamWriter
  ) {
    super.visitClassDeclaration(classDeclaration, data)

    classDeclaration.classKind

    val containingFile = classDeclaration.containingFile!!


    codeGenerator.createNewFile(
      Dependencies(true, containingFile),
      containingFile.packageName.toString(),
      "asd",
    )


    val symbolName = classDeclaration.simpleName.asString()

//    val constructor = classDeclaration.primaryConstructor ?: return
//
//    data.write(
//      buildString {
//        append("interface ")
//        append(symbolName)
//        appendLine(" {")
//
//        constructor.parameters
//          .filter { it.name != null }
//          .forEach {
//            append("  ")
//            append(it.name?.getShortName())
//            append(if (it.hasDefault) "?:" else ":")
//            append(" ")
//            append(tsPrimitive(it.type.resolve()))
//            appendLine(";")
//          }
//
//        appendLine("}")
//      }
//    )
  }

}
//
//
//data class KxsIntrospectionInfo(
//  val ksType: KSType,
//  val descriptorHashCode: KxsDescriptorHashCode,
//  val sealedParent: SerialDescriptor? = null,
//  val sealedSubclasses: Set<SerialDescriptor>? = null,
//)


/**
 * Mark [Serializable] classes that will be converted to TypeScript.
 */
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@SerialInfo
annotation class TsExport
