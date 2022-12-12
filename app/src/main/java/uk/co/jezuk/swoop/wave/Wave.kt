package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.Players
import uk.co.jezuk.swoop.craft.Projectile
import uk.co.jezuk.swoop.craft.Target

interface Wave {
    val players: Players get() = Players()

    fun onTap(x: Float, y: Float) = Unit
    fun onMove(x: Float, y: Float, offsetX: Float, offsetY: Float) = Unit

    /////
    fun update(frameRateScale: Float)
    fun draw(canvas: Canvas)

    /////
    fun addTarget(target: Target) = Unit
    fun removeTarget(target: Target) = Unit

    /////
    fun addProjectile(projectile: Projectile) = Unit
    fun removeProjectile(projectile: Projectile) = Unit
} // Wave
