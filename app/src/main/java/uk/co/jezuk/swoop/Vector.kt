package uk.co.jezuk.swoop

import java.lang.Math.cos
import java.lang.Math.sin
import kotlin.math.min

data class Point(val x: Double, val y: Double)

class Vector(
    private var magnitude: Double,
    private var direction: Double
) {
    private var directionRad = Math.toRadians(direction)

    var angle: Double
        get() = this.direction
        set(value) {
            this.direction = value
            this.directionRad = Math.toRadians(value)
        }

    val offset: Point
        get() = Point(
            magnitude * cos(directionRad),
            magnitude * sin(directionRad)
        )

    operator fun plusAssign(vec2: Vector): Unit {
        val (x1, y1) = offset
        val (x2, y2) = vec2.offset

        val x = x1 + x2
        val y = y1 + y2

        magnitude = min(magnitudeFromOffsets(x, y), 20.0)
        angle = invertAngle(angleFromOffsets(x, y))
    }
} // class Vector