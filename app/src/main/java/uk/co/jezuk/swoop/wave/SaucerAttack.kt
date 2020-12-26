package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Saucer
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid

class SaucerAttack(
    game: Game,
    starField: StarField,
    initialAsteroids: Int = 5,
    g: Gun?
) : WaveWithShip(game, starField, g) {
    init {
        StonyAsteroid.field(game, this, initialAsteroids)
        Saucer(game, this)

        targets.onEliminated { endOfLevel() }
    }
}