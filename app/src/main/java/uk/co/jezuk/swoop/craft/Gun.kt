package uk.co.jezuk.swoop.craft

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.utils.Repeat
import uk.co.jezuk.swoop.wave.Wave
import kotlin.math.max
import kotlin.math.min

class Gun(
    private val game: Game,
    private val wave: Wave,
    private val ship: Ship
) {
    private var repeatDelay = 12
    private var trigger = Repeat(repeatDelay, { fire() })
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

    fun upgrade() {
        repeatDelay = max(repeatDelay-1, 7)
        bulletMaxAge = min(bulletMaxAge+5, 120)
        trigger.reset(repeatDelay)
    } // upgrade
} // Gun