package uk.co.jezuk.swoop

import android.graphics.Canvas
import uk.co.jezuk.swoop.craft.Craft
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.wave.Wave

class Player(val game: Game): Craft {
    private val ship = Ship(this)
    private val gun = Gun(this)
    private val lives_ = Lives()
    private val score_ = Score()

    override val position get() = ship.position
    override val killDist get() = ship.killDist
    val orientation get() = ship.orientation
    val velocity get() = ship.velocity
    val wave get() = currentWave!!

    var gunActive: Boolean = true
    private var currentWave: Wave? = null
    private val callbacks = ArrayList<() -> Unit>()
    private val armed get() = ship.armed && gunActive
    fun onLifeLost(callback: () -> Unit) = callbacks.add(callback)

    fun newWave(wave: Wave) {
        currentWave = wave
        ship.reset()
        callbacks.clear()
    }
    fun gunReset() = gun.reset()

    fun thrust() = ship.thrust()
    fun rotateTowards(angle: Double) = ship.rotateTowards(angle)
    fun rezOut() = ship.rezOut()
    fun hit() = ship.hit()

    val score get() = score_.score
    val lives get() = lives_.lives

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
        gun.reset()
        lives_.lifeLost()

        callbacks.forEach { it() }

        if (lives_.alive)
            return Game.NextShip.Continue

        game.gameOver()

        return Game.NextShip.End
    } // lifeLost
    fun lifeGained() = lives_.lifeGained()

    fun scored(add: Int) {
        score_.scored(add)
        game.scored(score)
    }

    fun upgrade() {
        gun.upgrade()
    }

    override fun update(frameRateScale: Float) {
        ship.update(frameRateScale)
        if(armed)
            gun.update(frameRateScale)
    }
    override fun draw(canvas: Canvas) {
        ship.draw(canvas)
    }

    fun draw(canvas: Canvas, newHighScore: Boolean) {
        score_.draw(canvas, newHighScore)
        lives_.draw(canvas)
    }

}