package net.drkappa.app.stepthree.utils

import android.opengl.GLES11Ext
import android.opengl.GLES20
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class GLUtils {

    companion object{
        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            return GLES20.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

        fun createShaderProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {
            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode)
            val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode)
            return GLES20.glCreateProgram().also {

                // add the vertex shader to program
                GLES20.glAttachShader(it, vertexShader)

                // add the fragment shader to program
                GLES20.glAttachShader(it, fragmentShader)

                // creates OpenGL ES program executables
                GLES20.glLinkProgram(it)

            }
        }

        fun setShaderProgram(shaderProgram: Int){
            GLES20.glUseProgram(shaderProgram)
        }

        fun setVertexData(shaderProgram : Int, data : FloatBuffer) : Int{
            return setData(shaderProgram,"aPosition",4,data)
        }

        fun setTextureData(shaderProgram : Int, data : FloatBuffer) : Int{
            return setData(shaderProgram,"aTexture",2,data)
        }

        fun setData(shaderProgram: Int, name: String,size: Int, data: FloatBuffer) : Int{
            return GLES20.glGetAttribLocation(shaderProgram, name).also {
                GLES20.glEnableVertexAttribArray(it)
                GLES20.glVertexAttribPointer(
                    it,
                    size,
                    GLES20.GL_FLOAT,
                    false,
                    0,
                    data
                )
            }
        }

        fun unsetData(handle : Int){
            GLES20.glDisableVertexAttribArray(handle)
        }

        fun drawTriangles(indexBuffer: ShortBuffer){
            GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,indexBuffer.limit(),
                GLES20.GL_UNSIGNED_SHORT,
                indexBuffer)
        }

        fun enableTexture(shaderProgram : Int,textureID:Int){
            GLES20.glGetUniformLocation(shaderProgram,"sTexture").also {
                GLES20.glUniform1i(it, 0)
            }
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textureID)
        }

        fun createCameraTexture() : Int{
            var textureID = IntArray(1)
            GLES20.glGenTextures(1, textureID, 0)
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureID[0])
            GLES20.glTexParameteri(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE
            )
            GLES20.glTexParameteri(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE
            )
            GLES20.glTexParameteri(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR
            )
            GLES20.glTexParameteri(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            return textureID[0]
        }

    }
}