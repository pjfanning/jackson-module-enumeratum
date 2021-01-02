package com.github.pjfanning.enumeratum

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EnumeratumSerializerSpec extends AnyWordSpec with Matchers {
  "EnumeratumModule" should {
    "serialize Color" in {
      val mapper = JsonMapper.builder().addModule(EnumeratumModule).build()
      mapper.writeValueAsString(Color.Red) shouldEqual (s""""${Color.Red.entryName}"""")
    }
    "serialize case class" in {
      val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
      val car = Car("Volga", Color.Blue)
      val json = mapper.writeValueAsString(car)
      json should include(s""""model":"${car.model}"""")
      json should include(s""""color":"${car.color.entryName}"""")
    }
  }
}
