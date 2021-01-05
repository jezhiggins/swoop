package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.craft.asteroid.Asteroid
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.utils.Latch
import uk.co.jezuk.swoop.utils.Repeat
import uk.co.jezuk.swoop.wave.Wave
import kotlin.math.sin
import kotlin.random.Random

class Tumbler(
    private val game: Game,
    private val wave: Wave,
    traverse: Array<Point> = game.extent.randomHorizontalTraverse()
): Target {
    override val position = Point(traverse[0])
    private val velocity = Vector(Random.nextDouble(2.0, 5.0), position.angleTo(traverse[1]))
    private val orientation = Rotation.random()
    private var rotation = Random.nextDouble(1.0, 2.5)
    private var shooter = Repeat(Random.nextInt(120, 180), { fire() })
    private var alive = true
    private val explodeShape = outerShape.copyOf()
    private var explosion = Latch(70, { wave.removeTarget(this) })

    init {
        game.sound(R.raw.sauceralarm, position)
        wave.addTarget(this)

        if (Random.nextDouble() < 0.5)
            rotation = -rotation
    } // init

    override val killDist get() = 50f

    override fun update(frameRateScale: Float) {
        position.move(velocity, frameRateScale, game.extent, killDist)
        orientation += rotation * frameRateScale
        if (alive)
            shooter.tick(frameRateScale)
        else
            blowUpTumbler(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        orientation.rotate(canvas)
        if (alive)
            canvas.drawPath(path, shipBrush)
        else
            canvas.drawLines(explodeShape, shipBrush)

        canvas.restore()
    } // draw

    override fun shot(): Target.Impact {
        if (alive) {
            game.scored(1000)
            explode()
            return Target.Impact.HARD
        }
        return Target.Impact.NONE
    } // shot

    override fun explode() {
        alive = false
        game.sound(R.raw.saucerexplosion, position)
        BigPuff(wave, position)
    } // explode

    override fun shipCollision(ship: Ship) {
        if (alive)
            ship.hit()
    } // shipCollision

    private fun blowUpTumbler(frameRateScale: Float) {
        for (l in 0 until explodeShape.size step 4) {
            val x = blowUpShift(explodeShape[l], frameRateScale)
            val y = blowUpShift(explodeShape[l + 3], frameRateScale)

            for (p in 0 until 4 step 2) {
                explodeShape[l + p] += x
                explodeShape[l + 1 + p] += y
            }
        }

        explosion.tick(frameRateScale)
    } // blowUpShip

    private fun blowUpShift(p: Float, frameRateScale: Float): Float {
        if (p < 0) return -5f * frameRateScale
        if (p > 0) return 5f * frameRateScale
        return 0f
    } // blowUpShift

    private fun fire() {
        game.sound(R.raw.saucerfire, position)
        for (offset in 0..360 step 90) {
            val initialPosition = Point(position)
            val direction = orientation.angle + offset
            initialPosition.move(Vector(60.0, direction, 60.0), 1f, game.extent)
            val missileVelocity = velocity.copy()
            missileVelocity += Vector(7.0, direction)
            Missile(game, wave, initialPosition, missileVelocity, direction.toFloat())
        }
    } // fire

    companion object {
        private val outerShape = floatArrayOf(
            60f, -25f, 60f, 25f,
            60f, 25f, 25f, 60f,
            25f, 60f, -25f, 60f,
            -25f, 60f, -60f, 25f,
            -60f, 25f, -60f, -25f,
            -60f, -25f, -25f, -60f,
            -25f, -60f, 25f, -60f,
            25f, -60f, 60f, -25f
        )
        private val upperRightCutout = floatArrayOf(
            48f, -20f, 20f, -48f,
            20f, -48f, 10f, -10f,
            10f, -10f, 48f, -20f
        )
        private val upperLeftCutout = floatArrayOf(
            -48f, -20f, -20f, -48f,
            -20f, -48f, -10f, -10f,
            -10f, -10f, -48f, -20f
        )
        private val lowerLeftCutout = floatArrayOf(
            -48f, 20f, -20f, 48f,
            -20f, 48f, -10f, 10f,
            -10f, 10f, -48f, 20f
        )
        private val lowerRightCutout = floatArrayOf(
            48f, 20f, 20f, 48f,
            20f, 48f, 10f, 10f,
            10f, 10f, 48f, 20f
        )

        val path = Path()

        init {
            val shapes = listOf(
                outerShape,
                upperLeftCutout, upperRightCutout,
                lowerLeftCutout, lowerRightCutout)

            for (shape in shapes) {
                path.moveTo(shape[0], shape[1])
                for (i in shape.indices step 2)
                    path.lineTo(shape[i], shape[i + 1])
            }
            path.close()
        } // init

        val shipBrush = Paint()

        init {
            shipBrush.setARGB(255, 255, 102, 0)
            shipBrush.strokeWidth = 8f
            shipBrush.strokeCap = Paint.Cap.ROUND
            shipBrush.style = Paint.Style.STROKE
        } // init
    }
} // class Tumbler
