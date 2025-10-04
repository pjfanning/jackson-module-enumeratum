package com.github.pjfanning.enumeratum.deser

import tools.jackson.core.JsonParser
import tools.jackson.databind._
import tools.jackson.databind.deser.std.StdDeserializer
import tools.jackson.databind.deser.{Deserializers, KeyDeserializers}
import com.github.pjfanning.enumeratum.JacksonEnumeratumModule
import enumeratum.{Enum, EnumEntry}

import scala.languageFeature.postfixOps

private object EnumeratumDeserializerShared {
  private[deser] val EnumEntryClass = classOf[EnumEntry]

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

  override def findBeanDeserializer(javaType: JavaType, config: DeserializationConfig, beanDescRef: BeanDescription.Supplier): ValueDeserializer[EnumEntry] =
    if (EnumeratumDeserializerShared.EnumEntryClass isAssignableFrom javaType.getRawClass)
      EnumeratumDeserializer(javaType.getRawClass.asInstanceOf[Class[EnumEntry]])
    else None.orNull

  override def hasDeserializerFor(config: DeserializationConfig, valueType: Class[_]): Boolean =
    EnumeratumDeserializerShared.EnumEntryClass isAssignableFrom valueType
}

private object EnumeratumKeyDeserializerResolver extends KeyDeserializers {

  override def findKeyDeserializer(javaType: JavaType, config: DeserializationConfig, beanDescRef: BeanDescription.Supplier): KeyDeserializer =
    if (EnumeratumDeserializerShared.EnumEntryClass isAssignableFrom javaType.getRawClass)
      EnumeratumKeyDeserializer(javaType.getRawClass.asInstanceOf[Class[EnumEntry]])
    else None.orNull
}

trait EnumeratumDeserializerModule extends JacksonEnumeratumModule {
  override def getModuleName: String = "EnumeratumDeserializerModule"
  this += { _ addDeserializers EnumeratumDeserializerResolver }
  this += { _ addKeyDeserializers EnumeratumKeyDeserializerResolver }
}
