package uk.co.jezuk.swoop.craft.asteroid

import android.graphics.Color
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
    override val fillBrush = brush
    override val outlineBrush = outline

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
            OrangeSpaceman(wave, position)
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
        val brush = fillBrush(Color.BLACK)
        val outline = outlineBrush(Color.argb(255, 160, 160, 160))

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