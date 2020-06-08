package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import uk.co.jezuk.swoop.geometry.Point

interface Target {
    val position: Point
    val killDist: Float

    fun update(fps: Long)
    fun draw(canvas: Canvas)

    fun shot()
    fun explode()

    fun checkShipCollision(ship: Ship)
}