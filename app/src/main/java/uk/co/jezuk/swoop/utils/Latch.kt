package uk.co.jezuk.swoop.utils

import uk.co.jezuk.swoop.Frames
import kotlin.math.max
import kotlin.time.Duration

class Latch(
    startAfter: Duration,
    private val action: () -> Unit
) {
    private var count: Float = Frames.In(startAfter)
    private val done get() = count <= 0f
    val running get() = !done

    fun tick(frameRateScale: Float): Int {
        if (done) return 0

        count -= frameRateScale
        if (done) action()
        return max(count.toInt(), 0)
    } // tick
} // class Latch