package org.emulinker.kaillera.controller.v086.protocol

import java.nio.ByteBuffer
import org.emulinker.kaillera.controller.messaging.MessageFormatException
import org.emulinker.kaillera.controller.messaging.ParseException
import org.emulinker.kaillera.controller.v086.protocol.V086Message.Companion.validateMessageNumber
import org.emulinker.util.UnsignedUtil

data class GameKick
    @Throws(MessageFormatException::class)
    constructor(override val messageNumber: Int, val userId: Int) : V086Message() {

  override val bodyLength = 3
  override val messageId = ID

  init {
    validateMessageNumber(messageNumber)
    require(userId in 0..0xFFFF) { "UserID out of acceptable range: $userId" }
  }

  public override fun writeBodyTo(buffer: ByteBuffer) {
    buffer.put(0x00.toByte())
    UnsignedUtil.putUnsignedShort(buffer, userId)
  }

  companion object {
    const val ID: Byte = 0x0F

    @Throws(ParseException::class, MessageFormatException::class)
    fun parse(messageNumber: Int, buffer: ByteBuffer): GameKick {
      if (buffer.remaining() < 3) throw ParseException("Failed byte count validation!")
      val b = buffer.get()
      /*SF MOD
      if (b != 0x00)
      	throw new MessageFormatException("Invalid " + DESC + " format: byte 0 = " + EmuUtil.byteToHex(b));
      */
      return GameKick(messageNumber, UnsignedUtil.getUnsignedShort(buffer))
    }
  }
}