package net.drkappa.app.steptwo

import android.opengl.GLES20

class StepTwoGLUtils {
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
                // link OpenGL ES programs
                GLES20.glLinkProgram(it)
            }
        }

        fun setShaderProgram(shaderProgram: Int){
            GLES20.glUseProgram(shaderProgram)
        }

    }
}