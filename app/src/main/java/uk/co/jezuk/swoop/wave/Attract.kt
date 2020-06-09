package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Asteroid
import uk.co.jezuk.swoop.craft.Comet
import uk.co.jezuk.swoop.utils.Latch
import uk.co.jezuk.swoop.utils.Repeat
import kotlin.random.Random

class Attract(
    private val game: Game
) : WaveWithTargets() {
    private var starField = StarField(game.extent)
    val cometGun = Repeat(750, { Comet(game, this) })

    init {
        Asteroid.field(
            game,
            this,
            Random.nextInt(2, 5),
            Random.nextInt(2, 7),
            Random.nextInt(2,6),
            { game.extent.randomPoint() }
        )
    }

    /////
    override fun onSingleTapUp() =
        game.nextWave(EndAttract(game, starField, targets))
    override fun onScroll(offsetX: Float, offsetY: Float) = Unit
    override fun onLongPress() = Unit

    /////
    override fun update(frameRateScale: Float) {
        updateTargets(frameRateScale)
        cometGun.tick(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        drawTargets(canvas)

        drawText("SWOOP", canvas,0.0, 0.0, pen)

        val margin = 40.0
        val almostLeft = game.extent.left + margin
        val almostRight = game.extent.right - margin
        val justOffBottom = game.extent.bottom - margin
        drawText("Forest Road Game Krew", canvas, 0.0, justOffBottom, smallPen)
        tinyPen.textAlign = Paint.Align.LEFT
        drawText("Alright Bab!", canvas, almostLeft, justOffBottom, tinyPen)
        tinyPen.textAlign = Paint.Align.RIGHT
        drawText("Made in Birmingham", canvas, almostRight, justOffBottom, tinyPen)
    } // draw

    private fun drawText(text: String, canvas: Canvas, x: Double, y: Double, pen: Paint) {
        canvas.drawText(text, x.toFloat(), y.toFloat(), pen)
    } // drawText

    companion object {
        private val pen = TextPaint()
        private val smallPen = Paint()
        private val tinyPen = Paint()

        init {
            pen.color = Color.WHITE
            pen.alpha = 255
            pen.textSize = 256f
            pen.textAlign = Paint.Align.CENTER

            smallPen.setARGB(255, 0, 200, 0)
            smallPen.textSize = 48f
            smallPen.textSkewX = -.2f
            smallPen.textAlign = Paint.Align.CENTER

            tinyPen.setARGB(255, 0, 200, 0)
            tinyPen.textSize = 24f
            tinyPen.textSkewX = -.2f
            tinyPen.textAlign = Paint.Align.RIGHT
        }
    }
} // Attract