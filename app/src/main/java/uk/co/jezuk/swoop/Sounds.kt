package uk.co.jezuk.swoop

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class Sounds(private val context: Context) {
    private val pool = SoundPool.Builder()
        .setMaxStreams(10)
        .setAudioAttributes(soundAttrs())
        .build()
    private val loadedSounds = mutableMapOf<Int, Int>()

    private val pop = load(R.raw.pop)

    fun pop() { play(pop) }

    fun load(soundResId: Int): Int {
        if (loadedSounds.containsKey(soundResId))
            return loadedSounds[soundResId]!!
        val loadedId = pool.load(context, soundResId, 1)
        loadedSounds.put(soundResId, loadedId)
        return loadedId
    } // load

    fun play(soundId: Int) {
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