package uk.co.jezuk.swoop.craft.asteroid

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.craft.spaceman.OrangeSpaceman
import uk.co.jezuk.swoop.craft.Target
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class StonyAsteroid(
    game: Game,
    wave: Wave,
    pos: Point,
    scale: Float = Big
): Asteroid(game, wave, pos, scale) {
   override fun drawAsteroid(canvas: Canvas) {
       canvas.drawLines(
           shape,
           brush
       )
   } // drawAsteroid

    override fun shot(): Target.Effect {
        split()
        return Target.Soft(400 / size.toInt())
    } // shot

    override fun playerCollision(player: Player) {
        split()
        player.hit()
    } // playerCollision

    private fun split() {
        if (Random.nextFloat() < 0.1f)
            OrangeSpaceman(game, wave, position)
        explode()

        if (size != Small) {
            scale /= 2
            velocity = AsteroidVector(scale)
            StonyAsteroid(
                game,
                wave,
                position,
                scale
            )
        } else {
            wave.removeTarget(this)
        }
    } // split

    companion object {
        val brush = Paint()

        init {
            brush.setARGB(255, 160, 160, 160)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.STROKE
        }

        fun field(
            game: Game,
            wave: Wave,
            big: Int,
            medium: Int = 0,
            small: Int = 0,
            originFn: () -> Point = { Game.extent.randomPointOnEdge() }
        ) {
            field(
                ::StonyAsteroid,
                game,
                wave,
                big,
                medium,
                small,
                originFn
            )
        } // field
    } // companion object
} // Asteroid