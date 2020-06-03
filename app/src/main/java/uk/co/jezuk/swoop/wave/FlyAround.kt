package uk.co.jezuk.swoop.wave

import android.content.Context
import android.graphics.Canvas
import uk.co.jezuk.swoop.Sounds
import uk.co.jezuk.swoop.craft.Asteroids
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.angleFromOffsets

class FlyAround(
    sounds: Sounds,
    private val width: Int,
    private val height: Int
) : Wave {
    private var ship = Ship(sounds)
    private var starField = StarField(width, height)
    private var asteroids = Asteroids()

    init {
        asteroids.wave(5, width, height)
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
        asteroids.update(fps, width, height)
        ship.update(fps, width, height)

        asteroids.findCollisions(ship)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        asteroids.draw(canvas)
        ship.draw(canvas)
    } // draw
} // FlyAround