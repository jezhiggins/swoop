package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave
import kotlin.math.sin
import kotlin.random.Random

class Saucer(
    private val wave: Wave,
    aggressiveness: Int,
    traverse: Array<Point> = Game.extent.randomHorizontalTraverse()
): Target {
    override val position = Point(traverse[0])
    private val startPosition = Point(traverse[0])
    private val basePosition = Point(traverse[0])
    private val velocity = Vector(Random.nextDouble(3.0, 3.0 + aggressiveness), position.angleTo(traverse[1]))
    private val bounciness = Random.nextDouble(1.0, (aggressiveness * 2.0)) * Math.PI
    private val swinginess = Random.nextDouble(100.0, 100.0 + (50.0 * aggressiveness))
    private val traverseLength = startPosition.distance(traverse[1])
    private val skew = Rotation.random()
    private val firedStep = if (wave.players.isNotEmpty()) Random.nextDouble(0.4 - (0.07 * aggressiveness), 0.4) else 1000.0
    private var fired = firedStep

    init {
        Game.sound(R.raw.sauceralarm, position)
        wave.addTarget(this)
    } // init

    override val killDist get() = 50f

    override fun update(frameRateScale: Float) {
        val distance = basePosition.distance(startPosition) / traverseLength
        val bounceOffset = sin(distance  * bounciness) * swinginess

        position.moveTo(basePosition)
        position.move(Vector(bounceOffset, skew), 1f, Game.extent, killDist)

        if (!basePosition.moveNoWrap(velocity, frameRateScale, Game.extent, killDist))
            destroyed()

        fireIfReady(distance)
    } // update

    private fun fireIfReady(distance: Float) {
        if (fired > distance) return

        fired += firedStep

        val direction = position.angleTo(wave.players[Random.nextInt(0, wave.players.size)].position)

        Game.sound(R.raw.saucerfire, position)
        for (offset in -30..30 step 30)
            Missile(wave, Point(position), Vector(7.0, direction-offset))
    }

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)

        canvas.drawLines(shape, shipBrush)

        canvas.restore()
    } // draw

    override fun shot(): Target.Effect {
        explode()
        return Target.Hard(1500)
    } // shot

    override fun explode() {
        Game.sound(R.raw.saucerexplosion, position)
        BigPuff(wave, position)
        Explosion(
            wave, position, velocity, Rotation.none(), 0.0,
            shape, shipBrush, 70
        )
        destroyed()
    } // explode

    override fun playerCollision(player: Player) = player.hit()
    private fun destroyed() = wave.removeTarget(this)

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