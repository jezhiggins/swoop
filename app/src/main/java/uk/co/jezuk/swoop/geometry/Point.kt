package uk.co.jezuk.swoop.geometry

data class Point(var x: Double, var y: Double) {
    fun move(vec: Vector, width: Int, height: Int) {
        val (deltaX, deltaY) = vec.offset
        x += deltaX.toFloat()
        y += deltaY.toFloat()

        val halfWidth = width / 2.0
        if (x < -halfWidth) x = halfWidth
        if (x > halfWidth) x = -halfWidth
        val halfHeight = height / 2.0
        if (y < -halfHeight) y = halfHeight
        if (y > halfHeight) y = -halfHeight
    } // move

    fun distance(pos: Point): Float {
        val offsetX = distanceBetween(x, pos.x)
        val offsetY = distanceBetween(y, pos.y)

        return magnitudeFromOffsets(offsetX, offsetY).toFloat()
    } // distance

    fun reset() {
        x = 0.0
        y = 0.0
    } // reset
} // Point