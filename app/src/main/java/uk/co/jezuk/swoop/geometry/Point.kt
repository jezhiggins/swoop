package uk.co.jezuk.swoop.geometry

import android.graphics.Canvas
import android.graphics.Paint

data class Point(
    var x: Double,
    var y: Double
) {
    constructor(p: Point): this(p.x, p.y)

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

    fun moveTo(target: Point) {
        x = target.x
        y = target.y
    } // moveTo

    fun distance(pos: Point): Float {
        val offsetX = distanceBetween(x, pos.x)
        val offsetY = distanceBetween(y, pos.y)

        return magnitudeFromOffsets(offsetX, offsetY).toFloat()
    } // distance

    fun angleTo(pos: Point): Double {
        val offsetX = x - pos.x
        val offsetY = y - pos.y

        return angleFromOffsets(offsetX, offsetY)
    } // angleTo

    fun translate(canvas: Canvas) =
        canvas.translate(x.toFloat(), y.toFloat())

    fun draw(canvas: Canvas, brush: Paint) =
        canvas.drawPoint(x.toFloat(), y.toFloat(), brush)

    fun reset() {
        x = 0.0
        y = 0.0
    } // reset

    companion object {
        val Hyperspace = Point(-100000.0, -100000.0)
    }
} // Point