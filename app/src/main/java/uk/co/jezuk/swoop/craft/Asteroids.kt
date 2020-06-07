package uk.co.jezuk.swoop.craft

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.Wave

fun Asteroids(
    game: Game,
    wave: Wave,
    big: Int,
    medium: Int = 0,
    small: Int = 0,
    originFn: () -> Point = { game.extent.randomPointOnEdge() }
) {
    val sizes = mapOf(
        Pair(big, Asteroid.Big),
        Pair(medium, Asteroid.Medium),
        Pair(small, Asteroid.Small)
    )
    for ((count, size) in sizes) {
        for(a in 0 until count) {
            Asteroid(
                game,
                wave,
                originFn(),
                size
            )
        }
    }
} // Asteroids
