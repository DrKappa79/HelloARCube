package net.drkappa.app.stepthree

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import net.drkappa.app.stepthree.mesh.FullScreenQuad
import net.drkappa.app.stepthree.mesh.Mesh
import net.drkappa.app.stepthree.scene.Scene
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class StepThreeGLRenderer  : GLSurfaceView.Renderer {

    /*
    // Old version
        lateinit var fullScreenQuad: Mesh
        private var loaded = false
     */
    // New version
    var arScene: Scene? = null

    var screenWidth = 1080
    var screenHeight = 1920

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glEnable( GLES20.GL_DEPTH_TEST )
        GLES20.glDepthFunc( GLES20.GL_LEQUAL )
    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
        screenWidth = p1
        screenHeight = p2
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // New version
        arScene?.let {
                it.load()
            it.render()
        }



    }



}