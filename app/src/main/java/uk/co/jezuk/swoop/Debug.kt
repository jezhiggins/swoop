package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Debug {
    private var textPen = Paint()
    private var updates: Long = 0
    private var cumulativeScale: Float = 0f

    private var avg: Int = 0

    init {
        textPen.color = Color.WHITE
        textPen.alpha =127
        textPen.textSize = 32f
    }

    fun update(frameRateScale: Float) {
        cumulativeScale += frameRateScale
        ++updates

        avg = (50 / (cumulativeScale/updates)).toInt()
    } // update

    fun draw(canvas: Canvas) {
        canvas.drawText("FPS: ${avg}", 100f, 100f, textPen)
        canvas.drawText("Accel: ${canvas.isHardwareAccelerated}", 100f, 130f, textPen)
    }
}