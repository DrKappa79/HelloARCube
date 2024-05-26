package net.drkappa.app.helloarcube.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class BufferUtils {
    companion object{

        private const val FLOAT_SIZE_IN_BYTES = 4
        private const val SHORT_SIZE_IN_BYTES = 2

        fun allocateFloatBuffer(buffer : FloatArray ) : FloatBuffer{

            return ByteBuffer.allocateDirect(buffer.size * FLOAT_SIZE_IN_BYTES).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(buffer)
                    position(0)
                }
            }

        }

        fun allocateShortBuffer(buffer : ShortArray ) : ShortBuffer{

            return ByteBuffer.allocateDirect(buffer.size * SHORT_SIZE_IN_BYTES).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(buffer)
                    position(0)
                }
            }

        }
    }
}