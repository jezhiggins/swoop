package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.utils.Latch
import uk.co.jezuk.swoop.utils.RestartableLatch

class Ship(private val game: Game): Craft {
    var orientation = Rotation(-90.0)
    private var targetOrientation = orientation.clone()

    private val thrustSound = game.loadSound(R.raw.thrust)
    private val explosionSound = game.loadSound(R.raw.shipexplosion)
    private val rezInSound = game.loadSound(R.raw.rezin)
    private val rezOutSound = game.loadSound(R.raw.rezout)

    private val pos = Point(0.0, 0.0)
    private var state: ShipState = RezIn(this)

    /////////////////////////////////////
    override val position get() = state.position
    override val killDist get() = shipRadius

    val velocity = Vector(0.0, 0.0)
    val armed get() = state.armed

    init {
        reset()
    } // init

    private fun lifeLost(): Game.NextShip {
        reset()
        return game.lifeLost()
    } // lifeLost

    private fun reset() {
        orientation.reset(-90.0)
        targetOrientation = orientation.clone()
        velocity.reset()
        pos.reset()
    } // reset

    /////////////////////////////////////
    fun thrust() = state.thrust()
    fun rotateTowards(angle: Double) = state.rotateTowards(angle)

    override fun update(frameRateScale: Float) {
        rotateShip(frameRateScale)
        applyThrust(frameRateScale)

        state.update(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        pos.translate(canvas)
        orientation.rotate(canvas)

        state.draw(canvas)

        canvas.restore()
    } // draw

    fun hit() = state.hit()
    fun rezOut() { state = RezOut(this) }

    ////////////////
    private fun rotateShip(frameRateScale: Float) {
        val angleOffset = targetOrientation - orientation

        val direction = if (angleOffset.angle >= 0) 1.0 else -1.0
        val magnitude = Math.abs(angleOffset.angle)
        val rotationDelta = if (magnitude > 30) {
            direction * 5
        } else if (magnitude > 3) {
            direction * 2
        } else if (magnitude > 1) {
            direction
        } else {
            0.0
        }

        orientation += (rotationDelta * frameRateScale)
    } // rotateShip

    private fun applyThrust(frameRateScale: Float) {
        pos.move(velocity, frameRateScale, game.extent, killDist)
    } // applyThrust

    //////////////////////////
    companion object {
        val Hyperspace = Point(-100000.0, -100000.0)

        val shipBrush = Paint()
        private val shipFillBrush = Paint()
        private val thrustBrush = Paint()
        private val explodeBrush = Paint()
        private val redBrush = Paint()

        private val shipPath = Path()
        val shape = floatArrayOf(
            50f, 0f, -50f, 25f,
            -30f, 0f, -50f, 25f,
            -30f, 0f, -50f, -25f,
            50f, 0f, -50f, -25f
        )
        private val thruster = floatArrayOf(
            -40f, 15f, -70f, 0f,
            -70f, 0f, -40f, -15f,
            -45f, 12f, -75f, 0f,
            -75f, 0f, -45f, -12f
        )

        private val shipRadius: Float get() = 30f;

        init {
            shipBrush.color = Color.GREEN
            shipBrush.alpha = 175
            shipBrush.strokeWidth = 10f
            shipBrush.strokeCap = Paint.Cap.ROUND
            shipBrush.style = Paint.Style.STROKE

            shipFillBrush.color = Color.BLACK
            shipFillBrush.alpha = 255
            shipBrush.strokeWidth = 10f
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

            shipPath.moveTo(shape[0], shape[1])
            for (i in 0 until shape.size step 2)
                shipPath.lineTo(shape[i], shape[i+1])
            shipPath.close()
        } // init
    } // companion object

    //////////////////////////
    private interface ShipState {
        val position: Point get() = Hyperspace
        val armed: Boolean get() = false

        fun thrust() = Unit
        fun rotateTowards(angle: Double) = Unit

        fun update(frameRateScale: Float)
        fun draw(canvas: Canvas)

        fun hit() = Unit
    } // ShipState

    private class Flying(private val ship: Ship): ShipState {
        private val thrustFrames = RestartableLatch(10)

        override val position get() = ship.pos
        override val armed get() = true

        override fun thrust() {
            val thrust = Vector(2.0, ship.orientation)
            ship.velocity += thrust

            thrustFrames.start()
            ship.thrustSound(ship.position)
        } // thrust

        override fun rotateTowards(angle: Double) {
            ship.targetOrientation = Rotation(angle)
        } // rotateTowards

        override fun update(frameRateScale: Float) {
            thrustFrames.tick(frameRateScale)
        } // update

        override fun draw(canvas: Canvas) {
            canvas.drawPath(shipPath, shipFillBrush)
            canvas.drawLines(shape, shipBrush)

            if (thrustFrames.running)
                canvas.drawLines(thruster, thrustBrush)
        } // draw

        override fun hit() {
            ship.state = Exploding(ship)
        } // explode
    } // Flying

    private class Exploding(private val ship: Ship): ShipState {
        private val explodeShape = shape.copyOf()
        private var explosion = Latch(50, { whatsNext() })

        init {
            ship.explosionSound(ship.position)
        }

        override fun update(frameRateScale: Float) {
            blowUpShip(frameRateScale)
        } // update

        override fun draw(canvas: Canvas) {
            canvas.drawLines(explodeShape, explodeBrush)
        } // draw

        private fun blowUpShip(frameRateScale: Float) {
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

        private fun whatsNext() {
            ship.state = if (ship.lifeLost() == Game.NextShip.Continue) {
                RezIn(ship)
            } else {
                SpinningInTheVoid()
            }
        } // whatsNext
    } // Exploding

    private class RezIn(private val ship: Ship): ShipState {
        private val pause = Latch(60, { ship.rezInSound(ship.position) })
        private var radius = 600f

        override fun rotateTowards(angle: Double) {
            ship.pos.move(Vector(15.0, angle), 1f, ship.game.extent)
        }
        override fun update(frameRateScale: Float) {
            pause.tick(frameRateScale)
            if (pause.running) return

            radius -= (radius / 20)
            if (radius < 5)
                ship.state = Flying(ship)
        } // update

        override fun draw(canvas: Canvas) {
            if (pause.running) return

            val r = (radius / 100).toInt()
            val brush = if ((r/2f) == (r/2).toFloat()) shipBrush else redBrush

            canvas.drawCircle(0f, 0f, radius, brush)
        } // draw
    } // RezIn

    private class RezOut(private val ship: Ship): ShipState {
        private val rezOutShape = shape.copyOf()
        private var r = 0

        init {
            ship.rezOutSound(ship.position)
        }

        override fun update(frameRateScale: Float) {
            ++r

            for (i in 0 until rezOutShape.size) {
                var v = rezOutShape[i]
                if (v < 0) v -= r
                if (v > 0) v += r
                rezOutShape[i] = v
            }

        } // update

        override fun draw(canvas: Canvas) {
            val brush = if ((r/2f) == (r/2).toFloat()) shipBrush else redBrush
            canvas.drawLines(rezOutShape, brush)
        } // draw
    } // RezOut

    private class SpinningInTheVoid : ShipState {
        override fun update(frameRateScale: Float) = Unit
        override fun draw(canvas: Canvas) = Unit
    }
} // Ship

