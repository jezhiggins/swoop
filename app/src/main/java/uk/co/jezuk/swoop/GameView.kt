package uk.co.jezuk.swoop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
    private var textPen = Paint()
    private var ship = Ship()
    private var fps: Long = 0
    private var gestureDetector: GestureDetector
    private val stars = FloatArray(400)
    private val starBrush = Paint()

    init {
        holder.addCallback(this)

        textPen.setARGB(127, 255, 255, 255)
        textPen.textSize = 32f

        starBrush.setARGB(255, 255, 255, 255)

        gestureDetector = GestureDetector(this.context, this)
    } // init

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        for (i in 0 until stars.size step 2) {
            val x = Math.random() * width
            val y = Math.random() * height
            stars[i] = x.toFloat()
            stars[i+1] = y.toFloat()
        }

        startThread()
    } // surfaceCreated

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) = Unit

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        stopThread()
    } // surfaceDestroyed

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    } // onTouchEvent

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean = false
    override fun onDown(ev: MotionEvent): Boolean = true
    override fun onShowPress(ev: MotionEvent) = Unit
    override fun onSingleTapUp(ev: MotionEvent): Boolean {
        ship.thrust()
        return true
    } // onSingleTapUp
    override fun onScroll(ev1: MotionEvent, ev2: MotionEvent, offsetX: Float, offsetY: Float): Boolean {
        ship.rotateTowards(angleFromOffsets(offsetX, offsetY))
        return true
    } // onScroll
    override fun onLongPress(ev: MotionEvent) = ship.thrust()

    fun update(fps: Long) {
        this.fps = fps

        ship.update(fps, width, height)
    } // update

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        ship.draw(canvas)

        canvas.drawPoints(stars, starBrush)

        canvas.drawText("FPS: ${this.fps}", 100f, 100f, textPen)
        canvas.drawText("Accel: ${canvas.isHardwareAccelerated}", 100f, 130f, textPen)
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