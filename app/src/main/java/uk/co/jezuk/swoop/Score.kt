package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Score(
    private val positionDisplay: (Canvas) -> Unit
) {
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
        canvas.save()
        doDraw(canvas)
        canvas.restore()
    }
    private fun doDraw(canvas: Canvas) {
        if (score == -1) return
        if (targetScore != currentScore)
            updateScore(10)

        positionDisplay(canvas)

        canvas.drawText(
            "$currentScore".padStart(6, '0'),
            0f,
            0f,
            scorePen
        )
        if (tracker?.isHighScore(targetScore) != true) return

        scorePen.textSize = 32f
        canvas.drawText(
            "High Score",
            10f,
            -110f,
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

        fun SinglePlayer(canvas: Canvas) {
            canvas.translate(-Game.extent.canvasOffsetX + 50, Game.extent.canvasOffsetY - 50)
        }

        fun PlayerOne(canvas: Canvas) {
            canvas.translate(Game.extent.canvasOffsetX - 100, -Game.extent.canvasOffsetY + 270)
            canvas.rotate(270f)
            canvas.scale(0.5f, 0.5f)
        }

        fun PlayerTwo(canvas: Canvas) {
            canvas.translate(-Game.extent.canvasOffsetX + 100, Game.extent.canvasOffsetY - 270)
            canvas.rotate(90f)
            canvas.scale(0.5f, 0.5f)
        }
    }
}