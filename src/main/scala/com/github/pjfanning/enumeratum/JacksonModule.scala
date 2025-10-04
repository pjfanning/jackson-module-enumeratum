package com.github.pjfanning.enumeratum

import java.util.Properties

import tools.jackson.core.Version
import tools.jackson.core.util.VersionUtil
import tools.jackson.databind.JacksonModule
import tools.jackson.databind.JacksonModule.SetupContext
import tools.jackson.databind.`type`.TypeModifier
import tools.jackson.databind.deser.Deserializers
import tools.jackson.databind.ser.{Serializers, ValueSerializerModifier}

import scala.collection.JavaConverters._
import scala.collection.mutable

object JacksonEnumeratumModule {
  private val cls = classOf[JacksonEnumeratumModule]
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

trait JacksonEnumeratumModule extends JacksonModule {

  private val initializers = Seq.newBuilder[SetupContext => Unit]

  override def getModuleName = "JacksonEnumeratumModule"

  override def version = JacksonEnumeratumModule.version

  def setupModule(context: SetupContext): Unit = {
    initializers.result().foreach(_ apply context)
  }

  protected def +=(init: SetupContext => Unit): this.type = { initializers += init; this }
  protected def +=(ser: Serializers): this.type = this += (_ addSerializers ser)
  protected def +=(deser: Deserializers): this.type = this += (_ addDeserializers deser)
  protected def +=(typeMod: TypeModifier): this.type = this += (_ addTypeModifier typeMod)
  protected def +=(valueSerMod: ValueSerializerModifier): this.type = this += (_ addSerializerModifier valueSerMod)
}
