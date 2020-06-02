package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint

class Debug {
    private var textPen = Paint()
    private var fps: Long = 0

    init {
        textPen.setARGB(127, 255, 255, 255)
        textPen.textSize = 32f
    }

    fun update(fps: Long) {
        this.fps = fps
    } // update

    fun draw(canvas: Canvas) {
        canvas.drawText("FPS: ${this.fps}", 100f, 100f, textPen)
        canvas.drawText("Accel: ${canvas.isHardwareAccelerated}", 100f, 130f, textPen)
    }
}