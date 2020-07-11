package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Minelayer
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import uk.co.jezuk.swoop.utils.RepeatN
import uk.co.jezuk.swoop.utils.RestartableLatch
import uk.co.jezuk.swoop.wave.Wave

class Minefield(
    private val game: Game,
    private val starField: StarField,
    minelayerDelay: Int,
    private val minelayerCount: Int,
    g: Gun?
): WaveWithProjectilesAndTargets() {
    private val ship = Ship(game)
    private val gun = Gun(game, this, ship, g)
    private val launcher = RestartableLatch(minelayerDelay, ::launchMinelayer)
    private var layerCount = 0

    init {
        StonyAsteroid.field(game, this, 6)

        launcher.start()
        targets.onEliminated { endOfLevel() }
    } // init

    private fun launchMinelayer() {
        ++layerCount
        Minelayer(game, this, { if (layerCount < minelayerCount) launcher.start() })
    } // launchMinelayer

    /////
    override fun onSingleTapUp(event: MotionEvent) = ship.thrust()
    override fun onScroll(offsetX: Float, offsetY: Float) {
        ship.rotateTowards(
            angleFromOffsets(offsetX, offsetY)
        )
    } // onScroll
    override fun onLongPress() = ship.thrust()

    /////
    override fun update(frameRateScale: Float) {
        launcher.tick(frameRateScale)

        gun.update(frameRateScale)
        ship.update(frameRateScale)

        updateTargets(frameRateScale)
        updateProjectiles(frameRateScale)

        checkCollisions(ship)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)

        drawTargets(canvas)
        drawProjectiles(canvas)

        ship.draw(canvas)
    } // draw

    override fun upgrade() {
        gun.upgrade()
    } // upgrade

    /////
    private fun endOfLevel() {
        game.endOfWave(starField, ship, projectiles, gun)
    } // endOfLevel
} // Emptiness