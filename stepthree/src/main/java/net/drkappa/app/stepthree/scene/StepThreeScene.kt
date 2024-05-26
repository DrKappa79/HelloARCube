package net.drkappa.app.stepthree.scene

import com.google.ar.core.Session
import net.drkappa.app.stepthree.gesture.GestureManager
import net.drkappa.app.stepthree.mesh.CameraViewer
import net.drkappa.app.stepthree.mesh.FullScreenQuad
import net.drkappa.app.stepthree.mesh.Mesh
import net.drkappa.app.stepthree.utils.GLUtils

class StepThreeScene(val gestureManager: GestureManager,val session : Session) : Scene{

    lateinit var cameraViewer : CameraViewer

    var loaded = false

    override fun load() {
        if( !loaded ) {
            cameraViewer = CameraViewer()
            cameraViewer.load()
            cameraViewer.textureID = GLUtils.createCameraTexture()
            session.setCameraTextureName(cameraViewer.getTextureName())
            loaded = true
        }
    }

    override fun render() {
        val frame = session.update()
        cameraViewer.updateBuffersFromFrame(frame)

        if (frame.getTimestamp() == 0L) {
            // Suppress rendering if the camera did not produce the first frame yet. This is to avoid
            // drawing possible leftover data from previous sessions if the texture is reused.
            return
        }

        cameraViewer.draw()
    }

}