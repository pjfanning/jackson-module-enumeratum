package com.github.pjfanning.enumeratum.deser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind._
import com.github.pjfanning.enumeratum.JacksonModule
import enumeratum.{Enum, EnumEntry}

import scala.languageFeature.postfixOps

private case class EnumeratumDeserializer[T <: EnumEntry](clazz: Class[T]) extends StdDeserializer[T](clazz) {
  private val clazzName = clazz.getName
  private val enum = Class.forName(clazzName + "$").getField("MODULE$").get(null).asInstanceOf[Enum[T]]

  override def deserialize(p: JsonParser, ctxt: DeserializationContext): T = {
    emptyToNone(p.getValueAsString) match {
      case Some(text) => enum.withNameInsensitive(text)
      case _ => None.orNull.asInstanceOf[T]
    }
  }

  private def emptyToNone(str: String): Option[String] = {
    Option(str).map(_.trim) match {
      case Some(s) if !s.isEmpty => Some(s)
      case _ => None
    }
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
