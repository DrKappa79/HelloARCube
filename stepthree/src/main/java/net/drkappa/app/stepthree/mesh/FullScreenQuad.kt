package net.drkappa.app.stepthree.mesh

import net.drkappa.app.stepthree.utils.BufferUtils
import net.drkappa.app.stepthree.utils.GLUtils.Companion.createShaderProgram
import net.drkappa.app.stepthree.utils.GLUtils.Companion.drawTriangles
import net.drkappa.app.stepthree.utils.GLUtils.Companion.setShaderProgram
import net.drkappa.app.stepthree.utils.GLUtils.Companion.setVertexData
import net.drkappa.app.stepthree.utils.GLUtils.Companion.unsetData
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class FullScreenQuad : Mesh {
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

    val quadVertices = floatArrayOf(
        /*0:*/-1f, -1f, 1f,1f,
        /*1:*/+1f, -1f, 1f,1f,
        /*2:*/-1f, +1f, 1f,1f,
        /*3:*/+1f, +1f, 1f,1f
    )
    val quadUV = floatArrayOf(
        /*0:*/0.0f, 1.0f,
        /*1:*/1.0f, 1.0f,
        /*2:*/0.0f, 0.0f,
        /*3:*/1.0f, 0.0f
    )
    val quadIndices = shortArrayOf(
        /* First triangle */ 0, 1, 2,
        /* Second triangle */ 1, 3, 2
    )

    private lateinit var vertexBuffer: FloatBuffer

    private lateinit var UVBuffer: FloatBuffer

    private lateinit var indexBuffer: ShortBuffer


    override fun load(){
        shaderProgram = createShaderProgram(vertexShaderCode,fragmentShaderCode)
        vertexBuffer = BufferUtils.allocateFloatBuffer(quadVertices)
        UVBuffer = BufferUtils.allocateFloatBuffer(quadUV)
        indexBuffer = BufferUtils.allocateShortBuffer(quadIndices)
    }

    override fun draw(){

        setShaderProgram(shaderProgram)
        val positionHandle = setVertexData(shaderProgram, vertexBuffer)
        drawTriangles(indexBuffer)
        unsetData(positionHandle)

    }
}