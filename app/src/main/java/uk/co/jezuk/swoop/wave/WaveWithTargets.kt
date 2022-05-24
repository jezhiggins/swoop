package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.craft.Target
import uk.co.jezuk.swoop.craft.Targets

abstract class WaveWithTargets(
    protected val targets: Targets = Targets()
): Wave {
    protected fun updateTargets(frameRateScale: Float) {
        targets.update(frameRateScale)
    } // updateTargets

    protected fun checkPlayerCollision(player: Player) {
        targets.checkPlayerCollision(player)
    } // checkShipCollision

    protected fun drawTargets(canvas: Canvas) {
        targets.draw(canvas)
    } // drawTargets

    override fun addTarget(target: Target) {
        targets.add(target)
    } // addTarget
    override fun removeTarget(target: Target) {
        targets.remove(target)
    } // removeTarget
} // WaveWithTargets