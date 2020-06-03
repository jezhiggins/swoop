package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.craft.Asteroids

class EndAttract(
    private val game: Game,
    private val starField: StarField,
    private val asteroids: Asteroids
): Wave {
    private var tick = 0
    private val steps = 120 / asteroids.size

    /////
    override fun onSingleTapUp() = Unit
    override fun onScroll(offsetX: Float, offsetY: Float) = Unit
    override fun onLongPress() = Unit

    /////
    override fun update(fps: Long) {
        asteroids.update(fps, game.width, game.height)

        if (++tick == steps) {
            asteroids.explodeLast()
            tick = 0
        }
        if (asteroids.size == 0)
            game.nextWave(FlyAround(game, starField))
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        asteroids.draw(canvas)
    } // draw
}