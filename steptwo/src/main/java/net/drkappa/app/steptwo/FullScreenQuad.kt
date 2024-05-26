package net.drkappa.app.steptwo

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class FullScreenQuad {

    private val vertexShaderCode =
        "attribute vec4 aPosition;\n" +
                "varying vec2 vColor;\n"+
                "void main() {\n" +
                " vColor = (aPosition.xy+vec2(1.0,1.0))/2.0;\n"+
                " gl_Position = aPosition;\n" +
                "}"

    private val fragmentShaderCode =

        "precision mediump float;\n" +
                "varying vec2 vColor;\n" +
                "void main() {\n" +
                "  gl_FragColor = vec4(vColor.x,vColor.y,(vColor.x+vColor.y)/2.0,1.0);\n" +
                "}"

    private var shaderProgram : Int = -1

    private val quadVertices = floatArrayOf(
        /*0:*/-1f, -1f, 1f,1f,
        /*1:*/+1f, -1f, 1f,1f,
        /*2:*/-1f, +1f, 1f,1f,
        /*3:*/+1f, +1f, 1f,1f
    )

    private val quadIndices = shortArrayOf(
        /* First triangle */ 0, 1, 2,
        /* Second triangle */ 1, 3, 2
    )

    private val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(quadVertices.size * 4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(quadVertices)
            position(0)
        }
    }

    private val indexBuffer: ShortBuffer = ByteBuffer.allocateDirect(quadIndices.size * 2).run {
        order(ByteOrder.nativeOrder())
        asShortBuffer().apply {
            put(quadIndices)
            position(0)
        }
    }

    fun load(){
        shaderProgram = StepTwoGLUtils.createShaderProgram(vertexShaderCode, fragmentShaderCode)
    }

    fun draw(){
        if( shaderProgram == -1 )
            return

        GLES20.glUseProgram(shaderProgram)
        val handle = GLES20.glGetAttribLocation(shaderProgram, "aPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                4,
                GLES20.GL_FLOAT,
                false,
                0,
                vertexBuffer
            )
        }
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,indexBuffer.limit(),
            GLES20.GL_UNSIGNED_SHORT,
            indexBuffer)

        GLES20.glDisableVertexAttribArray(handle)

    }

}