package uk.co.jezuk.swoop.craft.asteroid

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.craft.Puff
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.Spaceman
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

    override fun shot(): Target.Impact {
        game.scored(400/size.toInt())
        split()
        return Target.Impact.SOFT
    } // shot

    override fun shipCollision(ship: Ship) {
        split()
        ship.hit()
    } // shipCollision

    private fun split() {
        if (Random.nextFloat() < 0.1f)
            Spaceman(game, wave, position)
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
            originFn: () -> Point = { game.extent.randomPointOnEdge() }
        ) {
            val sizes = mapOf(
                Pair(big,
                    Big
                ),
                Pair(medium,
                    Medium
                ),
                Pair(small,
                    Small
                )
            )
            for ((count, size) in sizes) {
                for(a in 0 until count) {
                    StonyAsteroid(
                        game,
                        wave,
                        originFn(),
                        size
                    )
                }
            }
        } // field
    } // companion object
} // Asteroid