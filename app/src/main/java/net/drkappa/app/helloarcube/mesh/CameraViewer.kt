package net.drkappa.app.helloarcube.mesh

import com.google.ar.core.Coordinates2d
import com.google.ar.core.Frame
import net.drkappa.app.helloarcube.utils.ARGLUtils
import net.drkappa.app.helloarcube.utils.BufferUtils
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class CameraViewer : Mesh() {

    private val vertexShaderCode =
        "attribute vec4 aPosition;\n"+
                "attribute vec2 aTexture;\n"+
                "varying vec2 vTexture;\n"+
                "void main(){\n"+
                "   vTexture = aTexture;\n"+
                "   gl_Position = aPosition;\n"+
                "}"

    private val fragmentShaderCode =
        "#extension GL_OES_EGL_image_external : require\n"+
                "precision mediump float;\n"+
                "uniform samplerExternalOES sTexture\n;"+
                "varying vec2 vTexture;\n"+
                "void main(){\n"+
                "   gl_FragColor = texture2D(sTexture, vTexture);\n"+
                "}"

    val quadVertices = floatArrayOf(
        /*0:*/-1f, -1f,1f,1f,
        /*1:*/+1f, -1f,1f,1f,
        /*2:*/-1f, +1f,1f,1f,
        /*3:*/+1f, +1f,1f,1f
    )
    val quadUV = floatArrayOf(
        /*0:*/-1.0f, -1.0f,
        /*1:*/1.0f, -1.0f,
        /*2:*/-1.0f, 1.0f,
        /*3:*/1.0f, 1.0f
    )
    val quadIndices = shortArrayOf(
        /* First triangle */ 0, 1, 2,
        /* Second triangle */ 1, 3, 2
    )

    var textureID = -1

    private var shaderProgram : Int = -1

    lateinit var vertexBuffer: FloatBuffer

    lateinit var UVBuffer: FloatBuffer
    lateinit var UVBufferTransformed: FloatBuffer

    lateinit var indexBuffer: ShortBuffer


    fun getTextureName() : Int {
        return textureID
    }

    fun updateBuffersFromFrame(frame: Frame){
        frame.transformCoordinates2d(
            Coordinates2d.OPENGL_NORMALIZED_DEVICE_COORDINATES,
            UVBuffer,
            Coordinates2d.TEXTURE_NORMALIZED,
            UVBufferTransformed
        )
        UVBuffer.position(0)
        UVBufferTransformed.position(0)
    }

    override fun load() {
        shaderProgram = ARGLUtils.createShaderProgram(vertexShaderCode, fragmentShaderCode)
        vertexBuffer = BufferUtils.allocateFloatBuffer(quadVertices)
        UVBuffer = BufferUtils.allocateFloatBuffer(quadUV)
        UVBufferTransformed = BufferUtils.allocateFloatBuffer(quadUV)
        indexBuffer = BufferUtils.allocateShortBuffer(quadIndices)
    }

    override fun draw() {
        ARGLUtils.setShaderProgram(shaderProgram)
        ARGLUtils.enableTexture(shaderProgram,textureID)
        val positionHandle = ARGLUtils.setVertexData(shaderProgram, vertexBuffer)
        val UVHandle = ARGLUtils.setTextureData(shaderProgram, UVBufferTransformed)
        ARGLUtils.drawTriangles(indexBuffer)
        ARGLUtils.unsetData(UVHandle)
        ARGLUtils.unsetData(positionHandle)
    }

    override fun cloneDrawData(): Mesh {
        val cameraViewer = CameraViewer()
        cameraViewer.textureID = textureID
        cameraViewer.shaderProgram = shaderProgram
        cameraViewer.vertexBuffer = vertexBuffer
        cameraViewer.UVBuffer = UVBuffer
        cameraViewer.indexBuffer = indexBuffer
        return cameraViewer
    }

}