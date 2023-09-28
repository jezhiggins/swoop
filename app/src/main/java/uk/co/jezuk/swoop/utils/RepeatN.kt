package uk.co.jezuk.swoop.utils

import uk.co.jezuk.swoop.Frames
import kotlin.math.max
import kotlin.time.Duration

class RepeatN(
    private var startAfter: Duration,
    private var times: Int,
    private val action: () -> Unit
) {
    private var count = Frames.In(startAfter)

    fun tick(frameRateScale: Float): Int {
        if (times == 0) return 0

        count -= frameRateScale
        val dec = max(count.toInt(), 0)
        if (dec == 0) {
            action()
            count = Frames.In(startAfter)
            --times
        }
        return dec
    } // tick

    fun reset(newStart: Duration) {
        startAfter = newStart
    } // reset
} // class RepeatN