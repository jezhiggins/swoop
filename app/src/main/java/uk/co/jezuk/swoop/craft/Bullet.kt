package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave

//////////////////////
class Bullet(
    private val game: Game,
    private val wave: Wave,
    pos: Point,
    orientation: Double,
    initVel: Vector
): Projectile {
    override val position = pos.copy()
    override val killDist = 1f

    private val velocity = initVel.copy()
    var age = 0
    private val maxAge = 75

    init {
        velocity.maximum = 30.0
        velocity += Vector(10.0, orientation, 30.0)

        wave.addProjectile(this)
    } // init

    override fun update(fps: Long) {
        position.move(velocity, game.extent)
        if (++age > maxAge) wave.removeProjectile(this)
    } // update

    override fun draw(canvas: Canvas) {
        canvas.drawPoint(
            position.x.toFloat(),
            position.y.toFloat(),
            brush
        )
    } // draw

    override fun hit() {
        age += 20
    } // hit

    companion object {
        val brush = Paint()

        init {
            brush.color = Color.MAGENTA
            brush.alpha = 255
            brush.strokeWidth = 10f
            brush.strokeCap = Paint.Cap.ROUND
        }
    } // companion object
} // Bullet