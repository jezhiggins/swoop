package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.utils.Latch
import kotlin.math.min

class LevelTransition(
    private val game: Game,
    private val starField: StarField,
    private val newStarField: StarField,
    private val ship: Ship,
    private val nextWave: Wave
): Wave {
    private var transition = Latch(120, { game.nextWave(nextWave) })
    private var currentStarField = newStarField

    init {
        ship.rezOut()
    } // init

    override fun update(fps: Long) {
        ship.update(fps)

        when (transition.tick()) {
            60, 100 -> currentStarField = starField
            75, 50 -> currentStarField = newStarField
        }
    } // update

    override fun draw(canvas: Canvas) {
        currentStarField.draw(canvas)
        ship.draw(canvas)
    } // draw
} // Emptiness