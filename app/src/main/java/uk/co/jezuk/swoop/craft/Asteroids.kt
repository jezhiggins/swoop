package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import uk.co.jezuk.swoop.geometry.Point

class Asteroids {
    private val asteroids: MutableList<Asteroid> = mutableListOf()

    fun wave(count: Int, width: Int, height: Int) {
        asteroids.clear()
        for(a in 0 until count) {
            add(
                Asteroid(
                    this,
                    Point(
                        (Math.random() * width),
                        (Math.random() * height)
                    )
                )
            )
        }
    } // wave

    fun add(asteroid: Asteroid) = asteroids.add(asteroid)
    fun remove(asteroid: Asteroid) = asteroids.remove(asteroid)

    fun update(fps: Long, width: Int, height: Int) {
        asteroids.forEach { a -> a.update(fps, width, height) }
    } // update

    fun draw(canvas: Canvas) {
        asteroids.forEach { a -> a.draw(canvas) }
    } // draw

    fun findCollisions(ship: Ship) {
        val working = mutableListOf<Asteroid>()
        working.addAll(asteroids)
        working.forEach {
            a -> a.checkShipCollision(ship)
        }
    } // findCollisions
} // Asteroids
