package com.github.pjfanning.enumeratum

import com.fasterxml.jackson.databind.json.JsonMapper
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EnumeratumSerializerSpec extends AnyWordSpec with Matchers {
  "EnumeratumModule" should {
    "serialize Color" in {
      val mapper = JsonMapper.builder().addModule(EnumeratumModule).build()
      mapper.writeValueAsString(Color.Red) shouldEqual (s""""${Color.Red.entryName}"""")
    }
  }
}
