package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point

class Targets {
    private val targets = ArrayList<Target>()
    private val callbacks = ArrayList<() -> Unit>()

    operator fun iterator() = ArrayList(targets).iterator()
    operator fun get(index: Int) = targets[index]
    val size get() = targets.size

    fun onEliminated(callback: () -> Unit) = callbacks.add(callback)

    /////
    fun update(frameRateScale: Float) {
        iterator().forEach { t -> t.update(frameRateScale) }
    } // updateTargets

    fun checkShipCollision(ship: Ship) {
        iterator().forEach { t -> t.checkShipCollision(ship) }
    } // checkShipCollision

    fun draw(canvas: Canvas) {
        targets.forEach { t -> t.draw(canvas) }
    } // drawTargets

    /////
    fun add(target: Target) = targets.add(target)
    fun remove(target: Target) {
        targets.remove(target)
        if (targets.isEmpty())
            callbacks.forEach { c -> c() }
    } // remove
} // Asteroids
