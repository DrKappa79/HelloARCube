package net.drkappa.app.stepthree

import android.content.pm.PackageManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableException
import net.drkappa.app.stepthree.gesture.GestureManager
import net.drkappa.app.stepthree.scene.StepThreeScene

class StepThreeActivity : AppCompatActivity() {

    lateinit var glSurfaceView : GLSurfaceView
    lateinit var glRenderer : StepThreeGLRenderer
    lateinit var gestureManager : GestureManager
    var arSession : Session? = null

    var permissionGranted = false

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                permissionGranted = true
            } else {
                finish()
            }
        }

    fun requestARPermissions(){
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                permissionGranted = true
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.CAMERA) -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA)
            }
            else -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA)
            }
        }
    }

    fun isARCoreSupportedAndUpToDate(): Boolean {
        return when (ArCoreApk.getInstance().checkAvailability(this)) {
            ArCoreApk.Availability.SUPPORTED_INSTALLED -> true
            ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD, ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED -> {
                try {
                    when (ArCoreApk.getInstance().requestInstall(this, true)) {
                        ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                            false
                        }
                        ArCoreApk.InstallStatus.INSTALLED -> true
                    }
                } catch (e: UnavailableException) {
                    false
                }
            }
            else ->{
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glRenderer = StepThreeGLRenderer()
        glSurfaceView = StepThreeGLSurfaceView(glRenderer,this)
        gestureManager = GestureManager(applicationContext)
        setContentView(glSurfaceView)
        requestARPermissions()
    }

    override fun onResume() {
        super.onResume()
        arSession?.resume() ?: run{
            if( permissionGranted ) {
                if( isARCoreSupportedAndUpToDate() ){
                    createARSession()
                }
            }
        }
        glSurfaceView.onResume()
    }

    override fun onPause() {
        // Order is important, first pause the GLSurfaceView so that requests will be stopped
        glSurfaceView.onPause()
        arSession?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        arSession?.close()
        super.onDestroy()
    }

    fun createARSession() {
        arSession = Session(this)

        val config = Config(arSession)
        config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL

        arSession?.let {
            try {
                it.configure(config)
                it.resume()
                val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
                val defaultDisplay = windowManager.defaultDisplay
                it.setDisplayGeometry(
                    defaultDisplay.rotation,
                    glRenderer.screenWidth,
                    glRenderer.screenHeight
                )
                glRenderer.arScene = StepThreeScene(gestureManager = gestureManager, session = it)
            }catch (_ : Exception){

            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureManager.onTouch(null, p1 = event)
    }
}