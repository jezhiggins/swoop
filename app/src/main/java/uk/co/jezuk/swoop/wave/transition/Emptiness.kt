package uk.co.jezuk.swoop.wave.transition

import android.graphics.Canvas
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.wave.Wave

class Emptiness: Wave {
    override val ship: Ship? = null
    override fun update(frameRateScale: Float) = Unit
    override fun draw(canvas: Canvas) = Unit
} // Emptiness