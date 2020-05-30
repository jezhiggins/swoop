package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: GameView
) : Thread() {
    private var running: Boolean = false

    fun running() { this.running = true }
    fun stopped() { this.running = false; println("STOPPED")}

    override fun run() {
        var fps: Long = 60

        while (running) {
            val startTime = System.currentTimeMillis()

            if (surfaceHolder.surface.isValid) {
                val canvas = this.surfaceHolder.lockCanvas()

                this.gameView.update(fps)
                this.gameView.draw(canvas!!)

                surfaceHolder.unlockCanvasAndPost(canvas)

                val timeThisFrame = System.currentTimeMillis() - startTime
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame
                }
            } // if ...
        } // while ...
    } // run
} // GameThread