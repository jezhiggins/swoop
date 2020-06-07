package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.craft.Asteroids
import uk.co.jezuk.swoop.craft.Targets

class EndAttract(
    private val game: Game,
    private val starField: StarField,
    targets: Targets
): WaveWithTargets(targets) {
    private var tick = 0
    private val steps = 120 / targets.size

    init {
        game.start()
    }

    /////
    override fun update(fps: Long) {
        targets.update(fps)

        if (++tick == steps) {
            targets.explodeOne()
            tick = 0
        }
        if (targets.size == 0)
            game.nextWave(FlyAround(game, starField))
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        targets.draw(canvas)
    } // draw
}