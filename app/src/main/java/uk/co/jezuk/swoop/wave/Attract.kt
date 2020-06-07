package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Asteroids

class Attract(
    private val game: Game
) : WaveWithTargets() {
    private var starField = StarField(game.extent)

    init {
        Asteroids(
            game,
            this,
            randInt(3) + 1,
            randInt(5) + 1,
            randInt(4) + 1,
            { game.extent.randomPoint() }
        )
    }

    /////
    override fun onSingleTapUp() =
        game.nextWave(EndAttract(game, starField, targets))
    override fun onScroll(offsetX: Float, offsetY: Float) = Unit
    override fun onLongPress() = Unit

    /////
    override fun update(fps: Long) {
        updateTargets(fps)
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

        private fun randInt(max: Int) = (Math.random() * (max+1)).toInt()
    }
} // Attract