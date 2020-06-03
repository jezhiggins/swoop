package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Paint

class StarField (width: Int, height: Int, count: Int = 400) {
    private val stars = FloatArray(count)

    init {
        for (i in 0 until stars.size step 2) {
            val x = Math.random() * width
            val y = Math.random() * height
            stars[i] = x.toFloat()
            stars[i+1] = y.toFloat()
        }
    } // init

    fun draw(canvas: Canvas) {
        canvas.drawPoints(stars, starBrush)
    } // draw

    companion object {
        private val starBrush = Paint()

        init {
            starBrush.setARGB(255, 255, 255, 255)
            starBrush.strokeWidth = 4f
        }
    } // companion object
} // StarField