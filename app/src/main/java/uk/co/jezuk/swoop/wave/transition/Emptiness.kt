package uk.co.jezuk.swoop.wave.transition

import android.graphics.Canvas
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.wave.Wave

class Emptiness: Wave {
    override fun update(frameRateScale: Float) = Unit
    override fun draw(canvas: Canvas) = Unit
} // Emptiness