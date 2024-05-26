package net.drkappa.app.helloarcube.parameters

import android.opengl.Matrix
import com.google.ar.core.Anchor

class InstanceData() {

    var ModelMatrix = FloatArray(16)
    var MVPMatrix = FloatArray(16)

    var anchor : Anchor? = null

    init {
        Matrix.setIdentityM(ModelMatrix,0)
        Matrix.setIdentityM(MVPMatrix,0)
    }

    fun update(VPMatrix : FloatArray){
        anchor?.let {
            it.pose.toMatrix(ModelMatrix,0)
            Matrix.multiplyMM(MVPMatrix,0,VPMatrix,0,ModelMatrix,0)
        }
    }

}