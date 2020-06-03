package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector

class Asteroids {
    private val asteroids: MutableList<Asteroid> = mutableListOf()

    fun wave(count: Int, width: Int, height: Int) {
        asteroids.clear()
        for(a in 0 until count) {
            add(
                Asteroid(
                    this,
                    Point(
                        (Math.random() * width),
                        (Math.random() * height)
                    )
                )
            )
        }
    } // wave

    fun add(asteroid: Asteroid) = asteroids.add(asteroid)
    fun remove(asteroid: Asteroid) = asteroids.remove(asteroid)

    fun update(fps: Long, width: Int, height: Int) {
        asteroids.forEach { a -> a.update(fps, width, height) }
    } // update

    fun draw(canvas: Canvas) {
        asteroids.forEach { a -> a.draw(canvas) }
    } // draw

    fun findCollisions(ship: Ship) {
        val working = mutableListOf<Asteroid>()
        working.addAll(asteroids)
        working.forEach {
            a -> a.checkShipCollision(ship)
        }
    } // findCollisions
} // Asteroids

fun AsteroidVector(scale: Float) =
    Vector(6.0 - scale, Math.random() * 360)

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

        canvas.drawCircle(0f, 0f, killRadius, brush)

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
        val brush = makeAsteroidBrush()

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