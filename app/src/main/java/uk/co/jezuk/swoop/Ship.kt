package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint

class Ship {
    val shape = floatArrayOf(
        50f, 0f, -50f, 25f,
        -50f, 25f, -30f, 0f,
        -30f, 0f, -50f, -25f,
        -50f, -25f, 50f, 0f
    )
    val colour = Paint()
    var rotation = 45f
    var rotationDelta = 0f
    var vectorMagnitude = (Math.random() * 10).toFloat()
    var vectorAngle = Math.random() * 360
    var x = 0f
    var y = 0f

    init {
        colour.setARGB(100, 0, 255, 0)
        colour.strokeWidth = 10f
    }

    fun move(velocityX: Float) {
        rotationDelta = velocityX*3
    }

    fun update(fps: Long, width: Int, height: Int) {
        rotation += rotationDelta/fps
        rotationDelta = 0f

        val vectorRads = Math.toRadians(vectorAngle)
        val deltaX = vectorMagnitude * Math.sin(vectorRads)
        val deltaY = vectorMagnitude * Math.cos(vectorRads)

        x += deltaX.toFloat()
        y += deltaY.toFloat()

        val halfWidth = width / 2f
        if (x < -halfWidth) x = halfWidth
        if (x > halfWidth) x = -halfWidth
        val halfHeight = height / 2f
        if (y < -halfHeight) y = halfHeight
        if (y > halfHeight) y = -halfHeight
    }

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(x, y)
        canvas.translate(canvas.width/2f, canvas.height/2f)
        canvas.rotate(rotation)

        canvas.drawLines(shape, colour)

        canvas.restore()
    }
}