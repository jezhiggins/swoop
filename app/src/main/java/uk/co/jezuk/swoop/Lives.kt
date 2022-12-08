package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.craft.Ship
import kotlin.math.min

class Lives(
    private val shipShape: FloatArray,
    private val shipBrush: Paint,
    private val positionDisplay: (Canvas) -> Unit
) {
    private var currentLives = 0

    val lives get() = currentLives
    val alive get() = currentLives > 0

    fun start(startLives: Int) {
        currentLives = startLives
    }

    fun end() {
        currentLives = 0
    }

    fun lifeLost() = --currentLives
    fun lifeGained() {
        currentLives = min(currentLives + 1, 9)
    }

    fun draw(canvas: Canvas) {
        canvas.save()
        positionDisplay(canvas)
        for (l in 0 until currentLives) {
            canvas.drawLines(shipShape, shipBrush)
            canvas.translate(0f, -105f)
        } // for
        canvas.restore()
    } // drawLives

    companion object {
        fun SinglePlayer(canvas: Canvas) {
            canvas.translate(Game.extent.canvasOffsetX - 50, Game.extent.canvasOffsetY - 90)
            canvas.rotate(-90f)
            canvas.scale(0.75f, 0.75f)
        }

        fun PlayerOne(canvas: Canvas) {
            canvas.translate(Game.extent.canvasOffsetX - 50, -Game.extent.canvasOffsetY + 90)
            canvas.rotate(180f)
            canvas.scale(0.5f, 0.5f)
        }

        fun PlayerTwo(canvas: Canvas) {
            canvas.translate(-Game.extent.canvasOffsetX + 50, Game.extent.canvasOffsetY - 90)
            canvas.scale(0.5f, 0.5f)
        }
    }
}
