package net.drkappa.app.steptwo

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class StepTwoGLRenderer : GLSurfaceView.Renderer {

    lateinit var fullScreenQuad: FullScreenQuad

    private var loaded = false

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1.0f)
        GLES20.glEnable( GLES20.GL_DEPTH_TEST )
        GLES20.glDepthFunc( GLES20.GL_LEQUAL )
    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {

    }

    override fun onDrawFrame(p0: GL10?) {
        if( !loaded ){
            loadScene()
        }

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        fullScreenQuad.draw()

    }

    private fun loadScene(){
        fullScreenQuad = FullScreenQuad()
        fullScreenQuad.load()
        loaded = true
    }

}