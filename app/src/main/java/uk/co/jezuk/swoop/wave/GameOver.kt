package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game

class GameOver(
    private val game: Game,
    private val wave: Wave
) : Wave {
    private var ticker = 0
    private val pen = Paint()

    init {
        pen.setARGB(0, 255, 0, 0)
        pen.textSize = 160f
        pen.textAlign = Paint.Align.CENTER
    } // init

    private fun goToAttract() {
        game.end()
        game.attract()
    } // goToAttract

    override fun onSingleTapUp() {
        if (pen.alpha > 60) goToAttract()
    } // onSingleTapUp

    override fun update(fps: Long) {
        wave.update(fps)

        if (++ticker == 5) {
            ticker = 0
            if (pen.alpha != 255)
                pen.alpha += 1
            else
                goToAttract()
        }
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()
        wave.draw(canvas)
        canvas.restore()

        canvas.drawText("Game Over", 0f, 0f, pen)
    } // draw
} // GameOver