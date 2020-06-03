package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Asteroids
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.angleFromOffsets

class FlyAround(
    game: Game,
    private val starField: StarField
) : Wave {
    private var ship = Ship(game)
    private var asteroids = Asteroids(game, 5)

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

        asteroids.findCollisions(ship)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        asteroids.draw(canvas)
        ship.draw(canvas)
    } // draw
} // FlyAround