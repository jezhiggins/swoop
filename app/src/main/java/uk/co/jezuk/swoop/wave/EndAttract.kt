package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Targets
import uk.co.jezuk.swoop.utils.Repeat
import kotlin.random.Random

class EndAttract(
    private val game: Game,
    private val starField: StarField,
    targets: Targets
): WaveWithTargets(targets) {
    private val exploders = Repeat(120 / targets.size, { explodeOneTarget() })

    init {
        game.start()
        targets.onEliminated({ game.nextWave(AsteroidsAndComets(game, starField)) })
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