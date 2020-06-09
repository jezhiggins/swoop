package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class Comet(
    private val game: Game,
    private val wave: Wave
): Target {
    override val position = game.extent.randomPointOnEdge()
    private val extentWithTail = game.extent.inflated(500f)
    private var velocity = CometVector(position)
    private var orientation = Rotation.random()
    private val rotation = Random.nextDouble(-5.0, 5.0)
    private val slap = game.sounds.load(R.raw.cometslap)

    init {
        wave.addTarget(this)
    } // init

    override val killDist get() = 50f

    override fun update(frameRateScale: Float) {
        if (!position.moveNoWrap(velocity, frameRateScale, extentWithTail, killDist))
            wave.removeTarget(this)
        orientation += rotation
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)

        // canvas.drawCircle(0f, 0f, killRadius, brush)
        orientation.rotate(canvas)
        canvas.drawPath(cometPath, brush)
        orientation.unrotate(canvas)

        // comet tail
        canvas.rotate(velocity.angle.toFloat())
        for (y in -50 until 50 step 10)
            canvas.drawLine(
                -35f + Random.nextInt(0, 20),
                y.toFloat(),
                -500f + Random.nextInt(-50, 100),
                (y*Random.nextDouble(1.0, 1.75)).toFloat(),
                brush
            )

        canvas.restore()
    } // draw

    override fun shot() {
        slap(position.pan(game.extent))
    }
    override fun explode() { }

    override fun shipCollision(ship: Ship) = ship.hit()

    companion object {
        fun CometVector(position: Point) =
            Vector(Random.nextDouble(4.0, 7.0), angleFromOffsets(position.x, position.y))

        val brush = Paint()
        private val shape = floatArrayOf(
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
        val cometPath = Path()

        init {
            brush.setARGB(255, 160, 160, 225)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.FILL_AND_STROKE

            cometPath.moveTo(shape[0], shape[1])
            for (i in shape.indices step 2)
                cometPath.lineTo(shape[i], shape[i+1])
            cometPath.close()
        } // init
    } // companion object
} // Comet