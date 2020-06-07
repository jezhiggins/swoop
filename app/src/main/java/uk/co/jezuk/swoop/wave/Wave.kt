package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.craft.Target

interface Wave {
    fun onSingleTapUp() = Unit
    fun onScroll(offsetX: Float, offsetY: Float) = Unit
    fun onLongPress() = Unit

    /////
    fun update(fps: Long)
    fun draw(canvas: Canvas)

    /////
    fun addTarget(target: Target) = Unit
    fun removeTarget(target: Target) = Unit
} // Wave