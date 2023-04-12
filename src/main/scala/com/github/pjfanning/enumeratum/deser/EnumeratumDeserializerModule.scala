package com.github.pjfanning.enumeratum.deser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.deser.{Deserializers, KeyDeserializers}
import com.github.pjfanning.enumeratum.JacksonModule
import enumeratum.{Enum, EnumEntry}

import scala.languageFeature.postfixOps

private object EnumeratumDeserializerShared {
  val EnumEntryClass = classOf[EnumEntry]

  def emptyToNone(str: String): Option[String] = {
    Option(str).map(_.trim) match {
      case Some(s) if s.nonEmpty => Some(s)
      case _ => None
    }
  }

  def getEnumInstance[T <: EnumEntry](clazzName: String): Enum[T] =
    Class.forName(clazzName + "$").getField("MODULE$").get(null).asInstanceOf[Enum[T]]

}

private case class EnumeratumDeserializer[T <: EnumEntry](clazz: Class[T]) extends StdDeserializer[T](clazz) {
  import EnumeratumDeserializerShared._

  private val clazzName = clazz.getName
  private val enumInstance = getEnumInstance[T](clazzName)

  override def deserialize(p: JsonParser, ctxt: DeserializationContext): T = {
    emptyToNone(p.getValueAsString) match {
      case Some(text) => enumInstance.withNameInsensitive(text)
      case _ => None.orNull.asInstanceOf[T]
    }
  }
}

private case class EnumeratumKeyDeserializer[T <: EnumEntry](clazz: Class[T]) extends KeyDeserializer {
  import EnumeratumDeserializerShared._

  private val clazzName = clazz.getName
  private val enumInstance = getEnumInstance(clazzName)

  override def deserializeKey(key: String, ctxt: DeserializationContext): AnyRef = {
    emptyToNone(key) match {
      case Some(text) => enumInstance.withNameInsensitive(text)
      case None => None.orNull.asInstanceOf[T]
    }
  }
}

private object EnumeratumDeserializerResolver extends Deserializers.Base {

  override def findBeanDeserializer(javaType: JavaType, config: DeserializationConfig, beanDesc: BeanDescription): JsonDeserializer[EnumEntry] =
    if (EnumeratumDeserializerShared.EnumEntryClass isAssignableFrom javaType.getRawClass)
      EnumeratumDeserializer(javaType.getRawClass.asInstanceOf[Class[EnumEntry]])
    else None.orNull
}

private object EnumeratumKeyDeserializerResolver extends KeyDeserializers {

  override def findKeyDeserializer(javaType: JavaType, config: DeserializationConfig, beanDesc: BeanDescription): KeyDeserializer =
    if (EnumeratumDeserializerShared.EnumEntryClass isAssignableFrom javaType.getRawClass)
      EnumeratumKeyDeserializer(javaType.getRawClass.asInstanceOf[Class[EnumEntry]])
    else None.orNull
}

trait EnumeratumDeserializerModule extends JacksonModule {
  override def getModuleName: String = "EnumeratumDeserializerModule"
  this += { _ addDeserializers EnumeratumDeserializerResolver }
  this += { _ addKeyDeserializers EnumeratumKeyDeserializerResolver }
}
