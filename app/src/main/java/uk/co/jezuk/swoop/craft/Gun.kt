package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector

class Gun(private val game: Game, private val ship: Ship) {
    private val bullets = mutableListOf<Bullet>()
    private var rate = 20
    private var tick = 0

    private fun fire() {
        val bullet = Bullet(
            game,
            ship.position,
            ship.orientation,
            ship.velocity
        )
        bullets.add(bullet)
    }

    fun update(fps: Long) {
        bullets.forEach { b -> b.update(fps) }
        bullets.removeIf { b -> b.age >= 60 }

        if (++tick != rate) return
        if (!ship.armed) return

        tick = 0
        fire()
    } // update

    fun draw(canvas: Canvas) {
        bullets.forEach { b -> b.draw(canvas)}
    } // draw

    private class Bullet(
        private val game: Game,
        pos: Point,
        orientation: Double,
        initVel: Vector
    ) {
        private var position = pos.copy()
        private var velocity = initVel.copy()
        var age = 0

        init {
            velocity += Vector(10.0, orientation)
        }

        fun update(fps: Long) {
            position.move(velocity, game.extent)
            ++age
        }

        fun draw(canvas: Canvas) {
            canvas.drawPoint(
                position.x.toFloat(),
                position.y.toFloat(),
                brush
            )
        } // draw

        companion object {
            val brush = Paint()

            init {
                brush.setARGB(255, 255, 0, 255)
                brush.strokeWidth = 10f
                brush.strokeCap = Paint.Cap.ROUND
            }
        } // companion object
    } // Bullet
} // Gun