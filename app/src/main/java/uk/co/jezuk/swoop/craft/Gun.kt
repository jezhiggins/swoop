package uk.co.jezuk.swoop.craft

import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.utils.Repeat
import kotlin.math.max
import kotlin.math.min

class Gun(
    private val player: Player,
) {
    private var repeatDelay: Int = initialRepeatDelay
    private var bulletMaxAge:Int = initialBulletMaxAge
    private var trigger = Repeat(repeatDelay) { fire() }

    private fun fire() {
        Bullet(
            player,
            player.wave,
            bulletMaxAge
        )
    } // fire

    fun update(frameRateScale: Float) {
        trigger.tick(frameRateScale)
    } // update

    fun upgrade() {
        reset(
            max(repeatDelay-1, shortestRepeatDelay),
            min(bulletMaxAge+10, highestBulletMaxAge)
        )
    } // upgrade

    fun reset() {
        reset(initialRepeatDelay, initialBulletMaxAge)
    } // reset

    private fun reset(newRepeat: Int, newMaxAge: Int) {
        repeatDelay = newRepeat
        bulletMaxAge = newMaxAge
        trigger.reset(repeatDelay)
    } // update

    companion object {
        private const val initialRepeatDelay: Int = 12
        private const val initialBulletMaxAge: Int = 75

        private const val shortestRepeatDelay: Int = 7
        private const val highestBulletMaxAge: Int = 140
    }
} // Gun