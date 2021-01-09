# jackson-module-enumeratum

[![Build Status](https://travis-ci.com/pjfanning/jackson-module-enumeratum.svg?branch=master)](https://travis-ci.com/pjfanning/jackson-module-enumeratum)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.pjfanning/jackson-module-enumeratum_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.pjfanning/jackson-module-enumeratum_2.13)

Enumeratum support for Jackson

```
  val mapper = JsonMapper.builder().addModule(DefaultScalaModule).addModule(EnumeratumModule).build()
  val car = Car("Volga", Color.Blue)
  val json = mapper.writeValueAsString(car)
  mapper.readValue(json, classOf[Car]) shouldEqual(car)
```      
