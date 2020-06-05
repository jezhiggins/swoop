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
    val position = pos.copy()
    private var velocity = AsteroidVector(scale)
    private var orientation = (Math.random() * 360).toFloat()
    private val rotation = (Math.random() * 3).toFloat() - 2f
    private val killRadius = 25f

    val killDist get() = scale * killRadius
    val size get() = scale.toInt()

    fun update(fps: Long) {
        position.move(velocity, all.extent)
        orientation += rotation
        if (orientation < 0) orientation += 360
        if (orientation > 360) orientation -= 360
    } // update

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(
            position.x.toFloat(),
            position.y.toFloat()
        )
        canvas.scale(scale, scale)

        // canvas.drawCircle(0f, 0f, killRadius, brush)
        canvas.rotate(orientation)
        canvas.drawLines(shape, brush)

        canvas.restore()
    } // draw

    private fun explode() {
        if (scale != 1f) {
            scale /= 2
            velocity = AsteroidVector(scale)
            all.add(Asteroid(all, position, scale))
        } else {
            all.remove(this)
        }
        all.bang(this)
    } // split

    fun shot() {
        all.scored(400/scale.toInt())
        explode()
    } // shot

    fun checkShipCollision(ship: Ship) {
        if (ship.position.distance(position) < (killDist + ship.killDist)) {
            explode()
            ship.hit()
        }
    } // checkShipCollision

    companion object {
        val Big: Float = 4f
        val Medium: Float = 2f
        val Small: Float = 1f

        fun AsteroidVector(scale: Float) =
            Vector(6.0 - scale, Math.random() * 360)

        val brush = Paint()
        val shape = floatArrayOf(
            0f, 12.5f, 12.5f, 25f,
            12.5f, 25f, 25f, 12.5f,
            25f, 12.5f, 18.75f, 0f,
            18.75f, 0f, 25f, -12.5f,
            25f, -12.5f, 6.25f, -25f,
            6.25f, -25f, -12.5f, -25f,
            -12.5f, -25f, -25f, -12.5f,
            -25f, -12.5f, -25f, 12.5f,
            -25f, 12.5f, -12.5f, 25f,
            -12.5f, 25f, 0f, 12.5f
        )

        init {
            brush.setARGB(255, 160, 160, 160)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.STROKE
        }
    } // companion object
} // Asteroid