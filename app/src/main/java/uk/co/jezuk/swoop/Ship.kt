package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min

class Ship(private val sounds: Sounds) {
    private val shipPath = Path()
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
    private val shipFillBrush = Paint()
    private val thrustBrush = Paint()

    private var rotation = -90.0
    private var targetRotation = rotation
    private var thrustFrames = 0

    private val thrustSound = sounds.load(R.raw.thrust)

    private var velocity = Vector(0.0, 0.0)
    private var x = 0f
    private var y = 0f

    init {
        shipPath.moveTo(shape[0], shape[1])
        for (i in 0 until shape.size step 2)
            shipPath.lineTo(shape[i], shape[i+1])
        shipPath.close()

        shipBrush.setARGB(175, 0, 255, 0)
        shipBrush.strokeWidth = 10f
        shipBrush.strokeCap = Paint.Cap.ROUND
        shipBrush.style = Paint.Style.STROKE

        shipFillBrush.setARGB(255, 0, 0, 0)
        shipBrush.strokeWidth = 10f
        shipFillBrush.style = Paint.Style.FILL_AND_STROKE

        thrustBrush.setARGB(100, 255, 215, 0)
        thrustBrush.strokeWidth = 5f
    } // init

    fun thrust() {
        val thrust = Vector(2.0, rotation)
        velocity += thrust

        thrustFrames = 10
        sounds.play(thrustSound)
    } // thrust

    fun rotateTowards(angle: Double) {
        targetRotation = angle
    } // rotateTowards

    private fun rotateShip() {
        var angleOffset = targetRotation - rotation

        if (angleOffset > 180) angleOffset -= 360
        if (angleOffset < -180) angleOffset += 360

        val direction = if (angleOffset >= 0) 1.0 else -1.0
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
        val (deltaX, deltaY) = velocity.offset

        x += deltaX.toFloat()
        y += deltaY.toFloat()

        val halfWidth = width / 2f
        if (x < -halfWidth) x = halfWidth
        if (x > halfWidth) x = -halfWidth
        val halfHeight = height / 2f
        if (y < -halfHeight) y = halfHeight
        if (y > halfHeight) y = -halfHeight

        if (thrustFrames > 0) thrustFrames -= 1
    } // applyThrust

    fun update(fps: Long, width: Int, height: Int) {
        rotateShip()

        applyThrust(width, height)
    } // update

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(x, y)
        canvas.translate(canvas.width/2f, canvas.height/2f)
        canvas.rotate(rotation.toFloat())

        canvas.drawPath(shipPath, shipFillBrush)
        canvas.drawLines(shape, shipBrush)

        if (thrustFrames != 0)
            canvas.drawLines(thruster, thrustBrush)
        canvas.restore()
    } // draw
} // Ship