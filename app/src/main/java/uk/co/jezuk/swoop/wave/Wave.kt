package uk.co.jezuk.swoop.wave

import android.graphics.Canvas

interface Wave {
    fun onSingleTapUp()
    fun onScroll(offsetX: Float, offsetY: Float)
    fun onLongPress()

    /////
    fun update(fps: Long)
    fun draw(canvas: Canvas)
} // Wave