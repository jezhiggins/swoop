package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.craft.asteroid.Asteroid
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class Comet(
    private val wave: Wave
): Target {
    override val position = Game.extent.randomPointOnEdge()
    private val extentWithTail = Game.extent.inflated(500f)
    private val velocity = CometVector(position)
    private val orientation = Rotation.random()
    private val rotation = Random.nextDouble(-5.0, 5.0)
    private val slap = { Game.sound(R.raw.cometslap, position) }
    private val cometPath = Asteroid.paths[Random.nextInt(4)]

    init {
        wave.addTarget(this)
    } // init

    override val killDist get() = 50f

    override fun update(frameRateScale: Float) {
        if (!position.moveNoWrap(velocity, frameRateScale, extentWithTail, killDist))
            wave.removeTarget(this)
        orientation += (rotation * frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)

        // canvas.drawCircle(0f, 0f, killRadius, brush)
        canvas.save()
        orientation.rotate(canvas)
        canvas.scale(2f, 2f)
        canvas.drawPath(cometPath, brush)
        canvas.restore()

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

    override fun shot(): Target.Effect {
        slap()
        return Target.Hard()
    } // shot
    override fun explode() { }

    override fun playerCollision(player: Player) = player.hit()

    companion object {
        fun CometVector(position: Point) =
            Vector(Random.nextDouble(4.0, 7.0), angleFromOffsets(position.x, position.y))

        val brush = Paint()

        init {
            brush.setARGB(255, 160, 160, 225)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.FILL_AND_STROKE
        } // init
    } // companion object
} // Comet