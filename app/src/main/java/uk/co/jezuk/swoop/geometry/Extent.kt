package uk.co.jezuk.swoop.geometry

import android.graphics.Rect
import kotlin.random.Random

class Extent(
    val width: Int,
    val height: Int
) {
    private val halfWidth = width/2.0
    val left = -halfWidth
    val right = halfWidth
    private val halfHeight = height/2.0
    val top = -halfHeight
    val bottom = halfHeight
    val bounds = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

    val canvasOffsetX get() = halfWidth.toFloat()
    val canvasOffsetY get() = halfHeight.toFloat()

    fun randomPoint() = Point(randomX(), randomY())

    fun randomPointOnEdge(): Point {
        return when (rollD4()) {
            0 -> Point(randomX(), top)
            1 -> Point(left, randomY())
            2 -> Point(right, randomY())
            else -> Point(randomX(), bottom)
        }
    }

    private fun randomX() = Random.nextDouble(left, right)
    private fun randomY() = Random.nextDouble(top, bottom)
    private fun rollD4() = Random.nextInt(4)

    fun inflated(dist: Float): Extent {
        val doubleDist = (dist * 2).toInt()
        return Extent(width + doubleDist, height + doubleDist)
    }
}