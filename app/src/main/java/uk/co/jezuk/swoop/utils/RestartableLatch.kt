package uk.co.jezuk.swoop.utils

import uk.co.jezuk.swoop.Frames
import kotlin.math.max
import kotlin.time.Duration

class RestartableLatch(
    private val startAfter: Duration,
    private val action: () -> Unit = { }
) {
    private var count = 0f
    private val done get() = count <= 0f
    val running get() = !done

    fun tick(frameRateScale: Float): Int {
        if (done) return 0

        count -= frameRateScale
        if (done) action()
        return max(count.toInt(), 0)
    } // tick

    fun start() {
        count = Frames.In(startAfter)
    }
} // class RestartableLatch