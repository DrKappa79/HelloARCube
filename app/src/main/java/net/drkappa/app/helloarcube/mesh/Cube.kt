package net.drkappa.app.helloarcube.mesh

import net.drkappa.app.helloarcube.utils.ARGLUtils
import net.drkappa.app.helloarcube.utils.BufferUtils
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube : Mesh() {

    val CUBE_HALF_SIZE = 0.05f

    val cubeVertices = floatArrayOf(
        -CUBE_HALF_SIZE, 0f, -CUBE_HALF_SIZE,1f,
        CUBE_HALF_SIZE, 0f, -CUBE_HALF_SIZE,1f,
        CUBE_HALF_SIZE, CUBE_HALF_SIZE*2f, -CUBE_HALF_SIZE,1f,
        -CUBE_HALF_SIZE, CUBE_HALF_SIZE*2f, -CUBE_HALF_SIZE,1f,
        -CUBE_HALF_SIZE, 0f, CUBE_HALF_SIZE,1f,
        CUBE_HALF_SIZE, 0f, CUBE_HALF_SIZE,1f,
        CUBE_HALF_SIZE, CUBE_HALF_SIZE*2f, CUBE_HALF_SIZE,1f,
        -CUBE_HALF_SIZE, CUBE_HALF_SIZE*2f, CUBE_HALF_SIZE,1f
    )

    val cubeColors = floatArrayOf(
        0f, 0f, 0f, 1f, 1f, 0f, 0f, 1f,
        1f, 1f, 0f, 1f, 0f, 1f, 0f, 1f,
        0f, 0f, 1f, 1f, 1f, 0f, 1f, 1f,
        1f, 1f, 1f, 1f, 0f, 1f, 1f, 1f
    )

    val cubeIndices = shortArrayOf(
        0, 4, 5, 0, 5, 1,
        1, 5, 6, 1, 6, 2,
        2, 6, 7, 2, 7, 3,
        3, 7, 4, 3, 4, 0,
        4, 7, 6, 4, 6, 5,
        3, 0, 1, 3, 1, 2
    )

    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var colorBuffer: FloatBuffer
    private lateinit var indexBuffer: ShortBuffer

    private val vertexShaderCode =
        "attribute vec4 aPosition;\n" +
                "attribute vec3 aColor;\n"+
                "varying vec3 vColor;\n" +
                "uniform mat4 uMVPMatrix;\n"+
                "void main() {\n" +
                "vColor = aColor;\n"+
                " gl_Position = uMVPMatrix*aPosition;\n" +
                "}"

    private val fragmentShaderCode =

        "precision mediump float;\n" +
                "varying vec3 vColor;\n" +
                "void main() {\n" +
                "  gl_FragColor = vec4(vColor,1.0);\n" +
                "}"

    private var shaderProgram : Int = -1

    override fun load(){
        shaderProgram = ARGLUtils.createShaderProgram(vertexShaderCode, fragmentShaderCode)
        vertexBuffer = BufferUtils.allocateFloatBuffer(cubeVertices)
        colorBuffer = BufferUtils.allocateFloatBuffer(cubeColors)
        indexBuffer = BufferUtils.allocateShortBuffer(cubeIndices)
    }

    override fun draw() {
        ARGLUtils.setShaderProgram(shaderProgram)
        val positionHandle = ARGLUtils.setVertexData(shaderProgram, vertexBuffer)
        val colorHandle = ARGLUtils.setColorData(shaderProgram, colorBuffer)
        ARGLUtils.setUniforms(shaderProgram,instanceData)
        ARGLUtils.drawTriangles(indexBuffer)
        ARGLUtils.unsetData(colorHandle)
        ARGLUtils.unsetData(positionHandle)
    }

    override fun cloneDrawData(): Mesh {
        var cube = Cube()
        cube.vertexBuffer = vertexBuffer
        cube.colorBuffer = colorBuffer
        cube.indexBuffer = indexBuffer
        cube.shaderProgram = shaderProgram
        return cube
    }

}