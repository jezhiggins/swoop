package uk.co.jezuk.swoop.utils

class Latch(
    private var count: Int,
    private val action: () -> Unit
) {
    val done get() = count == 0
    val running get() = !done

    fun tick(): Int {
        if (done) return 0

        if (--count == 0) action()
        return count
    } // tick
} // class Latch