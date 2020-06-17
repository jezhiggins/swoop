package uk.co.jezuk.swoop.craft.asteroid

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
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

    init {
        wave.addTarget(this)
    } // init

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

        drawAsteroid(canvas)

        canvas.restore()
    } // draw

    abstract fun drawAsteroid(canvas: Canvas)

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

        fun AsteroidVector(scale: Float) =
            Vector(6.0 - scale, Random.nextDouble(360.0))
    } // companion object
} // Asteroid
