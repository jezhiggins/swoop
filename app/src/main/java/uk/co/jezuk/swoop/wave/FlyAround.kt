package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Asteroids
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.angleFromOffsets

class FlyAround(
    private val game: Game,
    private val starField: StarField,
    private val initialAsteroids: Int = 5
) : Wave {
    private var ship = Ship(game)
    private var asteroids = Asteroids(game, initialAsteroids)
    private var gun = Gun(game, ship, asteroids)

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
        ship.update(fps)
        gun.update(fps)

        asteroids.findCollisions(ship)
        if (asteroids.size == 0)
            game.nextWave(LevelTransition(game, starField, ship, initialAsteroids))
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        asteroids.draw(canvas)
        ship.draw(canvas)
        gun.draw(canvas)
    } // draw
} // FlyAround