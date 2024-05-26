package net.drkappa.app.helloarcube.gesture

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class GestureManager(context : Context) : View.OnTouchListener{

    private var gestureDetector: GestureDetector? = null
    private val queuedTaps: BlockingQueue<MotionEvent> = ArrayBlockingQueue(16)

    init{
        gestureDetector = GestureDetector(
            context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    queuedTaps.offer(e)
                    return true
                }

                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }
            })
    }

    fun poll(): MotionEvent? {
        return queuedTaps.poll()
    }

    override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
        return gestureDetector?.onTouchEvent(p1) ?: false
    }
}