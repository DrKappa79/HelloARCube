package net.drkappa.app.helloarcube

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import net.drkappa.app.helloarcube.scene.Scene
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ARGLRenderer : GLSurfaceView.Renderer {

    var screenWidth = 1080
    var screenHeight = 1920

    var arScene: Scene? = null

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glEnable( GLES20.GL_DEPTH_TEST )
        GLES20.glDepthFunc( GLES20.GL_LEQUAL )
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        arScene?.let {
            it.update()
            it.render()
        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        screenWidth = width
        screenHeight = height
    }

}