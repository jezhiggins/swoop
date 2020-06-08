package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.craft.Asteroids
import uk.co.jezuk.swoop.craft.Targets
import uk.co.jezuk.swoop.utils.Repeat

class EndAttract(
    private val game: Game,
    private val starField: StarField,
    targets: Targets
): WaveWithTargets(targets) {
    private val exploders = Repeat(120 / targets.size, { explodeOneTarget() })

    init {
        game.start()
        targets.onEliminated({ game.nextWave(FlyAround(game, starField)) })
    }

    /////
    override fun update(fps: Long) {
        targets.update(fps)
        exploders.tick()
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        targets.draw(canvas)
    } // draw

    /////
    private fun explodeOneTarget() {
        val t = targets.first()
        t.explode()
        targets.remove(t)
    } // explodeOneTarget
} // EndAttract