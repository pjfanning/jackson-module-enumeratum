package com.github.pjfanning.enumeratum

import enumeratum._

sealed trait Fruit extends EnumEntry

object Fruit extends Enum[Fruit] {

  val values = findValues

  case object Apple extends Fruit
  case object Pear extends Fruit
  case object Blueberry extends Fruit

}

