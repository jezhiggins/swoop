package uk.co.jezuk.swoop.craft

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.utils.Repeat
import uk.co.jezuk.swoop.wave.Wave

class Gun(
    private val game: Game,
    private val wave: Wave,
    private val ship: Ship
) {
    private var trigger = Repeat(12, { fire() })
    private var bulletMaxAge = 75

    private fun fire() {
        if (!ship.armed) return

        Bullet(
            game,
            wave,
            ship.position,
            ship.orientation,
            ship.velocity,
            bulletMaxAge
        )
    } // fire

    fun update(frameRateScale: Float) {
        trigger.tick(frameRateScale)
    } // update
} // Gun