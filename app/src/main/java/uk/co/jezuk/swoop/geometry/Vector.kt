package uk.co.jezuk.swoop.geometry

import java.lang.Math.cos
import java.lang.Math.sin
import kotlin.math.max
import kotlin.math.min

class Vector(
    private var magnitude: Double,
    private var direction: Double,
    var maximum: Double = 20.0
) {
    private var directionRad = Math.toRadians(direction)

    fun copy() = Vector(magnitude, direction)

    var angle: Double
        get() = this.direction
        set(value) {
            this.direction = value
            this.directionRad = Math.toRadians(value)
        }

    fun offset(frameRateScale: Float) =
        Point(
            magnitude * cos(directionRad) * frameRateScale,
            magnitude * sin(directionRad) * frameRateScale
        )

    operator fun plusAssign(vec2: Vector) {
        val (x1, y1) = offset(1f)
        val (x2, y2) = vec2.offset(1f)

        val x = x1 + x2
        val y = y1 + y2

        magnitude = min(magnitudeFromOffsets(x, y), maximum)
        angle = invertAngle(
            angleFromOffsets(
                x,
                y
            )
        )
    } // plusAssign

    fun reset() {
        angle = 0.0
        magnitude = 0.0
    } // reset
} // class Vector