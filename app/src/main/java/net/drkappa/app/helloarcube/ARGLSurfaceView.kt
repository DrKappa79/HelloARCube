package net.drkappa.app.helloarcube

import android.content.Context
import android.opengl.GLSurfaceView

class ARGLSurfaceView(renderer: Renderer, context: Context) : GLSurfaceView(context) {

    init {
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true
        setRenderer(renderer)
    }

}