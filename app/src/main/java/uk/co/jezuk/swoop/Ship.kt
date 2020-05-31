package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint

class Ship {
    val shape = floatArrayOf(
        -50f, 0f, 50f, 25f, 50f, 25f, 50f, -25f, 50f, -25f, -50f, 0f
    )
    val colour = Paint()
    var angle = 0f
    val speed = 0f

    init {
        colour.setARGB(100, 0, 255, 0)
        colour.strokeWidth = 5f
    }

    fun update(fps: Long) {
        angle += 360f/fps
    }

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(canvas.width/2f, canvas.height/2f)
        canvas.rotate(angle)

        canvas.drawLines(shape, colour)

        canvas.restore()
    }
}