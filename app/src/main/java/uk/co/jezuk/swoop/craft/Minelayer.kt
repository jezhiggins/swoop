package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.utils.RestartableLatch
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class Minelayer(
    private val wave: Wave,
    private val onDestroyed: () -> Unit,
    traverse: Array<Point> = Game.extent.randomTraverse(),
    private val alwaysDrop: Boolean = false
): Target {
    override val position = traverse[0]
    private val velocity = Vector(4.0, position.angleTo(traverse[1]))
    private val shipBrush = Paint()
    private val shieldBrush = Paint()
    private var shieldRadius = 75f
    private var dropAt = Point(position)
    private var trigger = RestartableLatch(25, ::dropMine)
    private val minefield = Game.extent.inflated(-30f)
    private var dropping = Random.nextBoolean()

    init {
        trigger.start()

        Game.sound(R.raw.minelayeralarm, position)
        wave.addTarget(this)

        shipBrush.setARGB(225, 60, 255, 255)
        shipBrush.strokeWidth = 8f
        shipBrush.strokeCap = Paint.Cap.ROUND
        shipBrush.style = Paint.Style.STROKE

        shieldBrush.setARGB(80, 180, 0, 180)
        shieldBrush.strokeWidth = 16f
        shieldBrush.strokeCap = Paint.Cap.ROUND
        shieldBrush.style = Paint.Style.STROKE
    } // init

    private fun dropMine() {
        if (Random.nextFloat() < 0.2) dropping = !dropping

        if (minefield.within(dropAt) && (dropping || alwaysDrop))
            Mine(wave, dropAt)
        dropAt = Point(position)
        trigger.start()
    } // dropMine

    override val killDist get() = shieldRadius

    override fun update(frameRateScale: Float) {
        trigger.tick(frameRateScale)

        if (!position.moveNoWrap(velocity, frameRateScale, Game.extent, killDist))
            destroyed()
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        canvas.rotate(velocity.angle.toFloat())
        canvas.drawLines(shape, shipBrush)
        if (shieldRadius >= 50f)
            canvas.drawCircle(0f, 0f, shieldRadius, shieldBrush)

        canvas.restore()
    } // draw

    override fun shot(): Target.Effect {
        shieldRadius -= 7f

        if (shieldRadius < 42f) {
            explode()
            return Target.Hard(500)
        }

        Game.sound(R.raw.minelayershieldhit, position)
        return Target.Hard()
    } // shot

    override fun explode() {
        destroyed()
        Game.sound(R.raw.minelayerexplosion, position)
        Puff(wave, position)
    } // explode

    private fun destroyed() {
        wave.removeTarget(this)
        onDestroyed()
    } // destroyed

    override fun playerCollision(player: Player) = player.hit()

    companion object {
        val shape = floatArrayOf(
            45f, 0f, -35f, 30f,
            45f, 0f, -35f, -30f,
            -35f, 30f, -35f, -30f,
            -35f, 10f, -50f, 25f,
            -35f, -10f, -50f, -25f
        )
    }
} // class Minelayer