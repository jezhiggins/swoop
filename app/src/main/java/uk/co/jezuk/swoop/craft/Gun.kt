package uk.co.jezuk.swoop.craft

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.utils.Repeat
import uk.co.jezuk.swoop.wave.Wave
import kotlin.math.max
import kotlin.math.min

class Gun(
    private val game: Game,
    private val wave: Wave,
    private val ship: Ship,
    oldGun: Gun? = null
) {
    private var repeatDelay: Int = oldGun?.repeatDelay ?: 12
    private var trigger = Repeat(repeatDelay, { fire() })
    private var bulletMaxAge:Int = oldGun?.bulletMaxAge ?: 75

    init {
        ship.onLifeLost { reset() }
    }

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
        reset(
            max(repeatDelay-1, 7),
            min(bulletMaxAge+10, 140)
        )
    } // upgrade

    private fun reset() {
        reset(12, 75)
    } // reset

    private fun reset(newRepeat: Int, newMaxAge: Int) {
        repeatDelay = newRepeat
        bulletMaxAge = newMaxAge
        trigger.reset(repeatDelay)
    } // update
} // Gun