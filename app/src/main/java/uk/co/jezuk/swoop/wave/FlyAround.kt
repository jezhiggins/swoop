package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Asteroids
import uk.co.jezuk.swoop.craft.Comet
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.Target
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import uk.co.jezuk.swoop.utils.Latch
import kotlin.math.min

class FlyAround(
    private val game: Game,
    private val starField: StarField,
    private val initialAsteroids: Int = 5
) : WaveWithProjectilesAndTargets() {
    private val ship = Ship(game)
    private val gun = Gun(game, this, ship)
    private val cometCountdown = Latch(100, { Comet(game, this)})

    init {
        Asteroids(game, this, initialAsteroids)
    }

    /////
    override fun onSingleTapUp() = ship.thrust()
    override fun onScroll(offsetX: Float, offsetY: Float) {
        ship.rotateTowards(
            angleFromOffsets(offsetX, offsetY)
        )
    } // onScroll
    override fun onLongPress() = ship.thrust()

    /////
    override fun update(fps: Long) {
        gun.update(fps)
        ship.update(fps)

        updateTargets(fps)
        updateProjectiles(fps)

        checkCollisions(ship)

        cometCountdown.tick()

        if (targets.size == 0)
            endOfLevel()
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)

        drawTargets(canvas)
        drawProjectiles(canvas)

        ship.draw(canvas)
    } // draw

    /////
    private fun endOfLevel() {
        val newStarField = StarField(game.extent)
        val nextWave = FlyAround(game, newStarField, min(initialAsteroids+1, 11))

        game.nextWave(LevelTransition(
            game,
            starField,
            newStarField,
            ship,
            nextWave
        ))
    } // endOfLevel
} // FlyAround