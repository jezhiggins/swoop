package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

class Ship(private val sounds: Sounds) {
    private val shipPath = Path()
    private val shape = floatArrayOf(
        50f, 0f, -50f, 25f,
        -30f, 0f, -50f, 25f,
        -30f, 0f, -50f, -25f,
        50f, 0f, -50f, -25f
    )
    private val thruster = floatArrayOf(
        -40f, 15f, -70f, 0f,
        -70f, 0f, -40f, -15f,
        -45f, 12f, -75f, 0f,
        -75f, 0f, -45f, -12f
    )
    private var explodeShape = shape.copyOf()

    private val shipBrush = Paint()
    private val shipFillBrush = Paint()
    private val thrustBrush = Paint()
    private val explodeBrush = Paint()

    private var rotation = -90.0
    private var targetRotation = rotation

    private var thrustFrames = 0
    private val thrustSound = sounds.load(R.raw.thrust)

    private var explodeFrames = 0

    private val velocity = Vector(0.0, 0.0)
    val position = Point(0.0, 0.0)

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

        explodeBrush.setARGB(175, 255, 255, 0)
        explodeBrush.strokeWidth = 10f
        explodeBrush.strokeCap = Paint.Cap.ROUND
        explodeBrush.style = Paint.Style.STROKE

        thrustBrush.setARGB(100, 255, 215, 0)
        thrustBrush.strokeWidth = 5f

        reset()
    } // init

    private fun reset() {
        rotation = -90.0
        targetRotation = rotation
        velocity.reset()
        position.reset()
    } // reset

    fun thrust() {
        if (explodeFrames != 0) return

        val thrust = Vector(2.0, rotation)
        velocity += thrust

        thrustFrames = 10
        sounds.play(thrustSound)
    } // thrust

    fun rotateTowards(angle: Double) {
        if (explodeFrames != 0) return

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
            direction
        }

        rotation += rotationDelta
        if (rotation > 180) rotation -= 360
        if (rotation < -180) rotation += 360
    } // rotateShip

    private fun applyThrust(width: Int, height: Int) {
        position.move(velocity, width, height)
        if (thrustFrames > 0) --thrustFrames
    } // applyThrust

    private fun blowUpShip() {
        for (l in 0 until explodeShape.size step 4) {
            val x = blowUpShift(explodeShape[l])
            val y = blowUpShift(explodeShape[l + 3])

            for (p in 0 until 4 step 2) {
                explodeShape[l + p] += x
                explodeShape[l + 1 + p] += y
            }
        }

        --explodeFrames
        if (explodeFrames == 0) {
            reset()
        }
    } // blowUpShip

    private fun blowUpShift(p: Float): Float {
        if (p < 0) return -5f
        if (p > 0) return 5f
        return 0f
    } // blowUpShift

    fun update(fps: Long, width: Int, height: Int) {
        rotateShip()

        if (explodeFrames != 0) {
            blowUpShip()
        }

        applyThrust(width, height)
    } // update

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(
            position.x.toFloat(),
            position.y.toFloat()
        )
        canvas.translate(canvas.width/2f, canvas.height/2f)
        canvas.rotate(rotation.toFloat())

        if (explodeFrames == 0) {
            canvas.drawPath(shipPath, shipFillBrush)
            canvas.drawLines(shape, shipBrush)
        } else {
            canvas.drawLines(explodeShape, explodeBrush)
        }

        if (thrustFrames != 0)
            canvas.drawLines(thruster, thrustBrush)

        canvas.restore()
    } // draw

    fun explode() {
        if (explodeFrames != 0) return

        explodeFrames = 50
        thrustFrames = 0

        for (l in 0 until shape.size)
            explodeShape[l] = shape[l]
    } // explode

    val killDist: Float
        get() = 25f;
} // Ship