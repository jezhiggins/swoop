package uk.co.jezuk.swoop.craft.asteroid

import android.graphics.Canvas
import android.graphics.Path
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.craft.Puff
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.Target
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

abstract class Asteroid(
    protected val game: Game,
    protected val wave: Wave,
    pos: Point,
    protected var scale: Float = 4f
) : Target {
    override val position = pos.copy()
    protected var velocity = AsteroidVector(scale)
    protected var orientation = Rotation.random()
    private val rotation = Random.nextDouble(-2.0, 2.0)
    private val killRadius = 25f
    override val killDist get() = scale * killRadius
    protected val size get() = scale

    private val smallBang = { game.sound(R.raw.bangsmall, position) }
    private val midBang = { game.sound(R.raw.bangmedium, position) }
    private val bigBang = { game.sound(R.raw.banglarge, position) }

    init {
        wave.addTarget(this)
    } // init

    override fun update(frameRateScale: Float) {
        position.move(velocity, frameRateScale, game.extent, killDist)
        orientation += rotation * frameRateScale
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        canvas.scale(scale, scale)

        // canvas.drawCircle(0f, 0f, killRadius, brush)
        orientation.rotate(canvas)

        drawAsteroid(canvas)

        canvas.restore()
    } // draw

    abstract fun drawAsteroid(canvas: Canvas)

    override fun explode() {
        Puff(wave, position)
        val b = when(size) {
            Small -> smallBang
            Medium -> midBang
            else -> bigBang
        }
        b()
    } // bang

    companion object {
        val Big: Float = 4f
        val Medium: Float = 2f
        val Small: Float = 1f
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

        val path = Path()

        init {
            path.moveTo(shape[0], shape[1])
            for (i in 0 until shape.size step 2)
                path.lineTo(shape[i], shape[i+1])
            path.close()
        } // init

        fun field(
            makeFn: (Game, Wave, Point, Float) -> Asteroid,
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
                    makeFn(
                        game,
                        wave,
                        originFn(),
                        size
                    )
                }
            }
        } // field

        fun AsteroidVector(scale: Float) =
            Vector(6.0 - scale, Random.nextDouble(360.0))
    } // companion object
} // Asteroid
