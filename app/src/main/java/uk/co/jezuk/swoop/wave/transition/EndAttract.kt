package uk.co.jezuk.swoop.wave.transition

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.GameMode
import uk.co.jezuk.swoop.craft.Targets
import uk.co.jezuk.swoop.utils.Repeat
import uk.co.jezuk.swoop.wave.StarField
import uk.co.jezuk.swoop.wave.WaveWithTargets
import uk.co.jezuk.swoop.wave.Waves
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class EndAttract(
    private val game: Game,
    private val starField: StarField,
    targets: Targets,
    fromWave: Int,
    gameMode: GameMode
): WaveWithTargets(targets) {
    private val exploders = Repeat(2.4.seconds / targets.size) { explodeOneTarget() }

    init {
        game.start(fromWave, gameMode)
        targets.onEliminated {
            game.nextWave(
                Waves.from(game, fromWave, starField)
            )
        }
    }

    /////
    override fun update(frameRateScale: Float) {
        updateTargets(frameRateScale)
        exploders.tick(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        drawTargets(canvas)
    } // draw

    /////
    private fun explodeOneTarget() {
        val t = targets[Random.nextInt(targets.size)]
        t.explode()
        targets.remove(t)
    } // explodeOneTarget
} // EndAttract