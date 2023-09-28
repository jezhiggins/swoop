package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.utils.Repeat
import uk.co.jezuk.swoop.wave.Wave
import kotlin.time.Duration.Companion.seconds

class Mine(
    private val wave: Wave,
    override val position: Point
): Target {
    private val brush = Paint()
    private val throbber = Repeat(0.22.seconds, ::throb)
    private var radius = 4f

    init {
        wave.addTarget(this)
        Game.sound(R.raw.minedrop, position)

        brush.color = Color.YELLOW
        brush.alpha = 180
        brush.strokeWidth = 5f
        brush.strokeCap = Paint.Cap.ROUND
        brush.style = Paint.Style.FILL_AND_STROKE
    } // init

    private fun throb() {
        radius = if (radius == 4f) 2f else 4f
    } // throb

    override val killDist get() = 20f

    override fun update(frameRateScale: Float) {
        throbber.tick(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        val o = 12f - radius
        canvas.drawLine(-o, -o, o, o, brush)
        canvas.drawLine(-o, o, o, -o, brush)
        canvas.drawCircle(-o, -o, radius, brush)
        canvas.drawCircle(-o, o, radius, brush)
        canvas.drawCircle(o, -o, radius, brush)
        canvas.drawCircle(o, o, radius, brush)

        canvas.restore()
    } // draw

    override fun shot(): Target.Effect {
        explode()
        return Target.Hard(50)
    } // shot

    override fun explode() {
        Game.sound(R.raw.mineexplosion, position)
        wave.removeTarget(this)
        Puff(wave, position)
    } // explode

    override fun playerCollision(player: Player) {
        explode()
        player.hit()
    } // playerCollision
} // class Minelayer