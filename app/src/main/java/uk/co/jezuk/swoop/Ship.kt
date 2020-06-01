package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.cos
import kotlin.math.sin

class Ship {
    private val shape = floatArrayOf(
        50f, 0f, -50f, 25f,
        -50f, 25f, -30f, 0f,
        -30f, 0f, -50f, -25f,
        -50f, -25f, 50f, 0f
    )
    private val thruster = floatArrayOf(
        -40f, 15f, -70f, 0f,
        -70f, 0f, -40f, -15f,
        -45f, 12f, -75f, 0f,
        -75f, 0f, -45f, -12f
    )

    private val shipBrush = Paint()
    private val thrustBrush = Paint()

    private var rotation = -90f
    private var targetRotation = rotation
    private var thrustFrames = 0

    private var vectorMagnitude = 0f
    private var vectorAngle = 0.0
    private var x = 0f
    private var y = 0f

    init {
        shipBrush.setARGB(100, 0, 255, 0)
        shipBrush.strokeWidth = 10f

        thrustBrush.setARGB(100, 255, 215, 0)
        thrustBrush.strokeWidth = 5f
    } // init

    fun thrust() {
        val vectorRads = Math.toRadians(vectorAngle)
        val x1 = vectorMagnitude * sin(vectorRads)
        val y1 = vectorMagnitude * cos(vectorRads)

        var invertRotation = rotation + 180
        if (invertRotation > 180) invertRotation -= 360

        val thrustRads = Math.toRadians(invertRotation.toDouble())
        val x2 = 2 * sin(thrustRads)
        val y2 = 2 * cos(thrustRads)

        val x = x1 + x2
        val y = y1 + y2

        vectorMagnitude = magnitudeFromOffsets(x, y)
        vectorAngle = angleFromOffsets(x, y).toDouble()

        thrustFrames = 10
    } // thrust

    fun rotateTowards(angle: Float) {
        targetRotation = angle
    } // rotateTowards

    private fun rotateShip() {
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

    private fun applyThrust(width: Int, height: Int) {
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

        if (thrustFrames > 0) thrustFrames -= 1
    }

    fun update(fps: Long, width: Int, height: Int) {
        rotateShip()

        applyThrust(width, height)
    } // update

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(x, y)
        canvas.translate(canvas.width/2f, canvas.height/2f)
        canvas.rotate(rotation)

        canvas.drawLines(shape, shipBrush)
        if (thrustFrames != 0)
            canvas.drawLines(thruster, thrustBrush)

        canvas.restore()
    } // draw
} // Ship