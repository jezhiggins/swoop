package uk.co.jezuk.swoop

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class HighScore(private val context: Context) {
    enum class Mode(val tag: String) {
        Pure("purehighscore"),
        Restart("restarthighscore")
    }

    private val prefs: SharedPreferences
        get() = context.getSharedPreferences("swoop", Context.MODE_PRIVATE)

    fun pure(): Int = prefs.getInt(Mode.Pure.tag, 0)
    fun restart(): Int = prefs.getInt(Mode.Restart.tag, 0)

    fun tracker(mode: Mode) = Tracker(mode, prefs)

    class Tracker(private val mode: Mode, private val prefs: SharedPreferences) {
        fun isHighScore(score: Int): Boolean {
            if (score < highScore) return false
            highScore = score
            return true
        }

        private var highScore: Int
            get() = prefs.getInt(mode.tag, 0)
            private set(value) = prefs.edit { putInt(mode.tag, value) }
    }
}
