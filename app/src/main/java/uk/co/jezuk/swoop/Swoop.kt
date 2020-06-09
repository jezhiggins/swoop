package uk.co.jezuk.swoop

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import uk.co.jezuk.swoop.databinding.ActivityFullscreenBinding

class Swoop : AppCompatActivity() {
    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var gameView: GameView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volumeControlStream = AudioManager.STREAM_MUSIC

        gameView = binding.fullscreenContent

        binding.root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onPause() {
        super.onPause()

        gameView.pause()
    } // onPause

    override fun onResume() {
        super.onResume()

        gameView.resume()
        hideHandler.postDelayed(hideRunnable, 100)
    } // onResume

    private val hideHandler = Handler()
    @SuppressLint("InlinedApi")
    private val hideRunnable = Runnable {
        binding.root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

}