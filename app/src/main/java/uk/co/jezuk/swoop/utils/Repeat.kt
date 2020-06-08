package uk.co.jezuk.swoop.utils

import kotlin.math.max

class Repeat(
    private val start: Int,
    private val action: () -> Unit
) {
    private var count = start.toFloat()

    fun tick(frameRateScale: Float): Int {
        count -= frameRateScale
        val dec = max(count.toInt(), 0)
        if (dec == 0) {
            action()
            count = start.toFloat()
        }
        return dec
    } // tick
} // class Latch