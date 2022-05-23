package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.Extent
import kotlin.math.min

class Player {
    private var currentLives = 0
    private var currentScore = -1
    private var targetScore = -1

    val score get() = targetScore
    val lives get() = currentLives
    val alive get() = currentLives > 0

    fun start(startLives: Int, startScore: Int) {
        currentLives = startLives
        currentScore = startScore
        targetScore = startScore
    }

    fun end() {
        currentLives = 0
        currentScore = -1
        targetScore = score;
    }

    fun lifeLost() = --currentLives
    fun lifeGained() {
        currentLives = min(currentLives + 1, 9)
    }

    fun scored(add: Int) {
        targetScore += add
    }

    private fun updateScore(add: Int) {
        currentScore += add
    }// scored

    fun draw(canvas: Canvas, extent: Extent, newHighScore: Boolean) {
        drawScore(canvas, extent, newHighScore)
        drawLives(canvas, extent)
    }

    private fun drawScore(canvas: Canvas, extent: Extent, newHighScore: Boolean) {
        if (score == -1) return
        if (targetScore != currentScore)
            updateScore(10);

        canvas.drawText(
            "${score}".padStart(6, '0'),
            -extent.canvasOffsetX + 50,
            extent.canvasOffsetY - 50,
            scorePen
        )
        if (!newHighScore) return

        scorePen.textSize = 32f
        canvas.drawText(
            "High Score",
            -extent.canvasOffsetX + 60,
            extent.canvasOffsetY - 160,
            scorePen
        )
        scorePen.textSize = 128f
    } // drawScore

    private fun drawLives(canvas: Canvas, extent: Extent) {
        canvas.translate(extent.canvasOffsetX - 50, extent.canvasOffsetY - 90)
        canvas.rotate(-90f)
        canvas.scale(0.75f, 0.75f)
        for (l in 0 until currentLives) {
            canvas.drawLines(Ship.shape, Ship.shipBrush)
            canvas.translate(0f, -105f)
        } // for
    } // drawLives

    companion object {
        private val scorePen = Paint()

        init {
            scorePen.color = Color.CYAN
            scorePen.alpha = 255
            scorePen.textSize = 128f
            scorePen.textAlign = Paint.Align.LEFT
        }
    }
}