package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.geometry.Point

class Nothing(): Target {
    override val position: Point = Point.Hyperspace
    override val killDist: Float = 0f

    override fun update(frameRateScale: Float) = Unit
    override fun draw(canvas: Canvas) = Unit

    override fun playerCollision(player: Player) = Unit
    override fun shot(): Target.Effect = Target.NoEffect
    override fun explode() = Unit
}