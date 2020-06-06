package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Ship
import kotlin.math.min

class LevelTransition(
    private val game: Game,
    private val starField: StarField,
    private val newStarField: StarField,
    private val ship: Ship,
    private val nextWave: Wave
): Wave {
    private var transition = 120
    private var currentStarField = newStarField

    init {
        ship.rezOut()
    }

    override fun update(fps: Long) {
        ship.update(fps)

        if (--transition == 0)
            game.nextWave(nextWave)

        when (transition) {
            60, 100 -> currentStarField = starField
            75, 50 -> currentStarField = newStarField
        }
    } // update

    override fun draw(canvas: Canvas) {
        currentStarField.draw(canvas)
        ship.draw(canvas)
    } // draw
} // Emptiness