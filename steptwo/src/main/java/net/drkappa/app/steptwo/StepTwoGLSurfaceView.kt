package net.drkappa.app.steptwo

import android.content.Context
import android.opengl.GLSurfaceView

class StepTwoGLSurfaceView (renderer: Renderer, context: Context) : GLSurfaceView(context) {

    init {
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true
        setRenderer(renderer)
    }

}