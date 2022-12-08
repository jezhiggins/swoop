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

    fun within(p: Point): Boolean =
        (p.x >= left) &&
        (p.x <= right) &&
        (p.y >= top) &&
        (p.y <= right)

    fun randomPoint() = Point(randomX(), randomY())

    fun randomPointOnEdge(): Point {
        return when (rollD4()) {
            0 -> Point(randomX(), top)
            1 -> Point(left, randomY())
            2 -> Point(right, randomY())
            else -> Point(randomX(), bottom)
        }
    } // randomPointOnEdge

    fun randomTraverse(): Array<Point> {
        return when (rollD4()) {
            0 -> arrayOf(Point(randomX(), top), Point(randomX(), bottom))
            1 -> arrayOf(Point(left, randomY()), Point(right, randomY()))
            2 -> arrayOf(Point(right, randomY()), Point(left, randomY()))
            else -> arrayOf(Point(randomX(), bottom), Point(randomX(), top))
        }
    } // randomTraverse

    fun randomHorizontalTraverse(): Array<Point> {
        return when (rollD4()) {
            0, 1 -> arrayOf(Point(left, randomY()), Point(right, randomY()))
            else -> arrayOf(Point(right, randomY()), Point(left, randomY()))
        }
    }

    private fun randomX() = Random.nextDouble(left, right)
    private fun randomY() = Random.nextDouble(top, bottom)
    private fun rollD4() = Random.nextInt(4)

    fun inflated(dist: Float): Extent {
        val doubleDist = (dist * 2).toInt()
        return Extent(width + doubleDist, height + doubleDist)
    }

    val WholeArea = bounds
    val LeftHalf = Rect(left.toInt(), top.toInt(), 0, bottom.toInt())
    val RightHalf = Rect(0, top.toInt(), right.toInt(), bottom.toInt())
}