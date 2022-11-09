package uk.co.jezuk.swoop

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import uk.co.jezuk.swoop.databinding.ActivityFullscreenBinding

class Swoop : AppCompatActivity() {
    private lateinit var gameView: GameView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volumeControlStream = AudioManager.STREAM_MUSIC

        gameView = binding.fullscreenContent

        hideSystemUI();
    } // onCreate

    override fun onPause() {
        super.onPause()

        gameView.pause()
    } // onPause

    override fun onResume() {
        super.onResume()

        gameView.resume()
        hideHandler.postDelayed(hideRunnable, 100)
    } // onResume

    private val hideHandler = Handler(Looper.getMainLooper())
    @SuppressLint("InlinedApi")
    private val hideRunnable = Runnable {
        hideSystemUI();
    } // hideRunnable

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, gameView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

    }
} // class Swoop