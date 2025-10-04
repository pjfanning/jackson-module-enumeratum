package com.github.pjfanning.enumeratum.ser

import com.fasterxml.jackson.annotation.JsonFormat
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.ser.Serializers
import tools.jackson.databind._
import com.github.pjfanning.enumeratum.JacksonEnumeratumModule
import enumeratum.EnumEntry

import scala.languageFeature.postfixOps

private object EnumeratumSerializerShared {
  private[ser] val EnumEntryClass = classOf[EnumEntry]
}

private object EnumeratumSerializer extends ValueSerializer[EnumEntry] {
  override def serialize(value: EnumEntry, jgen: JsonGenerator, provider: SerializationContext): Unit =
    provider.writeValue(jgen, value.entryName)

}

private object EnumeratumKeySerializer extends ValueSerializer[EnumEntry] {
  override def serialize(value: EnumEntry, jgen: JsonGenerator, provider: SerializationContext): Unit =
    jgen.writeName(value.entryName)
}

private object EnumeratumSerializerResolver extends Serializers.Base {

  override def findSerializer(config: SerializationConfig, javaType: JavaType,
                              beanDescRef: BeanDescription.Supplier,
                              formatOverrides: JsonFormat.Value): ValueSerializer[EnumEntry] =
    if (EnumeratumSerializerShared.EnumEntryClass.isAssignableFrom(javaType.getRawClass))
      EnumeratumSerializer
    else None.orNull
}

private object EnumeratumKeySerializerResolver extends Serializers.Base {

  override def findSerializer(config: SerializationConfig, javaType: JavaType,
                              beanDescRef: BeanDescription.Supplier,
                              formatOverrides: JsonFormat.Value): ValueSerializer[EnumEntry] =
    if (EnumeratumSerializerShared.EnumEntryClass.isAssignableFrom(javaType.getRawClass))
      EnumeratumKeySerializer
    else None.orNull
}

trait EnumeratumSerializerModule extends JacksonEnumeratumModule {
  override def getModuleName: String = "EnumeratumSerializerModule"
  this += { _ addSerializers EnumeratumSerializerResolver }
  this += { _ addKeySerializers EnumeratumKeySerializerResolver }
}
