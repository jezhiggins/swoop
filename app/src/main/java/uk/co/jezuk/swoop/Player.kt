package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.craft.Craft
import uk.co.jezuk.swoop.craft.Ship
import kotlin.math.min

class Player(val game: Game): Craft {
    private val ship = Ship(this)
    private val lives_ = Lives()
    private val score_ = Score()

    override val position get() = ship.position
    override val killDist get() = ship.killDist
    val orientation get() = ship.orientation
    val velocity get() = ship.velocity
    val armed get() = ship.armed

    private val callbacks = ArrayList<() -> Unit>()
    fun onLifeLost(callback: () -> Unit) = callbacks.add(callback)

    fun thrust() = ship.thrust()
    fun rotateTowards(angle: Double) = ship.rotateTowards(angle)
    fun rezOut() = ship.rezOut()
    fun hit() = ship.hit()

    val score get() = score_.score
    val lives get() = lives_.lives
    val alive get() = lives_.alive

    fun start(startLives: Int, startScore: Int) {
        lives_.start(startLives)
        score_.start(startScore)
    }

    fun end() {
        lives_.end()
        score_.end()
    }

    fun lifeLost(): Game.NextShip {
        ship.reset()
        lives_.lifeLost()

        callbacks.forEach { it() }

        if (lives_.alive)
            return Game.NextShip.Continue

        game.gameOver()

        return Game.NextShip.End
    } // lifeLosts
    fun lifeGained() = lives_.lifeGained()

    fun scored(add: Int) = score_.scored(add)

    override fun update(frameRateScale: Float) {
        ship.update(frameRateScale)
    }
    override fun draw(canvas: Canvas) {
        ship.draw(canvas)
    }

    fun draw(canvas: Canvas, newHighScore: Boolean) {
        score_.draw(canvas, newHighScore)
        lives_.draw(canvas)
    }

}

class Lives {
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
        canvas.translate(Game.extent.canvasOffsetX - 50, Game.extent.canvasOffsetY - 90)
        canvas.rotate(-90f)
        canvas.scale(0.75f, 0.75f)
        for (l in 0 until currentLives) {
            canvas.drawLines(Ship.shape, Ship.shipBrush)
            canvas.translate(0f, -105f)
        } // for
    } // drawLives
}

class Score {
    private var currentScore = -1
    private var targetScore = -1

    val score get() = targetScore

    fun start(startScore: Int) {
        currentScore = startScore
        targetScore = startScore
    }

    fun end() {
        currentScore = -1
        targetScore = score;
    }

    fun scored(add: Int) {
        targetScore += add
    }

    private fun updateScore(add: Int) {
        currentScore += add
    }// scored

    fun draw(canvas: Canvas, newHighScore: Boolean) {
        if (score == -1) return
        if (targetScore != currentScore)
            updateScore(10);

        canvas.drawText(
            "${score}".padStart(6, '0'),
            -Game.extent.canvasOffsetX + 50,
            Game.extent.canvasOffsetY - 50,
            scorePen
        )
        if (!newHighScore) return

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