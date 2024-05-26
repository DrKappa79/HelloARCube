package net.drkappa.app.helloarcube.camera

import android.opengl.Matrix
import com.google.ar.core.Camera

class ARCamera(var near:Float, var far:Float) {

    var VPMatrix = FloatArray(16)

    init {
        Matrix.setIdentityM(VPMatrix,0)
    }


    fun update(camera: Camera){
        val viewMatrix = FloatArray(16)
        val projectionMatrix = FloatArray(16)
        camera.getViewMatrix(viewMatrix,0)
        camera.getProjectionMatrix(projectionMatrix,0,near,far)
        Matrix.multiplyMM(VPMatrix,0,projectionMatrix,0,viewMatrix,0)
    }

}