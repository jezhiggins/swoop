package uk.co.jezuk.swoop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.Target
import uk.co.jezuk.swoop.geometry.Extent
import uk.co.jezuk.swoop.wave.*

class Game(context: Context) {
    enum class NextShip { Continue, End }
    private var wave: Wave = Emptiness()
    private lateinit var ext: Extent
    val sounds = Sounds(context)
    private var lives: Int = 0
    private var score: Int = -1

    fun setExtent(width: Int, height: Int) {
        ext = Extent(width, height)
        attract()
    } // setExtent

    fun attract() {
        wave = Attract(this)
    } // attract

    fun start() {
        lives = 3
        score = 0
    } // start

    fun end() {
        lives = 0
        score = -1
    } // end

    val extent get() = ext

    fun nextWave(w: Wave) { wave = w }

    fun lifeLost(): NextShip {
        if (--lives != 0) return NextShip.Continue

        nextWave(GameOver(this, wave))

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
    fun update(frameRateScale: Float) = wave.update(frameRateScale)
    fun draw(canvas: Canvas) {
        canvas.save()
        canvas.translate(extent.canvasOffsetX, extent.canvasOffsetY)

        wave.draw(canvas)

        drawScore(canvas)
        drawLives(canvas)

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
    } // drawScore

    private fun drawLives(canvas: Canvas) {
        canvas.translate(extent.canvasOffsetX - 50, extent.canvasOffsetY - 90)
        canvas.rotate(-90f)
        canvas.scale(0.75f, 0.75f)
        for (l in 0 until lives) {
            canvas.drawLines(Ship.shape, Ship.shipBrush)
            canvas.translate(0f, -105f)
        } // for
    } // drawLives

    companion object {
        private val pen = Paint()

        init {
            pen.color = Color.CYAN
            pen.alpha = 255
            pen.textSize = 128f
            pen.textAlign = Paint.Align.LEFT
        }
    }
} // Game