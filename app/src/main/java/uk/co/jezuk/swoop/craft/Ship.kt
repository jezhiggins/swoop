package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector

class Ship(private val game: Game) {
    private var rotation = -90.0
    private var targetRotation = rotation

    private val thrustSound = game.sounds.load(R.raw.thrust)
    private val explosionSound = game.sounds.load(R.raw.shipexplosion)
    private val rezInSound = game.sounds.load(R.raw.rezin)

    private val velocity = Vector(0.0, 0.0)
    private val pos = Point(0.0, 0.0)

    private var state: ShipState = RezIn(this)

    init {
        reset()
    } // init

    private fun reset() {
        rotation = -90.0
        targetRotation = rotation
        velocity.reset()
        pos.reset()
    } // reset

    /////////////////////////////////////
    val position: Point get() = state.position
    val killDist: Float get() = shipRadius

    fun thrust() = state.thrust()
    fun rotateTowards(angle: Double) = state.rotateTowards(angle)

    fun update(fps: Long) {
        rotateShip()
        applyThrust()

        state.update(fps)
    } // update

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(pos.x.toFloat(), pos.y.toFloat())
        canvas.rotate(rotation.toFloat())

        state.draw(canvas)

        canvas.restore()
    } // draw

    fun explode() = state.explode()

    ////////////////
    private fun rotateShip() {
        var angleOffset = targetRotation - rotation

        if (angleOffset > 180) angleOffset -= 360
        if (angleOffset < -180) angleOffset += 360

        val direction = if (angleOffset >= 0) 1.0 else -1.0
        val magnitude = Math.abs(angleOffset)
        val rotationDelta = if (magnitude > 30) {
            direction * 5
        } else if (magnitude > 3) {
            direction * 2
        } else {
            direction
        }

        rotation += rotationDelta
        if (rotation > 180) rotation -= 360
        if (rotation < -180) rotation += 360
    } // rotateShip

    private fun applyThrust() {
        pos.move(velocity, game.extent)
    } // applyThrust

    //////////////////////////
    companion object {
        val Hyperspace = Point(-100000.0, -100000.0)

        private val shipBrush = Paint()
        private val shipFillBrush = Paint()
        private val thrustBrush = Paint()
        private val explodeBrush = Paint()
        private val redBrush = Paint()

        private val shipPath = Path()
        private val shape = floatArrayOf(
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
            shipBrush.setARGB(175, 0, 255, 0)
            shipBrush.strokeWidth = 10f
            shipBrush.strokeCap = Paint.Cap.ROUND
            shipBrush.style = Paint.Style.STROKE

            shipFillBrush.setARGB(255, 0, 0, 0)
            shipBrush.strokeWidth = 10f
            shipFillBrush.style = Paint.Style.FILL_AND_STROKE

            explodeBrush.setARGB(175, 255, 255, 0)
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
        val position: Point

        fun thrust()
        fun rotateTowards(angle: Double)

        fun update(fps: Long)
        fun draw(canvas: Canvas)

        fun explode()
    } // ShipState

    private class Flying(private val ship: Ship):
        ShipState {
        private var thrustFrames = 0

        override val position: Point
            get() = ship.pos

        override fun thrust() {
            val thrust = Vector(2.0, ship.rotation)
            ship.velocity += thrust

            thrustFrames = 10
            ship.game.sounds.play(ship.thrustSound)
        } // thrust

        override fun rotateTowards(angle: Double) {
            ship.targetRotation = angle
        } // rotateTowards

        override fun update(fps: Long) {
            if (thrustFrames > 0) --thrustFrames
        } // update

        override fun draw(canvas: Canvas) {
            canvas.drawPath(shipPath, shipFillBrush)
            canvas.drawLines(shape, shipBrush)

            if (thrustFrames != 0)
                canvas.drawLines(thruster, thrustBrush)
        } // draw

        override fun explode() {
            ship.state = Exploding(ship)
        } // explode
    } // Flying

    private class Exploding(private val ship: Ship):
        ShipState {
        private var explodeShape = shape.copyOf()
        private var explodeFrames = 50

        init {
            ship.game.sounds.play(ship.explosionSound)
        }

        override val position: Point get() = Hyperspace
        override fun thrust() = Unit
        override fun rotateTowards(angle: Double) = Unit

        override fun update(fps: Long) {
            blowUpShip()
        } // update

        override fun draw(canvas: Canvas) {
            canvas.drawLines(explodeShape, explodeBrush)
        } // draw

        override fun explode() = Unit

        private fun blowUpShip() {
            for (l in 0 until explodeShape.size step 4) {
                val x = blowUpShift(explodeShape[l])
                val y = blowUpShift(explodeShape[l + 3])

                for (p in 0 until 4 step 2) {
                    explodeShape[l + p] += x
                    explodeShape[l + 1 + p] += y
                }
            }

            --explodeFrames
            if (explodeFrames == 0) {
                ship.reset()
                ship.state = RezIn(ship)
            }
        } // blowUpShip

        private fun blowUpShift(p: Float): Float {
            if (p < 0) return -5f
            if (p > 0) return 5f
            return 0f
        } // blowUpShift
    } // Exploding

    private class RezIn(private val ship: Ship):
        ShipState {
        private var pause = 60
        private var radius = 600f

        override val position: Point get() = Hyperspace
        override fun thrust() = Unit
        override fun rotateTowards(angle: Double) {
            ship.pos.move(Vector(15.0, angle), ship.game.extent)
        }
        override fun update(fps: Long) {
            if (pause != 0) {
                if (--pause == 0)
                    ship.game.sounds.play(ship.rezInSound)
                return
            }
            radius -= (radius / 20)
            if (radius < 5)
                ship.state = Flying(ship)
        } // update

        override fun draw(canvas: Canvas) {
            if (pause != 0) {
                return
            }
            val r = (radius / 100).toInt()
            val brush = if ((r/2f) == (r/2).toFloat()) shipBrush else redBrush

            canvas.drawCircle(0f, 0f, radius, brush)
        } // draw

        override fun explode() = Unit
    } // RezIn
} // Ship

