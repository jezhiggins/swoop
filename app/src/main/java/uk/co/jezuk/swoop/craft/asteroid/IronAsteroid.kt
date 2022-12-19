package uk.co.jezuk.swoop.craft.asteroid

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.craft.spaceman.OrangeSpaceman
import uk.co.jezuk.swoop.craft.Target
import uk.co.jezuk.swoop.craft.spaceman.BlueSpaceman
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class IronAsteroid(
    game: Game,
    wave: Wave,
    pos: Point,
    scale: Float = Big
): Asteroid(game, wave, pos, scale) {
    private val ting = { Game.sound(R.raw.ting, position) }

    private var spang = 0
    private fun resetSpang() {
        spang = (2 * size).toInt()
    } // resetSpang

    init {
        resetSpang()
    } // init

    override fun drawAsteroid(canvas: Canvas) {
        canvas.drawPath(path, brush)
        canvas.drawLines(shape, outline)
    } // drawAsteroid

    override fun shot(): Target.Effect {
        ting()

        if (--spang == 0) {
            split()
            return Target.Hard(400 / size.toInt())
        }

        return Target.Hard()
    } // shot

    override fun playerCollision(player: Player) = player.hit()

    private fun split() {
        val spacemanPops = Random.nextFloat()
        if (spacemanPops < 0.12f) {
            if (spacemanPops < 0.05f)
                BlueSpaceman(wave, position)
            else
                OrangeSpaceman(wave, position)
        }
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
        val brush = fillBrush(Color.argb(255, 204, 85, 0))
        val outline = outlineBrush(Color.BLACK)

        fun field(
            game: Game,
            wave: Wave,
            big: Int
        ) {
            field(
                ::IronAsteroid,
                game,
                wave,
                big
            )
        } // field

    } // companion object
} // Asteroid