package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Projectiles
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.utils.Latch
import kotlin.math.min

class LevelTransition(
    private val game: Game,
    private val starField: StarField,
    private val newStarField: StarField,
    private val ship: Ship,
    private val projectiles: Projectiles,
    private val nextWave: Wave
): Wave {
    private var transition = Latch(180, { startNextWave() })
    private var currentStarField = starField

    override fun update(frameRateScale: Float) {
        ship.update(frameRateScale)
        projectiles.update(frameRateScale)

        when (transition.tick(frameRateScale)) {
            120 -> ship.rezOut()
            40, 60, 100 -> currentStarField = newStarField
            50, 75 -> currentStarField = starField
        }
    } // update

    override fun draw(canvas: Canvas) {
        currentStarField.draw(canvas)
        if (currentStarField == starField)
          projectiles.draw(canvas)

        ship.draw(canvas)
    } // draw

    private fun startNextWave() {
        game.lifeGained()
        game.nextWave(nextWave)
    } // startNextWave
} // Emptiness