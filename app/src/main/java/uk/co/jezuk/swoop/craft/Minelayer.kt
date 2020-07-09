package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave

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

    init {
        wave.addTarget(this)

        shipBrush.setARGB(255, 127, 255, 255)
        shipBrush.strokeWidth = 5f
        shipBrush.strokeCap = Paint.Cap.ROUND
        shipBrush.style = Paint.Style.STROKE

        shieldBrush.setARGB(127, 180, 0, 180)
        shieldBrush.strokeWidth = 8f
        shieldBrush.strokeCap = Paint.Cap.ROUND
        shieldBrush.style = Paint.Style.STROKE
    } // init

    override val killDist get() = shieldRadius

    override fun update(frameRateScale: Float) {
        if (!position.moveNoWrap(velocity, frameRateScale, game.extent, killDist))
            wave.removeTarget(this)
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        canvas.rotate(velocity.angle.toFloat())
        canvas.drawLines(shape, shipBrush)
        canvas.drawCircle(0f, 0f, shieldRadius, shieldBrush)

        canvas.restore()
    } // draw

    override fun shot(): Target.Impact {
        shieldRadius -= 5f

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