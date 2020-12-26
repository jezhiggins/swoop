package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.view.MotionEvent
import uk.co.jezuk.swoop.craft.Projectile
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.Target

interface Wave {
    val ship: Ship?

    fun onSingleTapUp(event: MotionEvent) = Unit
    fun onScroll(offsetX: Float, offsetY: Float) = Unit
    fun onLongPress() = Unit

    /////
    fun update(frameRateScale: Float)
    fun draw(canvas: Canvas)

    /////
    fun addTarget(target: Target) = Unit
    fun removeTarget(target: Target) = Unit

    /////
    fun addProjectile(projectile: Projectile) = Unit
    fun removeProjectile(projectile: Projectile) = Unit

    ////
    fun upgrade() = Unit
} // Wave
