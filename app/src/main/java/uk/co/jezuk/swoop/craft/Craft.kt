package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import uk.co.jezuk.swoop.geometry.Point

interface Craft {
    val position: Point
    val killDist: Float

    fun update(frameRateScale: Float)
    fun draw(canvas: Canvas)

    companion object {
        fun collision(c1: Craft, c2: Craft): Boolean =
            c1.position.distance(c2.position) < (c1.killDist + c2.killDist)
    } // companion object
} // Craft