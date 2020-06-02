package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint

class Asteroids {
    private val asteroids: MutableList<Asteroid> = mutableListOf()

    fun wave(count: Int, width: Int, height: Int) {
        asteroids.clear()
        for(a in 0 until count) {
            asteroids.add(
                Asteroid(
                    (Math.random() * width).toFloat(),
                    (Math.random() * height).toFloat(),
                    Vector(2.0, Math.random() * 360)
                )
            )
        }
    } // wave

    fun update(fps: Long, width: Int, height: Int) {
        asteroids.forEach { a -> a.update(fps, width, height) }
    }

    fun draw(canvas: Canvas) {
        asteroids.forEach { a -> a.draw(canvas) }
    }
}

class Asteroid(
    private var x: Float,
    private var y: Float,
    private var velocity: Vector
) {
    private val brush = Paint()

    init {
        brush.setARGB(127, 255, 255, 255)
        brush.strokeWidth = 3f
        brush.strokeJoin = Paint.Join.BEVEL
        brush.style = Paint.Style.STROKE
    }

    fun update(fps: Long, width: Int, height: Int) {
        val (deltaX, deltaY) = velocity.offset

        x += deltaX.toFloat()
        y += deltaY.toFloat()

        val halfWidth = width / 2f
        if (x < -halfWidth) x = halfWidth
        if (x > halfWidth) x = -halfWidth
        val halfHeight = height / 2f
        if (y < -halfHeight) y = halfHeight
        if (y > halfHeight) y = -halfHeight
    } // update

    fun draw(canvas: Canvas) {
        canvas.save()

        canvas.translate(x, y)
        canvas.translate(canvas.width/2f, canvas.height/2f)

        canvas.drawCircle(0f, 0f, 100f, brush)

        canvas.restore()
    } // draw

}