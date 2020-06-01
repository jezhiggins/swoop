package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.cos
import kotlin.math.sin

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

    var vectorMagnitude = 0f
    var vectorAngle = 0.0
    var x = 0f
    var y = 0f

    init {
        colour.setARGB(100, 0, 255, 0)
        colour.strokeWidth = 10f
    } // init

    fun thrust() {
        val vectorRads = Math.toRadians(vectorAngle)
        val x1 = vectorMagnitude * sin(vectorRads)
        val y1 = vectorMagnitude * cos(vectorRads)

        val thrustRads = Math.toRadians(rotation.toDouble())
        val x2 = 10 * sin(thrustRads)
        val y2 = 10 * cos(thrustRads)

        val x = x1 + x2
        val y = y1 + y2

        vectorMagnitude = magnitudeFromOffsets(x, y)
        vectorAngle = angleFromOffsets(x, y).toDouble()
    } // thrust

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

    fun applyThrust() {
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
    }

    fun update(fps: Long, width: Int, height: Int) {
        rotateShip()

        applyThrust()
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