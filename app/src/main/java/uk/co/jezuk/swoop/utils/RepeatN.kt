package uk.co.jezuk.swoop.utils

import kotlin.math.max

class RepeatN(
    private var start: Int,
    private var times: Int,
    private val action: () -> Unit
) {
    private var count = start.toFloat()

    fun tick(frameRateScale: Float): Int {
        if (times == 0) return 0

        count -= frameRateScale
        val dec = max(count.toInt(), 0)
        if (dec == 0) {
            action()
            count = start.toFloat()
            --times
        }
        return dec
    } // tick

    fun reset(newStart: Int) {
        start = newStart
    } // reset
} // class RepeatN