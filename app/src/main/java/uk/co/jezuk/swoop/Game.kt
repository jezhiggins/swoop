package uk.co.jezuk.swoop

import android.content.Context
import android.graphics.Canvas
import uk.co.jezuk.swoop.wave.Emptiness
import uk.co.jezuk.swoop.wave.FlyAround
import uk.co.jezuk.swoop.wave.Wave

class Game(private val context: Context) {
    private var wave: Wave = Emptiness()

    fun start(width: Int, height: Int) {
        wave = FlyAround(context, width, height)
    } // start

    /////
    fun onSingleTapUp() = wave.onSingleTapUp()
    fun onScroll(offsetX: Float, offsetY: Float) = wave.onScroll(offsetX, offsetY)
    fun onLongPress() = wave.onLongPress()

    /////
    fun update(fps: Long) = wave.update(fps)
    fun draw(canvas: Canvas) = wave.draw(canvas)
} // Game