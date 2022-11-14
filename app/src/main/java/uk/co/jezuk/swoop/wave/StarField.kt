package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.geometry.Extent

class StarField (extent: Extent, count: Int = 400) {
    private val stars = FloatArray(count)

    init {
        for (i in stars.indices step 2) {
            val p = extent.randomPoint()
            stars[i] = p.x.toFloat()
            stars[i+1] = p.y.toFloat()
        }
    } // init

    fun draw(canvas: Canvas) {
        canvas.drawPoints(stars, starBrush)
    } // draw

    companion object {
        private val starBrush = Paint()

        init {
            starBrush.color = Color.WHITE
            starBrush.alpha = 255
            starBrush.strokeWidth = 4f
        }
    } // companion object
} // StarField