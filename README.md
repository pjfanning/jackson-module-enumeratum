# jackson-module-enumeratum

![Build Status](https://github.com/pjfanning/jackson-module-enumeratum/actions/workflows/ci.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.pjfanning/jackson-module-enumeratum_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.pjfanning/jackson-module-enumeratum_2.13)

Enumeratum support for Jackson

```
libraryDependencies += "com.github.pjfanning" %% "jackson-module-enumeratum" % "2.14.0"
```

```
  val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
  val car = Car("Volga", Color.Blue)
  val json = mapper.writeValueAsString(car)
  mapper.readValue(json, classOf[Car]) shouldEqual(car)
```      
