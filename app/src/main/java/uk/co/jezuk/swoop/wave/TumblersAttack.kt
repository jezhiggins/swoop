package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.utils.RepeatN

class TumblersAttack(
    game: Game,
    starField: StarField,
    initialAsteroids: Int,
    g: Gun?
) : WaveWithShip(game, starField, g) {
    private val tumblerLauncher = RepeatN(
        700 - (initialAsteroids * 50),
        initialAsteroids
    ) { Tumbler(game, this) }

    init {
        StonyAsteroid.field(game, this, initialAsteroids)

        targets.onEliminated { endOfLevel() }
    } // init

    override fun update(frameRateScale: Float) {
        super.update(frameRateScale)

        tumblerLauncher.tick(frameRateScale)
    } // update
} // class SaucerAttack