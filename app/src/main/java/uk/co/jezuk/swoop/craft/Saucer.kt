package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave
import kotlin.math.sin
import kotlin.random.Random

class Saucer(
    private val game: Game,
    private val wave: Wave,
    traverse: Array<Point> = game.extent.randomHorizontalTraverse()
): Target {
    override val position = Point(traverse[0])
    private val startPosition = Point(traverse[0])
    private val basePosition = Point(traverse[0])
    private val velocity = Vector(4.0 + Random.nextDouble(5.0), position.angleTo(traverse[1]))
    private val bounciness = 1.0 + Random.nextDouble(7.0) * Math.PI
    private val swinginess = 100.0 + (Random.nextDouble(250.0) * 2)
    private val traverseLength = startPosition.distance(traverse[1])
    private val skew = Rotation.random()
    private val shipBrush = Paint()

    init {
        wave.addTarget(this)

        shipBrush.color = Color.YELLOW
        shipBrush.strokeWidth = 8f
        shipBrush.strokeCap = Paint.Cap.ROUND
        shipBrush.style = Paint.Style.STROKE
    } // init

    override val killDist get() = 50f

    override fun update(frameRateScale: Float) {
        val distance = basePosition.distance(startPosition)
        val sinusoid = sin((distance / traverseLength) * bounciness) * swinginess

        position.moveTo(basePosition)
        position.move(Vector(sinusoid, skew), 1f, game.extent, killDist)

        if (!basePosition.moveNoWrap(velocity, frameRateScale, game.extent, killDist))
            destroyed();
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        canvas.drawLines(shape, shipBrush)

        canvas.restore()
    } // draw

    override fun shot(): Target.Impact {
        return Target.Impact.HARD
    } // shot

    override fun explode() {
        Puff(wave, position)
        destroyed()
    } // explode

    override fun shipCollision(ship: Ship) = ship.hit()

    private fun destroyed() {
        wave.removeTarget(this)
    }

    companion object {
        val shape = floatArrayOf(
            -50f, 0f, 50f, 0f,
            -50f, 0f, -30f, 20f,
            -30f, 20f, 30f, 20f,
            30f, 20f, 50f, 0f,
            -50f, 0f, -30f, -20f,
            -30f, -20f, 30f, -20f,
            30f, -20f, 50f, 0f,
            -25f, -20f, -25f, -35f,
            -25f, -35f, 25f, -35f,
            25f, -20f, 25f, -35f
        )
    }
} // class Saucer