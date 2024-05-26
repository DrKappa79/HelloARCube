package net.drkappa.app.stepthree

import android.content.Context
import android.opengl.GLSurfaceView

class StepThreeGLSurfaceView  (renderer: Renderer, context: Context) : GLSurfaceView(context) {

    init {
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true
        setRenderer(renderer)
    }

}