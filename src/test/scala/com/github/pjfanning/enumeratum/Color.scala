package com.github.pjfanning.enumeratum

import enumeratum._

sealed abstract class Color(override val entryName: String) extends EnumEntry

object Color extends Enum[Color] {

  val values = findValues

  case object Red extends Color("red")
  case object Green extends Color("green")
  case object Blue extends Color("blue")
}
