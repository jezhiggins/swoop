package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint

class Asteroids {
    private val asteroids: MutableList<Asteroid> = mutableListOf()

    fun wave(count: Int, width: Int, height: Int) {
        asteroids.clear()
        for(a in 0 until count) {
            asteroids.add(
                Asteroid(
                    Point(
                        (Math.random() * width),
                        (Math.random() * height)
                    ),
                    Vector(2.0, Math.random() * 360)
                )
            )
        }
    } // wave

    fun update(fps: Long, width: Int, height: Int) {
        asteroids.forEach { a -> a.update(fps, width, height) }
    }

    fun draw(canvas: Canvas) {
        asteroids.forEach { a -> a.draw(canvas) }
    }

    fun findCollisions(ship: Ship) {
        asteroids.forEach {
            a -> a.checkShipCollision(ship)
        }
    }
}

class Asteroid(
    private val position: Point,
    private var velocity: Vector
) {
    private val brush = Paint()
    private val killDist = 100f

    init {
        brush.setARGB(127, 255, 255, 255)
        brush.strokeWidth = 3f
        brush.strokeJoin = Paint.Join.BEVEL
        brush.style = Paint.Style.STROKE
    }

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

        canvas.drawCircle(0f, 0f, killDist, brush)

        canvas.restore()
    } // draw

    fun checkShipCollision(ship: Ship) {
        if (ship.position.distance(position) < killDist) {
            ship.explode()
        }
    } // checkShipCollision
} // Asteroid