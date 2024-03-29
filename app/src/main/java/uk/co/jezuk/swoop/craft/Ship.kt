package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import uk.co.jezuk.swoop.Frames
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.utils.Latch
import uk.co.jezuk.swoop.utils.RestartableLatch
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Ship(
    private val player: Player,
    shape: FloatArray,
    private val shipBrush: Paint,
    private val origin: Point,
    private val initialRotation: Rotation)
{
    var orientation = initialRotation.clone()
    val shipOutline: FloatArray = shape
    val shipFill = ShipFill(shape)
    private var targetOrientation = orientation.clone()

    private val thrustSound = { Game.sound(R.raw.thrust, position) }
    private val explosionSound = { Game.sound(R.raw.shipexplosion, position) }
    private val rezInSound = { Game.sound(R.raw.rezin, position) }
    private val rezOutSound = { Game.sound(R.raw.rezout, position) }

    private val pos = Point(origin)
    private var state: ShipState = RezIn(this)

    /////////////////////////////////////
    val position get() = state.position
    val killDist get() = Radius

    val velocity = Vector(0.0, 0.0)
    val armed get() = state.armed

    init {
        reset()
    } // init

    fun reset() {
        orientation.reset(initialRotation)
        targetOrientation = orientation.clone()
        velocity.reset()
        pos.reset(origin)
        state = RezIn(this)
    } // reset

    /////////////////////////////////////
    fun thrust() = state.thrust()
    fun rotateTowards(angle: Double) = state.rotateTowards(angle)

    fun update(frameRateScale: Float) {
        rotateShip(frameRateScale)
        applyThrust(frameRateScale)

        state.update(frameRateScale)
    } // update

    fun draw(canvas: Canvas) {
        canvas.save()

        pos.translate(canvas)
        orientation.rotate(canvas)

        state.draw(canvas)

        canvas.restore()
    } // draw

    fun hit() = state.hit()
    fun rezOut() {
        state = RezOut(this)
    }

    ////////////////
    private fun rotateShip(frameRateScale: Float) {
        val angleOffset = targetOrientation - orientation

        val direction = if (angleOffset.angle >= 0) 1.0 else -1.0
        val magnitude = abs(angleOffset.angle)
        val rotationDelta = if (magnitude > 90) {
            direction * 5
        } else if (magnitude > 45) {
            direction * 3
        } else if (magnitude > 20) {
            direction * 2
        } else if (magnitude > 1) {
            direction
        } else {
            0.0
        }

        orientation += (rotationDelta * frameRateScale)
    } // rotateShip

    private fun applyThrust(frameRateScale: Float) {
        pos.move(velocity, frameRateScale, Game.extent, killDist)
    } // applyThrust

    //////////////////////////
    companion object {
        val Dart = floatArrayOf(
            50f, 0f, -50f, 25f,
            -30f, 0f, -50f, 25f,
            -30f, 0f, -50f, -25f,
            50f, 0f, -50f, -25f
        )
        val Speeder = floatArrayOf(
            50f, 0f, -50f, 18f,
            -35f, 25f, -50f, 18f,
            -35f, 25f, -35f, -25f,
            -35f, -25f, -35f, 25f,
            -35f, -25f, -50f, -18f,
            50f, 0f, -50f, -18f,
        )

        val GreenBrush = Brush(Color.GREEN)
        val PinkBrush = Brush(Color.rgb(255, 20, 147))

        private val shipFillBrush = Paint()
        private val thrustBrush = Paint()
        private val explodeBrush = Paint()
        private val redBrush = Paint()

        private val thruster = floatArrayOf(
            -40f, 15f, -70f, 0f,
            -70f, 0f, -40f, -15f,
            -45f, 12f, -75f, 0f,
            -75f, 0f, -45f, -12f
        )

        val Radius: Float get() = 30f

        private fun Brush(color: Int): Paint {
            val brush = Paint()
            brush.color = color
            brush.alpha = 200
            brush.strokeWidth = 10f
            brush.strokeCap = Paint.Cap.ROUND
            brush.style = Paint.Style.STROKE
            return brush
        }

        init {
            shipFillBrush.color = Color.BLACK
            shipFillBrush.alpha = 255
            shipFillBrush.style = Paint.Style.FILL_AND_STROKE

            explodeBrush.color = Color.YELLOW
            explodeBrush.alpha = 175
            explodeBrush.strokeWidth = 10f
            explodeBrush.strokeCap = Paint.Cap.ROUND
            explodeBrush.style = Paint.Style.STROKE

            thrustBrush.setARGB(100, 255, 215, 0)
            thrustBrush.strokeWidth = 5f

            redBrush.setARGB(255, 255, 0, 0)
            redBrush.strokeWidth = 10f
            redBrush.style = Paint.Style.STROKE
        } // init

        private fun ShipFill(points: FloatArray): Path {
            val shape = Path()
            shape.moveTo(points[0], points[1])
            for (i in points.indices step 2)
                shape.lineTo(points[i], points[i+1])
            shape.close()
            return shape
        }
    } // companion object

    //////////////////////////
    private interface ShipState {
        val position: Point get() = Point.Hyperspace
        val armed: Boolean get() = false

        fun thrust() = Unit
        fun rotateTowards(angle: Double) = Unit

        fun update(frameRateScale: Float)
        fun draw(canvas: Canvas)

        fun hit() = Unit
    } // ShipState

    private class Flying(private val ship: Ship): ShipState {
        private val thrustFrames = RestartableLatch(0.2.seconds)

        override val position get() = ship.pos
        override val armed get() = true

        override fun thrust() {
            val thrust = Vector(2.0, ship.orientation)
            ship.velocity += thrust

            thrustFrames.start()
            ship.thrustSound()
        } // thrust

        override fun rotateTowards(angle: Double) {
            ship.targetOrientation = Rotation(angle)
        } // rotateTowards

        override fun update(frameRateScale: Float) {
            thrustFrames.tick(frameRateScale)
        } // update

        override fun draw(canvas: Canvas) {
            canvas.drawPath(ship.shipFill, shipFillBrush)
            canvas.drawLines(ship.shipOutline, ship.shipBrush)

            if (thrustFrames.running)
                canvas.drawLines(thruster, thrustBrush)
        } // draw

        override fun hit() {
            ship.state = Exploding(ship)
        } // explode
    } // Flying

    private class Exploding(private val ship: Ship): ShipState {
        private val exploder = Exploder({ whatsNext() }, ship.shipOutline, explodeBrush, 1.seconds, false)

        init {
            ship.explosionSound()
        }

        override fun update(frameRateScale: Float) {
            exploder.update(frameRateScale)
        } // update

        override fun draw(canvas: Canvas) {
            exploder.draw(canvas)
        } // draw

        private fun whatsNext() {
            ship.state = if (ship.player.lifeLost() == Game.NextShip.Continue) {
                RezIn(ship)
            } else {
                SpinningInTheVoid()
            }
        } // whatsNext
    } // Exploding

    private class RezIn(private val ship: Ship): ShipState {
        private val t = 1.2.seconds
        private val pause = Latch(t) { ship.rezInSound() }
        private var radius = 600f

        override fun rotateTowards(angle: Double) {
            ship.pos.move(Vector(15.0, angle), 1f, Game.extent)
        }
        override fun update(frameRateScale: Float) {
            pause.tick(frameRateScale)
            if (pause.running) return

            radius -= (radius / 20 * frameRateScale)
            if (radius < 5)
                ship.state = Flying(ship)
        } // update

        override fun draw(canvas: Canvas) {
            if (pause.running) return

            val r = (radius / 100).toInt()
            val brush = if ((r/2f) == (r/2).toFloat()) ship.shipBrush else redBrush

            canvas.drawCircle(0f, 0f, radius, brush)
        } // draw
    } // RezIn

    private class RezOut(private val ship: Ship): ShipState {
        private val rezOutShape = ship.shipOutline.copyOf()
        private var r = 0f

        init {
            ship.rezOutSound()
        }

        override fun update(frameRateScale: Float) {
            r += frameRateScale

            for (i in rezOutShape.indices) {
                var v = rezOutShape[i]
                if (v < 0) v -= r
                if (v > 0) v += r
                rezOutShape[i] = v
            }
        } // update

        override fun draw(canvas: Canvas) {
            val ir = r.toInt()
            val brush = if ((ir/2f) == (ir/2).toFloat()) ship.shipBrush else redBrush
            canvas.drawLines(rezOutShape, brush)
        } // draw
    } // RezOut

    private class SpinningInTheVoid : ShipState {
        override fun update(frameRateScale: Float) = Unit
        override fun draw(canvas: Canvas) = Unit
    }
} // Ship

