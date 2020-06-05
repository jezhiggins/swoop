package uk.co.jezuk.swoop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.geometry.Extent
import uk.co.jezuk.swoop.wave.Attract
import uk.co.jezuk.swoop.wave.Emptiness
import uk.co.jezuk.swoop.wave.FlyAround
import uk.co.jezuk.swoop.wave.Wave

class Game(context: Context) {
    enum class NextShip { Continue, End }
    private var wave: Wave = Emptiness()
    private lateinit var ext: Extent
    val sounds = Sounds(context)
    private var lives: Int = 0
    private var score: Int = -1

    fun setExtent(width: Int, height: Int) {
        ext = Extent(width, height)
        wave = Attract(this)
    } // setExtent

    fun start() {
        lives = 3
        score = 0
    } // start

    val extent get() = ext

    fun nextWave(w: Wave) { wave = w }

    fun lifeLost(): NextShip {
        if (--lives != 0) return NextShip.Continue

        // game over goes here

        return NextShip.End
    } // lifeLost
    fun scored(add: Int) {
        score += add
    }// scored

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

        drawScore(canvas)

        canvas.restore()
    } // draw

    private fun drawScore(canvas: Canvas) {
        if (score == -1) return

        canvas.drawText(
            "${score}".padStart(6, '0'),
            -extent.canvasOffsetX + 50,
            extent.canvasOffsetY - 50,
            pen
        )
    }

    companion object {
        private val pen = Paint()

        init {
            pen.setARGB(255, 255, 255, 255)
            pen.textSize = 48f
            pen.textAlign = Paint.Align.LEFT
        }
    }
} // Game