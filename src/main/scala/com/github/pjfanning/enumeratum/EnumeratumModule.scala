package com.github.pjfanning.enumeratum

import com.github.pjfanning.enumeratum.deser.EnumeratumDeserializerModule
import com.github.pjfanning.enumeratum.ser.EnumeratumSerializerModule

class EnumeratumModule extends EnumeratumSerializerModule with EnumeratumDeserializerModule

object EnumeratumModule extends EnumeratumModule {
  override def getModuleName: String = "EnumeratumModule"
}
