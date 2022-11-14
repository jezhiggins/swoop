package uk.co.jezuk.swoop

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import kotlin.math.abs

class Sounds(private val context: Context) {
    private val pool = SoundPool.Builder()
        .setMaxStreams(20)
        .setAudioAttributes(soundAttrs())
        .build()
    private val loadedSounds = mutableMapOf<Int, (Float) -> Unit>()

    fun load(soundResId: Int): (Float) -> Unit {
        if (loadedSounds.containsKey(soundResId))
            return loadedSounds[soundResId]!!
        val loadedId = pool.load(context, soundResId, 1)
        val playFn = { balance: Float -> play(loadedId, balance) }
        loadedSounds[soundResId] = playFn
        return playFn
    } // load

    private fun play(soundId: Int, balance: Float) {
        val shift = 1f - abs(balance)
        val leftVolume = if (balance < 0) 1f else shift
        val rightVolume = if (balance > 0) 1f else shift

        pool.play(soundId, leftVolume, rightVolume, 1, 0, 1f)
    } // play

    companion object {
        private fun soundAttrs(): AudioAttributes {
            return AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        } // soundAttrs
    } // companion
} // Sounds