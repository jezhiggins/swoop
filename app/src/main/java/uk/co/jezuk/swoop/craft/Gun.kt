package uk.co.jezuk.swoop.craft

import android.graphics.Paint
import uk.co.jezuk.swoop.Frames
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.utils.Repeat
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Gun(
    private val player: Player,
    private val brush: Paint
) {
    private var repeatDelay = initialRepeatDelay
    private var bulletMaxAge = initialBulletMaxAge
    private var trigger = Repeat(repeatDelay) { fire() }

    private fun fire() {
        Bullet(
            player,
            brush,
            player.wave,
            bulletMaxAge
        )
    } // fire

    fun update(frameRateScale: Float) {
        trigger.tick(frameRateScale)
    } // update

    fun upgrade() {
        reset(
            max(repeatDelay-0.02.seconds, shortestRepeatDelay),
            min(bulletMaxAge+0.2.seconds, highestBulletMaxAge)
        )
    } // upgrade

    fun reset() {
        reset(initialRepeatDelay, initialBulletMaxAge)
    } // reset

    private fun reset(newRepeat: Duration, newMaxAge: Duration) {
        repeatDelay = newRepeat
        bulletMaxAge = newMaxAge
        trigger.reset(repeatDelay)
    } // update

    companion object {
        private val initialRepeatDelay = 0.24.seconds
        private val initialBulletMaxAge = 1.5.seconds

        private val shortestRepeatDelay= 0.14.seconds
        private val highestBulletMaxAge = 3.seconds

        private fun max(lhs: Duration, rhs: Duration) =
            if (lhs > rhs) lhs else rhs

        private fun min(lhs: Duration, rhs: Duration) =
            if (lhs < rhs) lhs else rhs
    }
} // Gun