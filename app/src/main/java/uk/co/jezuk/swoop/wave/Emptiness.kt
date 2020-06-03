package uk.co.jezuk.swoop.wave

import android.graphics.Canvas

class Emptiness: Wave {
    override fun onSingleTapUp() = Unit
    override fun onScroll(offsetX: Float, offsetY: Float) = Unit
    override fun onLongPress() = Unit

    override fun update(fps: Long) = Unit
    override fun draw(canvas: Canvas) = Unit
} // Emptiness