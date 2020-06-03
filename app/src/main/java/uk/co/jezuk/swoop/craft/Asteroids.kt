package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point

class Asteroids(
    private val game: Game,
    big: Int,
    medium: Int = 0,
    small: Int = 0
) {
    private val asteroids: MutableList<Asteroid> = mutableListOf()

    init {
        val sizes = mapOf(
            Pair(big, Asteroid.Big),
            Pair(medium, Asteroid.Medium),
            Pair(small, Asteroid.Small)
        )
        for ((count, size) in sizes) {
            for(a in 0 until count) {
                add(
                    Asteroid(
                        this,
                        Point((Math.random() * game.width), (Math.random() * game.height)),
                        size
                    )
                )
            }
        }
    } // init

    val size: Int get() = asteroids.size
    fun add(asteroid: Asteroid) = asteroids.add(asteroid)
    fun remove(asteroid: Asteroid) = asteroids.remove(asteroid)
    fun explodeLast() {
        asteroids.remove(asteroids.last())
        pop()
    }

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

    fun pop() = game.sounds.pop()
} // Asteroids
