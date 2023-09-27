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
        val frameTimer = Frames()

        while(!surfaceHolder.surface.isValid) {
            sleep(100)
        }

        while (running) {
            frameTimer.time().use {
                this.gameView.update(frameTimer.scaling)

                val canvas = this.surfaceHolder.lockHardwareCanvas()
                this.gameView.draw(canvas)
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        } // while ...
    } // run
} // GameThread