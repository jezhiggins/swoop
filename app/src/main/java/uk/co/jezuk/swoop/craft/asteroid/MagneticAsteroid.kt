package uk.co.jezuk.swoop.craft.asteroid

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.craft.spaceman.OrangeSpaceman
import uk.co.jezuk.swoop.craft.Target
import uk.co.jezuk.swoop.craft.spaceman.BlueSpaceman
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class MagneticAsteroid(
    game: Game,
    wave: Wave,
    pos: Point,
    scale: Float = Big
): Asteroid(game, wave, pos, scale) {
    override fun update(frameRateScale: Float) {
        wave.player?.let {
            val shipDistance = position.distance(it.position)
            val shipAngle = position.angleTo(it.position)

            val deflectionMagnitude = (2e3 * scale) / (shipDistance * shipDistance)
            val deflectionVector = Vector(deflectionMagnitude, shipAngle)

            velocity += deflectionVector
        }
        super.update(frameRateScale)
    } // update

    override fun drawAsteroid(canvas: Canvas) {
        canvas.drawPath(path, brush)
        canvas.drawLines(shape, outline)
    } // drawAsteroid

    override fun shot(): Target.Effect {
        split()
        return Target.Hard(600 / size.toInt())
    } // shot

    override fun playerCollision(player: Player) = player.hit()

    private fun split() {
        val spacemanPops = Random.nextFloat()
        if (spacemanPops < 0.12f) {
            if (spacemanPops < 0.05f)
                BlueSpaceman(game, wave, position)
            else
                OrangeSpaceman(game, wave, position)
        }
        explode()
        if (size != Small) {
            scale /= 2
            velocity = AsteroidVector(scale)
            MagneticAsteroid(
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
        val outline = Paint()

        init {
            brush.setARGB(255, 153, 204, 255)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.FILL

            outline.setARGB(255, 51, 153, 255)
            outline.strokeWidth = 3f
            outline.strokeCap = Paint.Cap.ROUND
            outline.strokeJoin = Paint.Join.ROUND
            outline.style = Paint.Style.FILL
        }

        fun field(
            game: Game,
            wave: Wave,
            big: Int
        ) {
            field(
                ::MagneticAsteroid,
                game,
                wave,
                big
            )
        } // field

    } // companion object
} // Asteroid