package uk.co.jezuk.swoop

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
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
        var offsetX: Float = 0f,
        var offsetY: Float = 0f,
        var hasMoved: Boolean = false
    ) {
        val stationary get() = !hasMoved
        fun move(delta: Touch) {
            offsetX = x - delta.x
            offsetY = y - delta.y
            hasMoved = (Math.abs(offsetX) + Math.abs(offsetY)) > 2
        }
    }
    private val touches = mutableMapOf<Int, Touch>()

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
        val action = event.actionMasked

        event.transform(game.touchMatrix)

        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                touches[pointerId] = toTouch(event, pointerIndex)
            }
            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val start = touches[event.getPointerId(i)]
                    start?.move(toTouch(event, i))
                    if (start?.stationary == false)
                        onMove(start)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                val start = touches[pointerId]
                if (start?.stationary == true)
                    onTap(start)
                touches.remove(pointerId)
            }
        }

        return true
    } // onTouchEvent

    private fun toTouch(ev: MotionEvent, actionIndex: Int): Touch {
        val x = ev.getX(actionIndex)
        val y = ev.getY(actionIndex)
        return Touch(x, y)
    }

    private fun onTap(start: Touch) {
        game.onTap(start.x, start.y)
    } // onTap
    private fun onMove(start: Touch) {
        game.onMove(start.x, start.y, start.offsetX, start.offsetY)
    } // onMove

    fun update(frameRateScale: Float) {
        game.update(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        game.draw(canvas)
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