package com.github.pjfanning.enumeratum.ser

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ser.Serializers
import com.fasterxml.jackson.databind._
import com.github.pjfanning.enumeratum.JacksonModule
import enumeratum.EnumEntry

import scala.languageFeature.postfixOps

private object EnumeratumSerializer extends JsonSerializer[EnumEntry] {
  def serialize(value: EnumEntry, jgen: JsonGenerator, provider: SerializerProvider): Unit =
    jgen.writeString(value.entryName)
}

private object EnumeratumSerializerResolver extends Serializers.Base {
  private val EnumEntryClass = classOf[EnumEntry]

  override def findSerializer(config: SerializationConfig, javaType: JavaType, beanDesc: BeanDescription): JsonSerializer[EnumEntry] =
    if (EnumEntryClass.isAssignableFrom(javaType.getRawClass))
      EnumeratumSerializer
    else None.orNull
}

trait EnumeratumSerializerModule extends JacksonModule {
  this += { _ addSerializers EnumeratumSerializerResolver }
}
