package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import uk.co.jezuk.swoop.wave.Wave

class Comet(
    private val game: Game,
    private val wave: Wave
): Target {
    override val position = game.extent.randomPointOnEdge()
    private var velocity = CometVector(position)
    private var orientation = (Math.random() * 360).toFloat()
    private val rotation = (Math.random() * 11).toFloat() - 5f
    private val cometPath = Path()

    init {
        cometPath.moveTo(shape[0], shape[1])
        for (i in 0 until shape.size step 2)
            cometPath.lineTo(shape[i], shape[i+1])
        cometPath.close()

        wave.addTarget(this)
    } // init

    override val killDist get() = 50f

    override fun update(frameRateScale: Float) {
        if (!position.moveNoWrap(velocity, frameRateScale, game.extent, killDist))
            wave.removeTarget(this)
        orientation += rotation
        if (orientation < 0) orientation += 360
        if (orientation > 360) orientation -= 360
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)

        // canvas.drawCircle(0f, 0f, killRadius, brush)
        canvas.rotate(orientation)
        canvas.drawPath(cometPath, brush)

        canvas.restore()
    } // draw

    override fun shot() { }
    override fun explode() { }

    override fun shipCollision(ship: Ship) = ship.hit()

    companion object {
        fun CometVector(position: Point) =
            Vector(7.0, angleFromOffsets(position.x, position.y))

        val brush = Paint()
        val shape = floatArrayOf(
            0f, 25f, 25f, 50f,
            25f, 50f, 50f, 25f,
            50f, 25f, 37.5f, 0f,
            37.5f, 0f, 50f, -25f,
            50f, -25f, 12.5f, -50f,
            12.5f, -50f, -25f, -50f,
            -25f, -50f, -50f, -25f,
            -50f, -25f, -50f, 25f,
            -50f, 25f, -25f, 50f,
            -25f, 50f, 0f, 25f
        )

        init {
            brush.setARGB(255, 160, 160, 225)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.FILL_AND_STROKE
        }
    } // companion object
} // Comet