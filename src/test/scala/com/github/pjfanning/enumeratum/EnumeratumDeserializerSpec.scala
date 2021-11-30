package com.github.pjfanning.enumeratum

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EnumeratumDeserializerSpec extends AnyWordSpec with Matchers {
  "EnumeratumModule" should {
    "deserialize Color" in {
      val mapper = JsonMapper.builder().addModule(EnumeratumModule).build()
      val red = s""""${Color.Red.entryName}""""
      mapper.readValue(red, classOf[Color]) shouldEqual(Color.Red)
    }
    "deserialize Color with non-singleton EnumeratumModule" in {
      val mapper = JsonMapper.builder().addModule(new EnumeratumModule).build()
      val red = s""""${Color.Red.entryName}""""
      mapper.readValue(red, classOf[Color]) shouldEqual(Color.Red)
    }
    "deserialize Color (uppercase)" in {
      val mapper = JsonMapper.builder().addModule(EnumeratumModule).build()
      val red = s""""${Color.Red.entryName.toUpperCase}""""
      mapper.readValue(red, classOf[Color]) shouldEqual(Color.Red)
    }
    "deserialize Ctx.Color" in {
      val mapper = JsonMapper.builder().addModule(EnumeratumModule).build()
      val red = s""""${Ctx.Color.Red.entryName}""""
      mapper.readValue(red, classOf[Ctx.Color]) shouldEqual(Ctx.Color.Red)
    }
    "deserialize case class" in {
      val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
      val car = Car("Volga", Color.Blue)
      val json = mapper.writeValueAsString(car)
      mapper.readValue(json, classOf[Car]) shouldEqual(car)
    }
    "deserialize case class with option" in {
      val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
      val car = CarWithOptionalColor("Volga", Some(Color.Blue))
      val json = mapper.writeValueAsString(car)
      mapper.readValue(json, classOf[CarWithOptionalColor]) shouldEqual(car)
      val car2 = CarWithOptionalColor("Volga", None)
      val json2 = mapper.writeValueAsString(car2)
      mapper.readValue(json2, classOf[CarWithOptionalColor]) shouldEqual(car2)
    }
  }
}
