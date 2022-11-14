package uk.co.jezuk.swoop.wave.transition

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Projectiles
import uk.co.jezuk.swoop.utils.Latch
import uk.co.jezuk.swoop.wave.StarField
import uk.co.jezuk.swoop.wave.Wave

class LevelTransition(
    private val game: Game,
    private val starField: StarField,
    private val newStarField: StarField,
    private val projectiles: Projectiles?,
    private val nextWave: Wave,
    private val waveIndex: Int
): Wave {
    override val player = game.player
    private var transition = Latch(180, { startNextWave() })
    private var currentStarField = starField

    init {
        player.gunActive = false
    }

    override fun update(frameRateScale: Float) {
        player.update(frameRateScale)
        projectiles?.update(frameRateScale)

        when (transition.tick(frameRateScale)) {
            120 -> player.rezOut()
            40, 60, 100 -> currentStarField = newStarField
            50, 75 -> currentStarField = starField
        }
    } // update

    override fun draw(canvas: Canvas) {
        currentStarField.draw(canvas)
        if (currentStarField == starField)
          projectiles?.draw(canvas)

        player.draw(canvas)
    } // draw

    private fun startNextWave() {
        player.lifeGained()
        game.checkpointScore(waveIndex)
        game.nextWave(nextWave)
    } // startNextWave
} // Emptiness