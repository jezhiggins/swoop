package uk.co.jezuk.swoop.geometry

import android.graphics.Canvas

class Rotation(
    initial: Double
) {
    var angle: Double = wraparound(initial)

    fun reset(a: Double) {
        angle = a
    } // reset

    operator fun plusAssign(delta: Double) {
        angle += delta

        angle = wraparound(angle + delta)
    } // plusAssign

    operator fun minus(rhs: Rotation): Rotation {
        return Rotation(angle - rhs.angle)
    } // minus

    fun clone() = Rotation(angle)

    fun rotate(canvas: Canvas) =
        canvas.rotate(angle.toFloat())

    private fun wraparound(a: Double): Double {
        if (a > 180) return a-360
        if (a < -180) return a+360
        return a
    }
} // Rotation