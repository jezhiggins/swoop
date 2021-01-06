package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.utils.Latch

class Exploder(
    action: () -> Unit,
    shape: FloatArray,
    private val brush: Paint,
    length: Int,
    useMid: Boolean = true,
    private val expansion: Float = 5f
) {
    private val explodingShape = shape.copyOf()
    private val lifetime = Latch(length, action)
    private val offset2nd = if (useMid) 2 else 0

    fun update(frameRateScale: Float) {
        expand(frameRateScale)
        lifetime.tick(frameRateScale)
    }

    fun draw(canvas: Canvas) {
        canvas.drawLines(explodingShape, brush)
    }

    private fun expand(frameRateScale: Float) {
        for (l in 0 until explodingShape.size step 4) {
            val x = blowUpShift(explodingShape[l], explodingShape[l + offset2nd], frameRateScale)
            val y = blowUpShift(explodingShape[l + 3], explodingShape[l + 3 - offset2nd], frameRateScale)

            for (p in 0 until 4 step 2) {
                explodingShape[l + p] += x
                explodingShape[l + 1 + p] += y
            }
        }
    } // expand

    private fun blowUpShift(p1: Float, p2: Float, frameRateScale: Float): Float {
        val p = (p1 + p2) / 2.0
        if (p < 0) return -expansion * frameRateScale
        if (p > 0) return expansion * frameRateScale
        return 0f
    } // blowUpShift
}
