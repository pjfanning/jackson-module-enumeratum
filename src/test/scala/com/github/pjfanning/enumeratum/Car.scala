package com.github.pjfanning.enumeratum

case class Car(model: String, color: Color)

case class CarWithOptionalColor(model: String, color: Option[Color])

case class Garage(name: String, carsByColor: Map[Color, Int])
