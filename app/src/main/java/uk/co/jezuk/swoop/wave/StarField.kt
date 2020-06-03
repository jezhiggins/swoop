package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Paint

class StarField (count: Int = 400) {
    private val stars = FloatArray(count)
    private val starBrush = Paint()

    init {
        starBrush.setARGB(255, 255, 255, 255)
        starBrush.strokeWidth = 4f
    }

    fun size(width: Int, height: Int) {
        for (i in 0 until stars.size step 2) {
            val x = Math.random() * width
            val y = Math.random() * height
            stars[i] = x.toFloat()
            stars[i+1] = y.toFloat()
        }
    } // size

    fun draw(canvas: Canvas) {
        canvas.drawPoints(stars, starBrush)
    }
} // StarField