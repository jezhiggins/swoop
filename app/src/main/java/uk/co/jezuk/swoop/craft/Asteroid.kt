package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector

class Asteroid(
    private val all: Asteroids,
    pos: Point,
    private var scale: Float = 4f
) {
    private val position = pos.copy()
    private var velocity = AsteroidVector(scale)
    private val killRadius = 25f

    private val killDist: Float get() = scale * killRadius

    fun update(fps: Long, width: Int, height: Int) {
        position.move(velocity, width, height)
    } // update

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(
            position.x.toFloat(),
            position.y.toFloat()
        )
        canvas.translate(canvas.width/2f, canvas.height/2f)
        canvas.scale(scale, scale)

        canvas.drawCircle(0f, 0f, killRadius,
            brush
        )

        canvas.restore()
    } // draw

    fun split() {
        if (scale != 1f) {
            scale /= 2
            velocity = AsteroidVector(scale)
            all.add(Asteroid(all, position, scale))
        } else {
            all.remove(this)
        }
    } // split

    fun checkShipCollision(ship: Ship) {
        if (ship.position.distance(position) < (killDist + ship.killDist)) {
            split()
            ship.explode()
        }
    } // checkShipCollision

    companion object {
        fun AsteroidVector(scale: Float) =
            Vector(6.0 - scale, Math.random() * 360)

        val brush =
            makeAsteroidBrush()

        private fun makeAsteroidBrush(): Paint {
            val brush = Paint()
            brush.setARGB(127, 255, 255, 255)
            brush.strokeWidth = 3f
            brush.strokeJoin = Paint.Join.BEVEL
            brush.style = Paint.Style.STROKE
            return brush
        }
    } // companion object
} // Asteroid