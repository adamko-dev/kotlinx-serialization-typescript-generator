package dev.adamko.kxstsgen

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule


class KxsTsGenerator(
  private val config: KxsTsConfig = KxsTsConfig(),
) {

  constructor(
    serializersModule: SerializersModule,
  ) : this(KxsTsConfig(serializersModule = serializersModule))


  fun generate(vararg serializers: KSerializer<*>): String {

    val processor = KxsTsProcessor(config)

    serializers.forEach { processor.addSerializer(it) }

    return processor.process()
  }
}
