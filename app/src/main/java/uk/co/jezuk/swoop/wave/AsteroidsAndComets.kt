package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.utils.Latch

class AsteroidsAndComets(
    game: Game,
    starField: StarField,
    initialAsteroids: Int = 5
) : WaveWithShip(game, starField) {
    private var cometGun: Latch? = null

    init {
        StonyAsteroid.field(game, this, initialAsteroids)

        if (initialAsteroids > 5)
            cometGun = Latch(500 + (100 * (11-initialAsteroids))) { Comet(this) }

        targets.onEliminated { endOfLevel() }
    } // init

    /////
    override fun update(frameRateScale: Float) {
        cometGun?.tick(frameRateScale)

        super.update(frameRateScale)
    } // update
} // FlyAround