package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave

class Missile(
        private val game: Game,
        private val wave: Wave,
        override val position: Point,
        private val velocity: Vector,
        private val orientation: Float = velocity.angle.toFloat()
): Target {
    init {
        wave.addTarget(this)
    }

    override val killDist get() = 12f

    override fun update(frameRateScale: Float) {
        if (!position.moveNoWrap(velocity, frameRateScale, Game.extent, killDist))
            destroyed()
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)

        canvas.rotate(orientation)
        canvas.drawPath(missilePath, brush)

        canvas.restore()
    } // draw

    override fun shot(): Target.Effect {
        destroyed()
        Game.sound(R.raw.missileexplosion, position)
        return Target.Hard(100)
    } // shot

    override fun explode() { }

    override fun playerCollision(player: Player) = player.hit()

    private fun destroyed() = wave.removeTarget(this)

    companion object {
        val brush = Paint()
        private val shape = floatArrayOf(
            15f, 0f,
            -10f, 10f,
            -10f, -10f
        )
        val missilePath = Path()

        init {
            brush.setARGB(255, 255, 215, 0)
            brush.strokeWidth = 1f
            brush.strokeCap = Paint.Cap.BUTT
            brush.strokeJoin = Paint.Join.BEVEL
            brush.style = Paint.Style.FILL_AND_STROKE

            missilePath.moveTo(shape[0], shape[1])
            for (i in shape.indices step 2)
                missilePath.lineTo(shape[i], shape[i+1])
            missilePath.close()
        }
    } // companion object
} // class Missile