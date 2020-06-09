package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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

        canvas.drawText("SWOOP", 0f, 0f, pen)
        canvas.drawText("Forest Road Game Krew",  0f, game.extent.bottom.toFloat() - 40f, smallPen)
    } // draw

    companion object {
        private val pen = Paint()
        private val smallPen = Paint()

        init {
            pen.color = Color.WHITE
            pen.alpha = 255
            pen.textSize = 128f
            pen.textAlign = Paint.Align.CENTER

            smallPen.setARGB(255, 0, 200, 0)
            smallPen.textSize = 48f
            smallPen.textSkewX = -.2f
            smallPen.textAlign = Paint.Align.CENTER
        }
    }
} // Attract