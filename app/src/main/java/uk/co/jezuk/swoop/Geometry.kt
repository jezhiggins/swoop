package uk.co.jezuk.swoop

fun angleFromOffsets(offsetX: Float, offsetY: Float): Float {
    return angleFromOffsets(offsetX.toDouble(), offsetY.toDouble())
}

fun angleFromOffsets(offsetX: Double, offsetY: Double): Float {
    var desiredAngle = Math.toDegrees(
        Math.atan(Math.abs(offsetY)/Math.abs(offsetX))
    ).toFloat()

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
}

fun magnitudeFromOffsets(offsetX: Float, offsetY: Float): Float {
    return magnitudeFromOffsets(offsetX.toDouble(), offsetY.toDouble())
}
fun magnitudeFromOffsets(offsetX: Double, offsetY: Double): Float {
    return Math.sqrt(
       Math.pow(offsetX, 2.0) + Math.pow(offsetY, 2.0)
   ).toFloat()
}