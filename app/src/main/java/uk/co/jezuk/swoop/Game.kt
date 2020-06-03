package uk.co.jezuk.swoop

import android.content.Context
import android.graphics.Canvas
import uk.co.jezuk.swoop.wave.Attract
import uk.co.jezuk.swoop.wave.Emptiness
import uk.co.jezuk.swoop.wave.FlyAround
import uk.co.jezuk.swoop.wave.Wave

class Game(context: Context) {
    private var wave: Wave = Emptiness()
    private var w: Int = 0
    private var h: Int = 0
    val sounds = Sounds(context)

    fun start(width: Int, height: Int) {
        w = width
        h = height
        wave = Attract(this)
    } // start

    val width: Int get() = w
    val height: Int get() = h

    fun nextWave(w: Wave) { wave = w }

    /////
    fun onSingleTapUp() = wave.onSingleTapUp()
    fun onScroll(offsetX: Float, offsetY: Float) = wave.onScroll(offsetX, offsetY)
    fun onLongPress() = wave.onLongPress()

    /////
    fun update(fps: Long) = wave.update(fps)
    fun draw(canvas: Canvas) = wave.draw(canvas)
} // Game