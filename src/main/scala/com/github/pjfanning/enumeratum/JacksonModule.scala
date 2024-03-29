package com.github.pjfanning.enumeratum

import java.util.Properties

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.core.util.VersionUtil
import com.fasterxml.jackson.databind.Module.SetupContext
import com.fasterxml.jackson.databind.`type`.TypeModifier
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.ser.{BeanSerializerModifier, Serializers}
import com.fasterxml.jackson.databind.Module

import scala.collection.JavaConverters._
import scala.collection.mutable

object JacksonModule {
  private val cls = classOf[JacksonModule]
  private val buildPropsFilename = cls.getPackage.getName.replace('.','/') + "/build.properties"
  lazy val buildProps: mutable.Map[String, String] = {
    val props = new Properties
    val stream = cls.getClassLoader.getResourceAsStream(buildPropsFilename)
    if (stream ne null) props.load(stream)

    props.asScala
  }
  lazy val version: Version = {
    val groupId = buildProps("groupId")
    val artifactId = buildProps("artifactId")
    val version = buildProps("version")
    VersionUtil.parseVersion(version, groupId, artifactId)
  }
}

trait JacksonModule extends Module {

  private val initializers = Seq.newBuilder[SetupContext => Unit]

  override def getModuleName = "JacksonModule"

  override def version = JacksonModule.version

  def setupModule(context: SetupContext): Unit = {
    initializers.result().foreach(_ apply context)
  }

  protected def +=(init: SetupContext => Unit): this.type = { initializers += init; this }
  protected def +=(ser: Serializers): this.type = this += (_ addSerializers ser)
  protected def +=(deser: Deserializers): this.type = this += (_ addDeserializers deser)
  protected def +=(typeMod: TypeModifier): this.type = this += (_ addTypeModifier typeMod)
  protected def +=(beanSerMod: BeanSerializerModifier): this.type = this += (_ addBeanSerializerModifier beanSerMod)
}
