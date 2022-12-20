package uk.co.jezuk.swoop

import android.graphics.Canvas
import uk.co.jezuk.swoop.craft.Craft
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.wave.Wave

class Player(
    private val onDied: () -> Unit,
    mode: Mode
): Craft {
    private val ship = Ship(
        this,
        mode.shipShape,
        mode.shipColor,
        mode.rezPoint,
        mode.initialRotation
    )
    private val gun = Gun(this, mode.bulletColor)
    private val lives_ = Lives(mode.shipShape, mode.shipColor, mode.livesDisplayPosition)
    private val score_ = Score(mode.scoreDisplayPosition)

    val touchArea = mode.touchArea
    override val position get() = ship.position
    override val killDist get() = ship.killDist
    val orientation get() = ship.orientation
    val velocity get() = ship.velocity
    val wave get() = currentWave!!

    private var gunActive: Boolean = true
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
    val alive get() = lives_.alive

    fun start(startLives: Int, startScore: Int, tracker: HighScore.Tracker) {
        lives_.start(startLives)
        score_.start(startScore, tracker)
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

        onDied()

        return Game.NextShip.End
    } // lifeLost
    fun lifeGained() = lives_.lifeGained()

    fun scored(add: Int) {
        score_.scored(add)
    }

    fun gunOff() { gunActive = false }
    fun gunOn() { gunActive = true }
    fun upgrade() = gun.upgrade()

    override fun update(frameRateScale: Float) {
        ship.update(frameRateScale)
        if(armed)
            gun.update(frameRateScale)
    }
    override fun draw(canvas: Canvas) {
        canvas.save()

        ship.draw(canvas)

        score_.draw(canvas)
        lives_.draw(canvas)

        canvas.restore()
    }

}