package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector

//////////////////////
class Bullet(
    private val game: Game,
    pos: Point,
    orientation: Double,
    initVel: Vector
): Craft {
    override val position = pos.copy()
    override val killDist = 1f

    private var velocity = initVel.copy()
    var age = 0

    init {
        velocity.maximum = 30.0
        velocity += Vector(10.0, orientation, 30.0)
    } // init

    override fun update(fps: Long) {
        position.move(velocity, game.extent)
        ++age
    } // update

    override fun draw(canvas: Canvas) {
        canvas.drawPoint(
            position.x.toFloat(),
            position.y.toFloat(),
            brush
        )
    } // draw

    fun hit() {
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