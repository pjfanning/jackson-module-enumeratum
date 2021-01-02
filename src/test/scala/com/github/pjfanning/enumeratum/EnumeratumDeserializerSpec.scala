package com.github.pjfanning.enumeratum

import com.fasterxml.jackson.databind.json.JsonMapper
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EnumeratumDeserializerSpec extends AnyWordSpec with Matchers {
  "EnumeratumModule" should {
    "deserialize Color" in {
      val mapper = JsonMapper.builder().addModule(EnumeratumModule).build()
      val red = s""""${Color.Red.entryName}""""
      mapper.readValue(red, classOf[Color]) shouldEqual(Color.Red)
    }
    "deserialize Color (uppercase)" in {
      val mapper = JsonMapper.builder().addModule(EnumeratumModule).build()
      val red = s""""${Color.Red.entryName.toUpperCase}""""
      mapper.readValue(red, classOf[Color]) shouldEqual(Color.Red)
    }
  }
}
