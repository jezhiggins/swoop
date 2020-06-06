package uk.co.jezuk.swoop.utils

class RestartableLatch(
    private val start: Int,
    private val action: () -> Unit = { }
) {
    private var count = 0
    val done get() = count == 0
    val running get() = !done

    fun tick(): Int {
        if (done) return 0

        if (--count == 0) action()
        return count
    } // tick

    fun start() {
        count = start
    }
} // class RestartableLatch