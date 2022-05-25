package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.wave.Wave

class Explosion(
    private val wave: Wave,
    pos: Point,
    private val velocity: Vector,
    initialOrientation: Rotation,
    private val rotation: Double,
    shape: FloatArray,
    brush: Paint,
    length: Int,
    useMid: Boolean = true,
    expansion: Float = 5f
) : Target {
    private val exploder = Exploder(
        { wave.removeTarget(this) },
        shape,
        brush,
        length,
        useMid,
        expansion
    )
    override val position = pos.copy()
    override val killDist = 0f
    private val orientation = initialOrientation.clone()

    init {
        wave.addTarget(this)
    }

    override fun update(frameRateScale: Float) {
        position.drift(velocity, frameRateScale)
        exploder.update(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        orientation.rotate(canvas)

        exploder.draw(canvas)

        canvas.restore()
    } // draw

    override fun playerCollision(player: Player) { }
    override fun shot(): Target.Effect = Target.NoEffect
    override fun explode() {}
}