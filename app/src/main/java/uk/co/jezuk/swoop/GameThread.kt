package uk.co.jezuk.swoop

import android.os.Build
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: GameView
) : Thread() {
    private var running: Boolean = false

    fun running() { this.running = true }
    fun stopped() { this.running = false }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun run() {
        var fps: Long = 60

        while(!surfaceHolder.surface.isValid) {
            sleep(100)
        }

        while (running) {
            val startTime = System.nanoTime() / 1000000

            this.gameView.update(fps)

            val canvas = this.surfaceHolder.lockHardwareCanvas()
            this.gameView.draw(canvas)
            surfaceHolder.unlockCanvasAndPost(canvas)

            val timeThisFrame = (System.nanoTime() / 1000000) - startTime
            if (timeThisFrame > 1) {
                fps = 1000 / timeThisFrame
            }
        } // while ...
    } // run
} // GameThread