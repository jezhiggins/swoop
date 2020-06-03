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

    fun randomPoint() =
        Point(
            (Math.random() * width) - right,
            (Math.random() * height) - bottom
        )
}