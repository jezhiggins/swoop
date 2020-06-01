package uk.co.jezuk.swoop

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(
    context: Context,
    attributes: AttributeSet
) : SurfaceView(context, attributes),
    SurfaceHolder.Callback,
    GestureDetector.OnGestureListener
{
    private var thread: GameThread? = null
    private var ship = Ship()
    private var fps: Long = 0
    private lateinit var gestureDetector: GestureDetector

    init {
        // add callback
        holder.addCallback(this)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        startThread()
        gestureDetector = GestureDetector(this.context, this)
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        stopThread()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    } // onTouchEvent

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        ship.thrust()
        return true
    }

    override fun onDown(ev: MotionEvent?): Boolean {
        return true
    }
    override fun onShowPress(ev: MotionEvent?) = Unit
    override fun onSingleTapUp(ev: MotionEvent?): Boolean = false
    override fun onScroll(ev1: MotionEvent, ev2: MotionEvent, offsetX: Float, offsetY: Float): Boolean {
        ship.rotateTowards(angleFromOffsets(offsetX, offsetY))
        return true
    }
    override fun onLongPress(ev: MotionEvent?) = Unit


    /**
     * Function to update the positions of sprites
     */
    fun update(fps: Long) {
        this.fps = fps
    } // update

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        ship.update(fps, canvas.width, canvas.height)

        ship.draw(canvas)
    } // draw

    fun pause() {
        stopThread()
    } // pause

    fun resume() {
        startThread()
    } // resume

    private fun startThread() {
        if (thread != null) return
        thread = GameThread(holder, this)
        thread?.running()
        thread?.start()
    } // startThread

    private fun stopThread() {
        if (thread == null) return
        try {
            thread?.stopped()
            thread?.join()
            thread = null
        } catch (e: InterruptedException) {
        }
    } // stopThread
} // GameView