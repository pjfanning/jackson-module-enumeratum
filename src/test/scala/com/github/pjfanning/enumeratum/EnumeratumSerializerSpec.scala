package com.github.pjfanning.enumeratum

import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.scala.DefaultScalaModule
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EnumeratumSerializerSpec extends AnyWordSpec with Matchers {
  "EnumeratumModule" should {
    "serialize enums" in {
      val mapper = JsonMapper.builder().addModule(EnumeratumModule).build()
      mapper.writeValueAsString(Color.Red) shouldEqual (s""""${Color.Red.entryName}"""")
      mapper.writeValueAsString(Fruit.Apple) shouldEqual (s""""${Fruit.Apple.entryName}"""")
    }
    "deserialize Colors" in {
      val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
      val colors = Colors(Set(Color.Red, Color.Green))
      val json = mapper.writeValueAsString(colors)
      mapper.readValue(json, classOf[Colors]) shouldEqual(colors)
    }
    "serialize enums with non-singleton EnumeratumModule" in {
      val mapper = JsonMapper.builder().addModule(new EnumeratumModule).build()
      mapper.writeValueAsString(Color.Red) shouldEqual (s""""${Color.Red.entryName}"""")
      mapper.writeValueAsString(Fruit.Apple) shouldEqual (s""""${Fruit.Apple.entryName}"""")
    }
    "serialize Ctx.Color" in {
      val mapper = JsonMapper.builder().addModule(EnumeratumModule).build()
      mapper.writeValueAsString(Ctx.Color.Red) shouldEqual (s""""${Ctx.Color.Red.entryName}"""")
    }
    "serialize case class" in {
      val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
      val car = Car("Volga", Color.Blue)
      val json = mapper.writeValueAsString(car)
      json should include(s""""model":"${car.model}"""")
      json should include(s""""color":"${car.color.entryName}"""")
    }
    "serialize case class with option" in {
      val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
      val car = CarWithOptionalColor("Volga", Some(Color.Blue))
      val json = mapper.writeValueAsString(car)
      json should include(s""""model":"${car.model}"""")
      json should include(s""""color":"${Color.Blue.entryName}"""")
      val car2 = CarWithOptionalColor("Volga", None)
      val json2 = mapper.writeValueAsString(car2)
      json2 should include(s""""model":"${car2.model}"""")
      json2 should include(s""""color":null""")
    }
    "serialize map with enum key" in {
      val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
      val colorMap = Map(Color.Red -> "red")
      val fruitMap = Map(Fruit.Apple -> Color.Red, Fruit.Pear -> Color.Green, Fruit.Blueberry -> Color.Blue)
      val jsonColors = mapper.writeValueAsString(colorMap)
      val jsonFruits = mapper.writeValueAsString(fruitMap)
      jsonColors should include(s"""{"${Color.Red.entryName}":"red"}""")
      jsonFruits should include(s"""{"${Fruit.Apple.entryName}":"${Color.Red.entryName}","${Fruit.Pear.entryName}":"${Color.Green.entryName}","${Fruit.Blueberry.entryName}":"${Color.Blue.entryName}"}""")
    }
  }
}
