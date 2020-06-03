package uk.co.jezuk.swoop

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

typealias PlaySound = (Int) -> Unit

class Sounds(private val context: Context) {
    private val pool = SoundPool.Builder()
        .setMaxStreams(10)
        .setAudioAttributes(soundAttrs())
        .build()
    private val loadedSounds = mutableMapOf<Int, PlaySound>()

    fun load(soundResId: Int): PlaySound {
        if (loadedSounds.containsKey(soundResId))
            return loadedSounds[soundResId]!!
        val loadedId = pool.load(context, soundResId, 1)
        val playFn = { balance: Int -> play(loadedId, balance) }
        loadedSounds.put(soundResId, playFn)
        return playFn
    } // load

    fun play(soundId: Int, balance: Int) {
        pool.play(soundId, 1f, 1f, 1, 0, 1f)
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