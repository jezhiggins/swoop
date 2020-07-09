package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.utils.RestartableLatch
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class Minelayer(
    private val game: Game,
    private val wave: Wave
): Target {
    private val traverse = game.extent.randomTraverse()

    override val position = traverse[0]
    private val velocity = Vector(4.0, position.angleTo(traverse[1]))
    private val shipBrush = Paint()
    private val shieldBrush = Paint()
    private var shieldRadius = 75f
    private var dropAt = Point(position)
    private var trigger = RestartableLatch(25, ::dropMine)
    private val minefield = game.extent.inflated(-30f)
    private var dropping = Random.nextBoolean()

    init {
        trigger.start()

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

        if (minefield.within(dropAt) && dropping)
            Mine(game, wave, dropAt)
        dropAt = Point(position)
        trigger.start()
    } // dropMine

    override val killDist get() = shieldRadius

    override fun update(frameRateScale: Float) {
        trigger.tick(frameRateScale)

        if (!position.moveNoWrap(velocity, frameRateScale, game.extent, killDist))
            wave.removeTarget(this)
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

    override fun shot(): Target.Impact {
        shieldRadius -= 7f

        if (shieldRadius < 42f) {
            game.scored(500)
            explode()
        }
        return Target.Impact.HARD
    } // shot

    override fun explode() {
        wave.removeTarget(this)
        Puff(wave, position)
    } // explode

    override fun shipCollision(ship: Ship) = ship.hit()

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