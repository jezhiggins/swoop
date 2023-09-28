package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.utils.RepeatN
import kotlin.time.Duration.Companion.seconds

class TumblersAttack(
    game: Game,
    starField: StarField,
    initialAsteroids: Int,
    gunReset: Boolean = false
) : WaveWithShip(game, starField, gunReset) {
    private val tumblerLauncher = RepeatN(
        (14 - initialAsteroids).seconds,
        initialAsteroids - 1
    ) { Tumbler(this) }

    init {
        StonyAsteroid.field(game, this, initialAsteroids)

        targets.onEliminated { endOfLevel() }
    } // init

    override fun update(frameRateScale: Float) {
        super.update(frameRateScale)

        tumblerLauncher.tick(frameRateScale)
    } // update
} // class SaucerAttack