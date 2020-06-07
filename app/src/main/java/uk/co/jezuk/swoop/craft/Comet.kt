package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.geometry.angleFromOffsets

class Comet(
    private val game: Game
) {
    val position = game.extent.randomPointOnEdge()
    private var velocity = CometVector(position)
    private var orientation = (Math.random() * 360).toFloat()
    private val rotation = (Math.random() * 3).toFloat() - 2f
    private val cometPath = Path()

    init {
        cometPath.moveTo(shape[0], shape[1])
        for (i in 0 until shape.size step 2)
            cometPath.lineTo(shape[i], shape[i+1])
        cometPath.close()
    }

    val killDist get() = 50f

    fun update(fps: Long) {
        position.move(velocity, game.extent, killDist)
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

        // canvas.drawCircle(0f, 0f, killRadius, brush)
        canvas.rotate(orientation)
        canvas.drawPath(cometPath, brush)

        canvas.restore()
    } // draw

    fun shot() {
    } // shot

    fun checkShipCollision(ship: Ship) {
        if (ship.position.distance(position) < (killDist + ship.killDist)) {
            ship.hit()
        }
    } // checkShipCollision

    companion object {
        fun CometVector(position: Point) =
            Vector(8.0, angleFromOffsets(position.x, position.y))

        val brush = Paint()
        val shape = floatArrayOf(
            0f, 25f, 25f, 50f,
            25f, 50f, 50f, 25f,
            50f, 25f, 18.75f, 0f,
            37.5f, 0f, 50f, -25f,
            50f, -25f, 13f, -50f,
            13f, -50f, -25f, -50f,
            -25f, -50f, -50f, -25f,
            -50f, -25f, -50f, 25f,
            -50f, 25f, -25f, 50f,
            -25f, 50f, 0f, 25f
        )

        init {
            brush.setARGB(255, 160, 160, 200)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.FILL_AND_STROKE
        }
    } // companion object
} // Comet