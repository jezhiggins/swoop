package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Asteroids
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import kotlin.math.min

class FlyAround(
    private val game: Game,
    private val starField: StarField,
    private val initialAsteroids: Int = 5
) : Wave {
    private val ship = Ship(game)
    private val asteroids = Asteroids(game, initialAsteroids)
    private val gun = Gun(game, ship, asteroids)

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

        asteroids.findCollisions(ship)
        if (asteroids.size == 0) {
            val newStarField = StarField(game.extent)
            val nextWave = FlyAround(game, newStarField, min(initialAsteroids+1, 11))

            game.nextWave(LevelTransition(
                game,
                starField,
                newStarField,
                ship,
                nextWave
            ))
        }
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        asteroids.draw(canvas)
        gun.draw(canvas)
        ship.draw(canvas)
    } // draw
} // FlyAround