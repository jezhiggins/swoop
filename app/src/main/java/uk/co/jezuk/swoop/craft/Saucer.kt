package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave
import kotlin.math.sin
import kotlin.random.Random

class Saucer(
    private val game: Game,
    private val wave: Wave,
    aggressiveness: Int,
    traverse: Array<Point> = game.extent.randomHorizontalTraverse()
): Target {
    override val position = Point(traverse[0])
    private val startPosition = Point(traverse[0])
    private val basePosition = Point(traverse[0])
    private val velocity = Vector(Random.nextDouble(3.0, 3.0 + aggressiveness), position.angleTo(traverse[1]))
    private val bounciness = Random.nextDouble(1.0, (aggressiveness * 2.0)) * Math.PI
    private val swinginess = Random.nextDouble(100.0, 100.0 + (50.0 * aggressiveness))
    private var bounceOffset = 0.0
    private val traverseLength = startPosition.distance(traverse[1])
    private val skew = Rotation.random()
    private val firedStep = if (wave.ship != null) Random.nextDouble(0.4 - (0.07 * aggressiveness), 0.4) else 1000.0
    private var fired = firedStep
    private var exploder: Exploder? = null

    private val alive get() = (exploder == null)

    init {
        game.sound(R.raw.sauceralarm, position)
        wave.addTarget(this)
    } // init

    override val killDist get() = 50f

    override fun update(frameRateScale: Float) {
        val distance = basePosition.distance(startPosition) / traverseLength
        if (alive)
            bounceOffset = sin(distance  * bounciness) * swinginess

        position.moveTo(basePosition)
        position.move(Vector(bounceOffset, skew), 1f, game.extent, killDist)

        if (!basePosition.moveNoWrap(velocity, frameRateScale, game.extent, killDist))
            destroyed();

        fireIfReady(distance)

        if (!alive)
            exploder?.update(frameRateScale)
    } // update

    private fun fireIfReady(distance: Float) {
        if (!alive) return
        if (fired > distance) return

        fired += firedStep

        val direction = position.angleTo(wave.ship!!.position)

        game.sound(R.raw.saucerfire, position)
        for (offset in -30..30 step 30)
            Missile(game, wave, Point(position), Vector(7.0, direction-offset))
    }

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        if (alive)
            canvas.drawLines(shape, shipBrush)
        else
            exploder?.draw(canvas)

        canvas.restore()
    } // draw

    override fun shot(): Target.Impact {
        if (alive) {
            game.scored(1500)
            explode()
            return Target.Impact.HARD
        }
        return Target.Impact.NONE
    } // shot

    override fun explode() {
        exploder = Exploder(::destroyed, shape, shipBrush, 70)
        game.sound(R.raw.saucerexplosion, position)
        BigPuff(wave, position)
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

        val shipBrush = Paint()

        init {
            shipBrush.color = Color.YELLOW
            shipBrush.strokeWidth = 8f
            shipBrush.strokeCap = Paint.Cap.ROUND
            shipBrush.style = Paint.Style.STROKE
        } // init
    }
} // class Saucer