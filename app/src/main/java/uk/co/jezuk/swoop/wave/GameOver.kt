package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Nothing
import uk.co.jezuk.swoop.utils.Repeat

class GameOver(
    private val game: Game,
    private val wave: Wave
) : Wave {
    private var brightener = Repeat(5, { brighten() })

    init {
        pen.alpha = 0
        wave.addTarget(Nothing())
    } // init

    private fun goToAttract() {
        game.end()
        game.attract()
    } // goToAttract

    override fun onSingleTapUp(event: MotionEvent) {
        if (pen.alpha > 60) goToAttract()
    } // onSingleTapUp

    override fun update(frameRateScale: Float) {
        wave.update(frameRateScale)
        brightener.tick(frameRateScale)
    } // update

    private fun brighten() {
        if (pen.alpha != 255)
            pen.alpha += 1
        else
            goToAttract()
    } // brighten

    override fun draw(canvas: Canvas) {
        canvas.save()
        wave.draw(canvas)
        canvas.restore()

        canvas.drawText("Game Over", 0f, 0f, pen)
    } // draw

    companion object {
        private val pen = Paint()

        init {
            pen.color = Color.RED
            pen.textSize = 160f
            pen.textAlign = Paint.Align.CENTER
        } // init
    } // companion object
} // GameOver