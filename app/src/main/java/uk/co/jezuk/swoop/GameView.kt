package uk.co.jezuk.swoop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(
    context: Context,
    attributes: AttributeSet
) : SurfaceView(context, attributes),
    SurfaceHolder.Callback
{
    private var thread: GameThread? = null
    private val game = Game(context)

    private data class Touch(
        val x: Float,
        val y: Float,
        var hasMoved: Boolean = false,
        var ex: Float = x,
        var ey: Float = y
    ) {
        fun moved() { hasMoved = true }
    }
    private val touches = SparseArray<Touch>()

    init {
        holder.addCallback(this)
    } // init

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        game.setExtent(width, height)

        startThread()
    } // surfaceCreated

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        game.setExtent(width, height)
    } // surfaceChanged

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        stopThread()
    } // surfaceDestroyed

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)

        val touch = scaleTouch(event, pointerIndex)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                touches.put(pointerId, touch)
            }
            MotionEvent.ACTION_MOVE -> {
                for (i in 0..(event.pointerCount-1)) {
                    val start = touches.get(event.getPointerId(i))
                    if (start != null) {
                        start.moved()
                        start.ex = touch.x
                        start.ey = touch.y
                        onMove(start, touch)
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                val start = touches.get(pointerId)
                if (!start.hasMoved)
                    onTap(start)
                touches.remove(pointerId)
            }
        }

        return true
    } // onTouchEvent

    private fun scaleTouch(ev: MotionEvent, actionIndex: Int): Touch {
        ev.transform(game.touchMatrix)
        val x = ev.getX(actionIndex)
        val y = ev.getY(actionIndex)
        return Touch(x, y)
    }

    private fun onTap(start: Touch) {
        game.onTap(start.x, start.y)
    } // onTap
    private fun onMove(start: Touch, to: Touch) {
        game.onMove(start.x, start.y, start.x-to.x, start.y-to.y)
    } // onMove

    fun update(frameRateScale: Float) {
        game.update(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        game.draw(canvas)

        canvas.save()
        canvas.setMatrix(game.scaleMatrix)
        canvas.clipRect(Game.extent.bounds)

        val paint = Paint()
        paint.color = Color.YELLOW
        paint.style = Paint.Style.FILL_AND_STROKE
        for (i in 0..(touches.size()-1)) {
            val touch = touches.valueAt(i)
            canvas.drawCircle(touch.x, touch.y, 100f, paint)
            if (touch.hasMoved)
                canvas.drawLine(touch.x, touch.y, touch.ex, touch.ey, paint)
        }

        canvas.restore()
        canvas.drawText("Total Pointers " + touches.size(), 10f, 40f, paint)
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