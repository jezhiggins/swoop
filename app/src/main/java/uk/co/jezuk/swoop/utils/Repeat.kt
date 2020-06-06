package uk.co.jezuk.swoop.utils

class Repeat(
    private val start: Int,
    private val action: () -> Unit
) {
    private var count = start

    fun tick(): Int {
        val dec = --count
        if (dec == 0) {
            action()
            count = start
        }
        return dec
    } // tick
} // class Latch