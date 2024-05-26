package net.drkappa.app.helloarcube.scene

import android.view.MotionEvent
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import net.drkappa.app.helloarcube.camera.ARCamera
import net.drkappa.app.helloarcube.gesture.GestureManager
import net.drkappa.app.helloarcube.mesh.CameraViewer
import net.drkappa.app.helloarcube.mesh.Cube
import net.drkappa.app.helloarcube.mesh.Mesh
import net.drkappa.app.helloarcube.utils.ARGLUtils

class ARScene(val gestureManager: GestureManager,val session: Session) : Scene {

    var arCamera = ARCamera(0.1f,20.0f)

    lateinit var cameraViewer : CameraViewer
    var referenceCube : Mesh? = null

    val cubes : ArrayList<Mesh> = arrayListOf()

    var loaded = false

    override fun update() {
        if( !loaded ){
            cameraViewer = CameraViewer()
            cameraViewer.load()
            cameraViewer.textureID = ARGLUtils.createCameraTexture()

            referenceCube = Cube()
            referenceCube?.load()


            session.setCameraTextureName(cameraViewer.getTextureName())

            loaded = true

        }


        val frame = session.update()
        cameraViewer.updateBuffersFromFrame(frame)

        if (frame.getTimestamp() == 0L) {
            // Suppress rendering if the camera did not produce the first frame yet. This is to avoid
            // drawing possible leftover data from previous sessions if the texture is reused.
            return
        }

        val camera = frame.camera
        arCamera.update(camera)

        if( camera.trackingState != TrackingState.TRACKING ){
            return
        }



        if( !hasTrackedPlanes() ){
            // You might want to inform user to move around
            return
        }


        val tap: MotionEvent? = gestureManager.poll()

        tap?.let {
            for ( hit in frame.hitTest(tap)) {
                // Check if any plane was hit, and if it was hit inside the plane polygon
                val trackable = hit.getTrackable()
                if ((trackable is Plane
                            && (trackable).isPoseInPolygon(hit.getHitPose()) && calculateDistanceToPlane(
                        hit.getHitPose(),
                        camera.getPose()
                    ) > 0)) {
                    // We can create our object
                    val anchor = hit.createAnchor()
                    referenceCube?.let {
                        val newCube = it.cloneDrawData()
                        newCube.instanceData.anchor= anchor
                        cubes.add(newCube)
                    }
                }
            }
        }

    }

    override fun render() {
        cameraViewer.draw()
        for( cube in cubes ){
            cube.instanceData.update(arCamera.VPMatrix)
            cube.draw()
        }

    }

    private fun hasTrackedPlanes() : Boolean{
        for (plane in session.getAllTrackables(Plane::class.java) ) {
            if (plane.trackingState == TrackingState.TRACKING) {
                return true
            }
        }
        return false
    }

    fun calculateDistanceToPlane(planePose: Pose, cameraPose: Pose): Float {
        val normal = FloatArray(3)
        val cameraX = cameraPose.tx()
        val cameraY = cameraPose.ty()
        val cameraZ = cameraPose.tz()
        // Get transformed Y axis of plane's coordinate system.
        planePose.getTransformedAxis(1, 1.0f, normal, 0)
        // Compute dot product of plane's normal with vector from camera to plane center.
        return (cameraX - planePose.tx()) * normal[0] + (cameraY - planePose.ty()) * normal[1] + (cameraZ - planePose.tz()) * normal[2]
    }
}