package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Asteroids

class Attract(
    private val game: Game
) : Wave {
    private var starField = StarField(game.width, game.height)
    private var asteroids = Asteroids(
        game,
        randInt(3)+1,
        randInt(5)+1,
        randInt(4)+1
    )

    /////
    override fun onSingleTapUp() =
        game.nextWave(EndAttract(game, starField, asteroids))
    override fun onScroll(offsetX: Float, offsetY: Float) = Unit
    override fun onLongPress() = Unit

    /////
    override fun update(fps: Long) {
        asteroids.update(fps, game.width, game.height)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        asteroids.draw(canvas)

        canvas.drawText("SWOOP", game.width/2f, game.height/2f, pen)

        canvas.drawText("Forest Road Game Krew", game.width/2f, game.height - 40f, smallPen)
    } // draw

    companion object {
        private val pen = Paint()
        private val smallPen = Paint()

        init {
            pen.setARGB(255, 255, 255, 255)
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