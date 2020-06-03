package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Ship
import kotlin.math.min

class LevelTransition(
    private val game: Game,
    private val starField: StarField,
    private val ship: Ship,
    private val initialAsteroids: Int
): Wave {
    private var transition = 120
    private val newStarField = StarField(game.extent)
    private var currentStarField = newStarField

    init {
        ship.rezOut()
    }

    override fun onSingleTapUp() = Unit
    override fun onScroll(offsetX: Float, offsetY: Float) = Unit
    override fun onLongPress() = Unit

    override fun update(fps: Long) {
        ship.update(fps)

        if (--transition == 0)
            game.nextWave(FlyAround(game, newStarField, min(initialAsteroids+1, 11)))

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