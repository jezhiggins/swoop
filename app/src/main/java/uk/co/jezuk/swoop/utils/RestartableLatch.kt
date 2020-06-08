package uk.co.jezuk.swoop.utils

import kotlin.math.max

class RestartableLatch(
    private val start: Int,
    private val action: () -> Unit = { }
) {
    private var count = 0f
    val done get() = count <= 0f
    val running get() = !done

    fun tick(frameRateScale: Float): Int {
        if (done) return 0

        count -= frameRateScale
        if (done) action()
        return max(count.toInt(), 0)
    } // tick

    fun start() {
        count = start.toFloat()
    }
} // class RestartableLatch