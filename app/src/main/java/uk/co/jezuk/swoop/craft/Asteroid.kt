package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class Asteroid(
    private val game: Game,
    private val wave: Wave,
    pos: Point,
    private var scale: Float = 4f
): Target {
    override val position = pos.copy()
    private var velocity = AsteroidVector(scale)
    private var orientation = Rotation.random()
    private val rotation = Random.nextDouble(-2.0, 2.0)
    private val killRadius = 25f
    private val smallBang = game.loadSound(R.raw.bangsmall)
    private val midBang = game.loadSound(R.raw.bangmedium)
    private val bigBang = game.loadSound(R.raw.banglarge)

    init {
        wave.addTarget(this)
    }

    override val killDist get() = scale * killRadius
    private val size get() = scale.toInt()

    override fun update(frameRateScale: Float) {
        position.move(velocity, frameRateScale, game.extent, killDist)
        orientation += rotation
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        canvas.scale(scale, scale)

        // canvas.drawCircle(0f, 0f, killRadius, brush)
        orientation.rotate(canvas)
        canvas.drawLines(shape, brush)

        canvas.restore()
    } // draw

    private fun split() {
        if (scale != 1f) {
            scale /= 2
            velocity = AsteroidVector(scale)
            Asteroid(game, wave, position, scale)
        } else {
            wave.removeTarget(this)
        }
        explode()
    } // split

    override fun shot(): Target.Impact {
        game.scored(400/scale.toInt())
        split()
        return Target.Impact.SOFT
    } // shot

    override fun shipCollision(ship: Ship) {
        split()
        ship.hit()
    } // shipCollision

    override fun explode() {
        Puff(wave, position)
        val b = when(size) {
            1 -> smallBang
            2 -> midBang
            else -> bigBang
        }
        b(position)
    } // bang

    companion object {
        val Big: Float = 4f
        val Medium: Float = 2f
        val Small: Float = 1f

        fun AsteroidVector(scale: Float) =
            Vector(6.0 - scale, Random.nextDouble(360.0))

        val brush = Paint()
        val shape = floatArrayOf(
            0f, 12.5f, 12.5f, 25f,
            12.5f, 25f, 25f, 12.5f,
            25f, 12.5f, 18.75f, 0f,
            18.75f, 0f, 25f, -12.5f,
            25f, -12.5f, 6.25f, -25f,
            6.25f, -25f, -12.5f, -25f,
            -12.5f, -25f, -25f, -12.5f,
            -25f, -12.5f, -25f, 12.5f,
            -25f, 12.5f, -12.5f, 25f,
            -12.5f, 25f, 0f, 12.5f
        )

        init {
            brush.setARGB(255, 160, 160, 160)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.STROKE
        }

        fun field(
            game: Game,
            wave: Wave,
            big: Int,
            medium: Int = 0,
            small: Int = 0,
            originFn: () -> Point = { game.extent.randomPointOnEdge() }
        ) {
            val sizes = mapOf(
                Pair(big, Big),
                Pair(medium, Medium),
                Pair(small, Small)
            )
            for ((count, size) in sizes) {
                for(a in 0 until count) {
                    Asteroid(
                        game,
                        wave,
                        originFn(),
                        size
                    )
                }
            }
        } // Asteroids
    } // companion object
} // Asteroid