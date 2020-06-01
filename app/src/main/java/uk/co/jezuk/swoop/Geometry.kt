package uk.co.jezuk.swoop

fun angleFromOffsets(offsetX: Float, offsetY: Float): Double {
    return angleFromOffsets(offsetX.toDouble(), offsetY.toDouble())
} // angleFromOffsets

fun angleFromOffsets(offsetX: Double, offsetY: Double): Double {
    var desiredAngle = Math.toDegrees(
        Math.atan(Math.abs(offsetY)/Math.abs(offsetX))
    )

    if (offsetX <= 0 && offsetY <= 0) {
    }
    if (offsetX <= 0 && offsetY > 0) {
        desiredAngle = -desiredAngle
    }
    if (offsetX > 0 && offsetY <= 0) {
        desiredAngle = 180 - desiredAngle
    }
    if (offsetX > 0 && offsetY > 0) {
        desiredAngle = -180 + desiredAngle
    }
    return desiredAngle
} // angleFromOffsets

fun magnitudeFromOffsets(offsetX: Double, offsetY: Double): Double {
    return Math.sqrt(
       Math.pow(offsetX, 2.0) + Math.pow(offsetY, 2.0)
   )
} // magnitudeFromOffsets

fun invertAngle(angle: Double): Double {
    val thrustAngle = angle + 180
    return if (thrustAngle > 180) thrustAngle - 360 else thrustAngle
} // invertAngle