package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.wave.Wave

class Puff(
    private val wave: Wave,
    pos: Point,
    private val maxSize: Float = 12f
) : Target {
    override val position = Point(pos)
    private val orientation = Rotation.random()
    override val killDist = 0f
    private var size = 2f

    init {
        wave.addTarget(this)
    } // init

    /////
    override fun update(frameRateScale: Float) {
        size += 0.2f
        if (size > maxSize)
            wave.removeTarget(this)
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        orientation.rotate(canvas)

        for (i in 0 until splots.size step 2) {
            canvas.drawCircle(
                splots[i] * size,
                splots[i+1] * size,
                3f,
                brush
            )
        } // for

        canvas.restore()
    } // draw

    /////
    override fun playerCollision(player: Player) = Unit
    override fun shot(): Target.Impact = Target.Impact.NONE
    override fun explode() = Unit

    companion object {
        val splots = floatArrayOf(
            -2f, 0f,
            -2f, -2f,
            2f, -2f,
            3f, 1f,
            2f, -1f,
            0f, 2f,
            1f, 3f,
            -1f, 3f,
            -4f, -1f,
            -3f, 1f
        )

        val brush = Paint()

        init {
            brush.setARGB(180, 160, 160, 80)
            brush.strokeWidth = 3f
            brush.strokeCap = Paint.Cap.ROUND
            brush.strokeJoin = Paint.Join.ROUND
            brush.style = Paint.Style.FILL_AND_STROKE
        } // init
    } // companion
} // Pull

fun BigPuff(wave: Wave, pos: Point) {
    Puff(wave, pos, 50f)
    Puff(wave, pos, 30f)
    Puff(wave, pos, 30f)
    Puff(wave, pos, 30f)
    Puff(wave, pos, 20f)
    Puff(wave, pos, 20f)
}