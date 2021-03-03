package com.github.pjfanning.enumeratum.deser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind._
import com.github.pjfanning.enumeratum.JacksonModule
import enumeratum.EnumEntry

import scala.languageFeature.postfixOps
import scala.util.Try

private case class EnumeratumDeserializer[T <: EnumEntry](clazz: Class[T]) extends StdDeserializer[T](clazz) {
  private val clazzName = clazz.getName
  private val enum = Class.forName(clazzName + "$").getField("MODULE$").get(null).asInstanceOf[Enum[T]]

  override def deserialize(p: JsonParser, ctxt: DeserializationContext): T = {
    val text = p.getValueAsString
    enum.withNameInsensitive(text)
  }
}

private object EnumeratumDeserializerResolver extends Deserializers.Base {
  private val EnumEntryClass = classOf[EnumEntry]

  override def findBeanDeserializer(javaType: JavaType, config: DeserializationConfig, beanDesc: BeanDescription): JsonDeserializer[EnumEntry] =
    if (EnumEntryClass isAssignableFrom javaType.getRawClass)
      EnumeratumDeserializer(javaType.getRawClass.asInstanceOf[Class[EnumEntry]])
    else None.orNull
}

trait EnumeratumDeserializerModule extends JacksonModule {
  this += { _ addDeserializers EnumeratumDeserializerResolver }
}
