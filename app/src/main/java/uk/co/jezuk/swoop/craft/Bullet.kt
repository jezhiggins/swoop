package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Frames
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

//////////////////////
class Bullet(
    private val player: Player,
    private val brush: Paint,
    private val wave: Wave,
    duration: Duration
): Projectile {
    private val maxAge = Frames.In(duration)
    override val position = player.position.copy()
    override val killDist = 1f

    private val velocity = player.velocity.copy()
    private var age = 0f

    init {
        velocity.maximum = 30.0
        velocity += Vector(10.0, player.orientation, 30.0)

        val toPointyEndOfShop = Vector(Ship.Radius*2, player.orientation, Ship.Radius*2)
        position.move(toPointyEndOfShop, 1f, Game.extent, Ship.Radius)

        wave.addProjectile(this)
    } // init

    override fun update(frameRateScale: Float) {
        position.move(velocity, frameRateScale, Game.extent, Ship.Radius)
        age += frameRateScale
        if (age > maxAge) wave.removeProjectile(this)
    } // update

    override fun draw(canvas: Canvas) {
        position.draw(canvas, brush)
    } // draw

    override fun hit(effect: Target.Effect) {
        player.scored(effect.score)
        when (effect.impact) {
            Target.Impact.HARD -> age = Frames.In(4.seconds)
            Target.Impact.SOFT -> age += Frames.In(0.4.seconds)
            Target.Impact.NONE -> Unit
        }
    } // hit

    companion object {
        val GreenBrush = Brush(Color.rgb(173, 255, 47))
        val MagentaBrush = Brush(Color.MAGENTA)

        private fun Brush(color: Int): Paint {
            val brush = Paint()
            brush.color = color
            brush.alpha = 255
            brush.strokeWidth = 10f
            brush.strokeCap = Paint.Cap.ROUND
            return brush
        }
    } // companion object
} // Bullet