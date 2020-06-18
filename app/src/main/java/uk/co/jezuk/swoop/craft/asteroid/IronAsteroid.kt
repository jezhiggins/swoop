package uk.co.jezuk.swoop.craft.asteroid

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.craft.Puff
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.Spaceman
import uk.co.jezuk.swoop.craft.Target
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class IronAsteroid(
    game: Game,
    wave: Wave,
    pos: Point,
    scale: Float = Big
): Asteroid(game, wave, pos, scale) {
    private val ting = { game.sound(R.raw.ting, position) }

    private var spang = 0
    private fun resetSpang() {
        spang = (2 * size).toInt()
    } // resetSpang

    init {
        resetSpang()
    } // init

    override fun drawAsteroid(canvas: Canvas) {
       canvas.drawPath(path, brush)
    } // drawAsteroid

    override fun shot(): Target.Impact {
        if (--spang != 0) {
            ting()
        } else {
            game.scored(400 / size.toInt())
            split()
        }
        return Target.Impact.HARD
    } // shot

    override fun shipCollision(ship: Ship) {
        ship.hit()
    } // shipCollision

    private fun split() {
        if (Random.nextFloat() < 0.1f)
            Spaceman(game, wave, position)
        explode()

        if (size != Small) {
            scale /= 2
            velocity = AsteroidVector(scale)
            resetSpang()
            
            IronAsteroid(
                game,
                wave,
                position,
                scale
            )
        } else {
            wave.removeTarget(this)
        }
    } // split

    companion object {
        val brush = Paint()

        init {
            brush.setARGB(255, 204, 85, 0)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.FILL_AND_STROKE
        }
    } // companion object
} // Asteroid