package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave
import kotlin.random.Random

class Spaceman(
    private val game: Game,
    private val wave: Wave,
    pos: Point
) : Target {
    override val position = Point(pos)
    private val velocity = SpacemanVector()
    private val orientation = Rotation.random()
    private val rotation = Random.nextDouble(-1.5, 1.5)
    private val spaceman = game.loadBitmap(R.drawable.spaceman).bitmap
    private val matrix = Matrix()
    private val sound = game.loadSound(R.raw.spaceman)
    override val killDist = spaceman.width / 2f

    init {
        wave.addTarget(this)

        sound(position)

        matrix.postTranslate(-spaceman.width / 2f, -spaceman.height / 2f)
        matrix.postRotate(orientation.angle.toFloat())
        matrix.postTranslate(0f, 0f)
    } // init

    /////
    override fun update(frameRateScale: Float) {
        position.move(velocity, frameRateScale, game.extent, killDist)
        matrix.postRotate(rotation.toFloat())
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)

        // canvas.drawCircle(0f, 0f, 50f, Asteroid.brush)
        canvas.drawBitmap(spaceman, matrix, null)

        canvas.restore()
    } // draw

    /////
    override fun shipCollision(ship: Ship) {
        game.scored(1000)
        wave.removeTarget(this)
    } // shipCollision
    override fun shot(): Target.Impact = Target.Impact.NONE
    override fun explode() = Unit

    companion object {
        fun SpacemanVector() =
            Vector(Random.nextDouble(2.0), Random.nextDouble(360.0))
    } // companion object
} // Spaceman