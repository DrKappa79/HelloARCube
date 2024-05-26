package net.drkappa.app.stepone

import android.content.Context
import android.opengl.GLSurfaceView

class StepOneGLSurfaceView (renderer: Renderer, context: Context) : GLSurfaceView(context) {

    init {
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true
        setRenderer(renderer)
    }

}