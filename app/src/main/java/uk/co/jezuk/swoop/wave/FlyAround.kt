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
) : Wave {
    private val ship = Ship(game)
    private val asteroids = Asteroids(game, initialAsteroids)
    private val gun = Gun(game, ship, asteroids)
    private val cometCountdown = Latch(100, { Comet(game, this)})
    private val targets = mutableListOf<Target>()

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
        asteroids.update(fps)
        gun.update(fps)
        ship.update(fps)

        updateTargets(fps)

        cometCountdown.tick()

        asteroids.findCollisions(ship)
        // comet?.checkShipCollision(ship)

        if (asteroids.size == 0)
            endOfLevel()
    } // update

    private fun updateTargets(fps: Long) {
        val working = ArrayList(targets)
        working.forEach {
            t -> t.update(fps)
        }
    }

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        asteroids.draw(canvas)
        targets.forEach {
            t -> t.draw(canvas)
        }
        gun.draw(canvas)
        ship.draw(canvas)
    } // draw

    /////
    override fun addTarget(target: Target) {
        targets.add(target)
    }
    override fun removeTarget(target: Target) {
        targets.remove(target)
    }

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