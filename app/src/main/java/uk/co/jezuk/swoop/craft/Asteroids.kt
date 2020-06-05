package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.R
import uk.co.jezuk.swoop.geometry.Point

class Asteroids(
    private val game: Game,
    big: Int,
    medium: Int = 0,
    small: Int = 0,
    originFn: () -> Point = { game.extent.randomPointOnEdge() }
) {
    private val asteroids = mutableListOf<Asteroid>()
    private val smallBang = game.sounds.load(R.raw.bangsmall)
    private val midBang = game.sounds.load(R.raw.bangmedium)
    private val bigBang = game.sounds.load(R.raw.banglarge)

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
                        originFn(),
                        size
                    )
                )
            }
        }
    } // init

    operator fun iterator() = ArrayList(asteroids).iterator()
    val size: Int get() = asteroids.size
    fun add(asteroid: Asteroid) = asteroids.add(asteroid)
    fun remove(asteroid: Asteroid) = asteroids.remove(asteroid)
    fun explodeLast() {
        val last = asteroids.last()
        asteroids.remove(last)
        bang(last)
    } // explodeLast

    fun update(fps: Long) {
        asteroids.forEach { a -> a.update(fps) }
    } // update

    fun draw(canvas: Canvas) {
        asteroids.forEach { a -> a.draw(canvas) }
    } // draw

    fun findCollisions(ship: Ship) {
        val working = ArrayList(asteroids)
        working.forEach {
            a -> a.checkShipCollision(ship)
        }
    } // findCollisions

    fun bang(asteroid: Asteroid) {
        val b = when(asteroid.size) {
            1 -> smallBang
            2 -> midBang
            else -> bigBang
        }
        b(asteroid.position.pan(extent))
    } // bang

    val extent = game.extent
    fun scored(add: Int) = game.scored(add)
} // Asteroids
