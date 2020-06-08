package uk.co.jezuk.swoop.geometry

data class Point(var x: Double, var y: Double) {
    fun move(
        vec: Vector,
        frameScaleRate: Float,
        fieldBounds: Extent,
        killDist: Float = 0f
    ) {
        val (deltaX, deltaY) = vec.offset(frameScaleRate)
        x += deltaX.toFloat()
        y += deltaY.toFloat()

        val bounds = fieldBounds.inflated(killDist)
        if (x < bounds.left) x = bounds.right
        if (x > bounds.right) x = bounds.left
        if (y < bounds.top) y = bounds.bottom
        if (y > bounds.bottom) y = bounds.top
    } // move

    fun moveNoWrap(
        vec: Vector,
        frameRateScale: Float,
        fieldBounds: Extent,
        killDist: Float = 0f
    ): Boolean {
        val (deltaX, deltaY) = vec.offset(frameRateScale)
        x += deltaX.toFloat()
        y += deltaY.toFloat()

        val bounds = fieldBounds.inflated(killDist)
        return (x >= bounds.left) && (x <= bounds.right)
                && (y >= bounds.top) && (y <= bounds.bottom)
    } // moveNoWrap

    fun distance(pos: Point): Float {
        val offsetX = distanceBetween(x, pos.x)
        val offsetY = distanceBetween(y, pos.y)

        return magnitudeFromOffsets(offsetX, offsetY).toFloat()
    } // distance

    fun pan(extent: Extent): Float {
        val panned = x.toFloat() / extent.canvasOffsetX
        return panned
    } // pan

    fun reset() {
        x = 0.0
        y = 0.0
    } // reset
} // Point