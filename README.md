# jackson-module-enumeratum

```
  val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
  val car = Car("Volga", Color.Blue)
  val json = mapper.writeValueAsString(car)
  mapper.readValue(json, classOf[Car]) shouldEqual(car)
```      
