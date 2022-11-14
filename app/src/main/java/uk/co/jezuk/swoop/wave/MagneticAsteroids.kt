package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.craft.asteroid.IronAsteroid
import uk.co.jezuk.swoop.craft.asteroid.MagneticAsteroid
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid

class MagneticAsteroids (
    game: Game,
    starField: StarField,
    stonyAsteroids: Int = 5,
    magneticAsteroids: Int = 2,
    gunReset: Boolean = false
) : WaveWithShip(game, starField, gunReset) {
    init {
        MagneticAsteroid.field(game, this, magneticAsteroids)
        StonyAsteroid.field(game, this, stonyAsteroids)

        targets.onEliminated { endOfLevel() }
    } // init
} // IronAsteroids