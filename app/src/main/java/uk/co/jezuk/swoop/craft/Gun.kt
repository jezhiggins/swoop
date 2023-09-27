package uk.co.jezuk.swoop.craft

import android.graphics.Paint
import uk.co.jezuk.swoop.Frames
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.utils.Repeat
import kotlin.math.max
import kotlin.math.min

class Gun(
    private val player: Player,
    private val brush: Paint
) {
    private var repeatDelay: Int = initialRepeatDelay
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
            max(repeatDelay-1, shortestRepeatDelay),
            min(bulletMaxAge+Frames.InSeconds(0.2f), highestBulletMaxAge)
        )
    } // upgrade

    fun reset() {
        reset(initialRepeatDelay, initialBulletMaxAge)
    } // reset

    private fun reset(newRepeat: Int, newMaxAge: Float) {
        repeatDelay = newRepeat
        bulletMaxAge = newMaxAge
        trigger.reset(repeatDelay)
    } // update

    companion object {
        private const val initialRepeatDelay: Int = 12
        private val initialBulletMaxAge = Frames.InSeconds(1.5f)

        private const val shortestRepeatDelay: Int = 7
        private val highestBulletMaxAge = Frames.InSeconds(3)
    }
} // Gun