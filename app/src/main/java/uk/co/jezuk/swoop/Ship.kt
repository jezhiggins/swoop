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
    var rotation = -90f
    var targetRotation = rotation

    var rotationDelta = 0f
    var vectorMagnitude = (Math.random() * 10).toFloat()
    var vectorAngle = Math.random() * 360
    var x = 0f
    var y = 0f

    init {
        colour.setARGB(100, 0, 255, 0)
        colour.strokeWidth = 10f
    } // init

    fun rotateTowards(angle: Float) {
        targetRotation = angle
    } // rotateTowards

    fun rotateShip() {
        var angleOffset = targetRotation - rotation

        if (angleOffset > 180) angleOffset -= 360
        if (angleOffset < -180) angleOffset += 360

        val direction = if (angleOffset >= 0) 1f else -1f
        val magnitude = Math.abs(angleOffset)
        val rotationDelta = if (magnitude > 30) {
            direction * 5
        } else if (magnitude > 3) {
            direction * 2
        } else {
            angleOffset
        }

        rotation += rotationDelta
        if (rotation > 180) rotation -= 360
        if (rotation < -180) rotation += 360
    } // rotateShip

    fun update(fps: Long, width: Int, height: Int) {
        rotateShip()

        /*
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
        */
    } // update

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(x, y)
        canvas.translate(canvas.width/2f, canvas.height/2f)
        canvas.rotate(rotation)

        canvas.drawLines(shape, colour)

        canvas.restore()
    } // draw
} // Ship