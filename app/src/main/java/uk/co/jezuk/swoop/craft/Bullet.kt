package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.geometry.Vector

//////////////////////
class Bullet(
    private val player: Player,
    private val maxAge: Int
): Projectile {
    override val position = player.position.copy()
    override val killDist = 1f

    private val velocity = player.velocity.copy()
    private var age = 0

    init {
        velocity.maximum = 30.0
        velocity += Vector(10.0, player.orientation, 30.0)

        val toPointyEndOfShop = Vector(Ship.Radius*2, player.orientation, Ship.Radius*2)
        position.move(toPointyEndOfShop, 1f, Game.extent, Ship.Radius)

        player.wave.addProjectile(this)
    } // init

    override fun update(frameRateScale: Float) {
        position.move(velocity, frameRateScale, Game.extent, Ship.Radius)
        if (++age > maxAge) player.wave.removeProjectile(this)
    } // update

    override fun draw(canvas: Canvas) {
        position.draw(canvas, brush)
    } // draw

    override fun hit(effect: Target.Effect) {
        player.scored(effect.score)
        when (effect.impact) {
            Target.Impact.HARD -> age = 100
            Target.Impact.SOFT -> age += 20
            Target.Impact.NONE -> Unit
        }
    } // hit

    companion object {
        val brush = Paint()

        init {
            brush.color = Color.MAGENTA
            brush.alpha = 255
            brush.strokeWidth = 10f
            brush.strokeCap = Paint.Cap.ROUND
        }
    } // companion object
} // Bullet