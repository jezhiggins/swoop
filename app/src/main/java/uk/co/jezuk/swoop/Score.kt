package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Score {
    private var currentScore = -1
    private var targetScore = -1
    private var tracker: HighScore.Tracker? = null

    val score get() = targetScore

    fun start(startScore: Int, hst: HighScore.Tracker) {
        currentScore = startScore
        targetScore = startScore
        tracker = hst
    }

    fun end() {
        currentScore = -1
        targetScore = -1
    }

    fun scored(add: Int) {
        targetScore += add
    }

    private fun updateScore(add: Int) {
        currentScore += add
    }// scored

    fun draw(canvas: Canvas) {
        if (score == -1) return
        if (targetScore != currentScore)
            updateScore(10)

        canvas.drawText(
            "$currentScore".padStart(6, '0'),
            -Game.extent.canvasOffsetX + 50,
            Game.extent.canvasOffsetY - 50,
            scorePen
        )
        if (tracker?.isHighScore(targetScore) != true) return

        scorePen.textSize = 32f
        canvas.drawText(
            "High Score",
            -Game.extent.canvasOffsetX + 60,
            Game.extent.canvasOffsetY - 160,
            scorePen
        )
        scorePen.textSize = 128f
    } // drawScore

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