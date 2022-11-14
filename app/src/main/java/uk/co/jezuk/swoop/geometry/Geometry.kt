package uk.co.jezuk.swoop.geometry

import kotlin.math.*

fun angleFromOffsets(offsetX: Float, offsetY: Float): Double {
    return angleFromOffsets(
        offsetX.toDouble(),
        offsetY.toDouble()
    )
} // angleFromOffsets

fun angleFromOffsets(offsetX: Double, offsetY: Double): Double {
    val desiredAngle = Math.toDegrees(
        atan(abs(offsetY) / abs(offsetX))
    )

    //if (offsetX <= 0 && offsetY <= 0)
    //    return desiredAngle

    if (offsetX <= 0 && offsetY > 0)
        return -desiredAngle

    if (offsetX > 0 && offsetY <= 0)
        return 180 - desiredAngle

    if (offsetX > 0 && offsetY > 0)
        return -180 + desiredAngle

    return desiredAngle
} // angleFromOffsets

fun magnitudeFromOffsets(offsetX: Double, offsetY: Double): Double {
    return sqrt(
       offsetX.pow(2.0) + offsetY.pow(2.0)
   )
} // magnitudeFromOffsets

fun invertAngle(angle: Double): Double {
    val thrustAngle = angle + 180
    return if (thrustAngle > 180) thrustAngle - 360 else thrustAngle
} // invertAngle

fun distanceBetween(a: Float, b: Float): Float {
    return max(a, b) - min(a, b)
}
fun distanceBetween(a: Double, b: Double): Double {
    return max(a, b) - min(a, b)
}