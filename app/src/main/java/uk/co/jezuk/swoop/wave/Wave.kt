package uk.co.jezuk.swoop.wave

import android.graphics.Canvas

interface Wave {
    fun onSingleTapUp() = Unit
    fun onScroll(offsetX: Float, offsetY: Float) = Unit
    fun onLongPress() = Unit

    /////
    fun update(fps: Long)
    fun draw(canvas: Canvas)
} // Wave