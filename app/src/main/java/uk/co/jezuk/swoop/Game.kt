package uk.co.jezuk.swoop

import android.content.Context
import android.graphics.Canvas
import uk.co.jezuk.swoop.geometry.Extent
import uk.co.jezuk.swoop.wave.Attract
import uk.co.jezuk.swoop.wave.Emptiness
import uk.co.jezuk.swoop.wave.FlyAround
import uk.co.jezuk.swoop.wave.Wave

class Game(context: Context) {
    private var wave: Wave = Emptiness()
    private lateinit var ext: Extent
    val sounds = Sounds(context)

    fun start(width: Int, height: Int) {
        ext = Extent(width, height)
        wave = Attract(this)
    } // start

    val extent get() = ext

    fun nextWave(w: Wave) { wave = w }

    /////
    fun onSingleTapUp() = wave.onSingleTapUp()
    fun onScroll(offsetX: Float, offsetY: Float) = wave.onScroll(offsetX, offsetY)
    fun onLongPress() = wave.onLongPress()

    /////
    fun update(fps: Long) = wave.update(fps)
    fun draw(canvas: Canvas) {
        canvas.save()
        canvas.translate(extent.canvasOffsetX, extent.canvasOffsetY)

        wave.draw(canvas)
        canvas.restore()
    }
} // Game