package uk.co.jezuk.swoop.wave.transition

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Projectiles
import uk.co.jezuk.swoop.utils.Latch
import uk.co.jezuk.swoop.wave.StarField
import uk.co.jezuk.swoop.wave.Wave
import kotlin.time.Duration.Companion.seconds

class LevelTransition(
    private val game: Game,
    private val starField: StarField,
    private val newStarField: StarField,
    private val projectiles: Projectiles?,
    private val nextWave: Wave,
    private val waveIndex: Int
): Wave {
    override val players = game.players
    private var transition = Latch(3.5.seconds) { startNextWave() }
    private var currentStarField = starField

    init {
        players.forAll { it.gunOff() }
    }

    override fun update(frameRateScale: Float) {
        players.forAll { it.update(frameRateScale) }
        projectiles?.update(frameRateScale)

        when (transition.tick(frameRateScale)) {
            120 -> players.forAlive { it.rezOut() }
            40, 60, 100 -> currentStarField = newStarField
            50, 75 -> currentStarField = starField
        }
    } // update

    override fun draw(canvas: Canvas) {
        currentStarField.draw(canvas)
        if (currentStarField == starField)
          projectiles?.draw(canvas)

        players.forAll { it.draw(canvas) }
    } // draw

    private fun startNextWave() {
        players.forAlive {
            it.lifeGained()
            it.gunOn()
        }

        game.nextWave(nextWave, waveIndex)
    } // startNextWave
} // Emptiness