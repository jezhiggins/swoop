package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.craft.asteroid.IronAsteroid
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid

class IronAsteroids (
    game: Game,
    starField: StarField,
    stonyAsteroids: Int = 5,
    ironAsteroids: Int = 1,
    g: Gun? = null
) : WaveWithShip(game, starField, g) {
    init {
        IronAsteroid.field(game, this, ironAsteroids)
        StonyAsteroid.field(game, this, stonyAsteroids)

        targets.onEliminated { endOfLevel() }
    } // init
} // IronAsteroids