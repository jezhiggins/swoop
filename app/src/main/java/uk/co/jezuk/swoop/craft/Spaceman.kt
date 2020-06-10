package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation
import uk.co.jezuk.swoop.wave.Wave

class Spaceman(
    game: Game,
    private val wave: Wave,
    pos: Point
) : Target {
    override val position = Point(pos)
    private val orientation = Rotation.random()
    override val killDist = 0f
    private val spaceman = game.loadBitmap(R.drawable.spaceman)

    init {
        wave.addTarget(this)
    } // init

    /////
    override fun update(frameRateScale: Float) {
    } // update

    override fun draw(canvas: Canvas) {
        canvas.save()

        position.translate(canvas)
        orientation.rotate(canvas)

        canvas.drawBitmap(spaceman.bitmap, 0f, 0f, null)

        canvas.restore()
    } // draw

    /////
    override fun shipCollision(ship: Ship) = Unit
    override fun shot(): Target.Impact = Target.Impact.NONE
    override fun explode() = Unit
} // Spaceman