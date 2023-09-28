package uk.co.jezuk.swoop.utils

import uk.co.jezuk.swoop.Frames
import kotlin.math.max
import kotlin.time.Duration

class Repeat(
    private var startAfter: Duration,
    private val action: () -> Unit
) {
    private var count = Frames.In(startAfter)

    fun tick(frameRateScale: Float): Int {
        count -= frameRateScale
        val dec = max(count.toInt(), 0)
        if (dec == 0) {
            action()
            count = Frames.In(startAfter)
        }
        return dec
    } // tick

    fun reset(newStart: Duration) {
        startAfter = newStart
    } // reset
} // class Repeat