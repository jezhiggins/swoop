package uk.co.jezuk.swoop

class Frames {
    private var fps: Long = 50
    private var startTime: Long = 0

    val scaling get() = TargetFPS / fps

    fun time(): Timer {
        return Timer(this)
    }
    fun start() {
        startTime = System.nanoTime() / 1000000
    }
    fun stop() {
        val timeThisFrame = (System.nanoTime() / 1000000) - startTime
        if (timeThisFrame > 1) {
            fps = 1000 / timeThisFrame
        }
    }

    companion object {
        const val TargetFPS = 50f;

        fun InSeconds(seconds: Float) = (TargetFPS * seconds)
        fun InSeconds(seconds: Int) = InSeconds(seconds.toFloat())
    }

    class Timer(
        private val frameTimer: Frames
    ): AutoCloseable {
        init {
            frameTimer.start()
        }

        override fun close() {
            frameTimer.stop()
        }
    }

}