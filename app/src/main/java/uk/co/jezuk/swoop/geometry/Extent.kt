package uk.co.jezuk.swoop.geometry

class Extent(
    private val width: Int,
    private val height: Int
) {
    private val halfWidth = width/2.0
    private val l = -halfWidth
    private val r = halfWidth
    private val halfHeight = height/2.0
    private val t = -halfHeight
    private val b = halfHeight

    val left get() = l
    val right get() = r
    val top get() = t
    val bottom get() = b

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

    private fun randomX() = (Math.random() * width) - right
    private fun randomY() = (Math.random() * height) - bottom
    private fun rollD4() = (Math.random() * 4).toInt()
}