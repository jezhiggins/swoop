package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Saucer
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.utils.RepeatN
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class SaucerAttack(
    game: Game,
    starField: StarField,
    initialAsteroids: Int,
    saucerAggressiveness: Int,
    gunReset: Boolean = false
) : WaveWithShip(game, starField, gunReset) {
    private val saucerLaunch = RepeatN(10.seconds, saucerAggressiveness + 3, launchSaucer(saucerAggressiveness))

    init {
        StonyAsteroid.field(game, this, initialAsteroids)

        targets.onEliminated { endOfLevel() }
    } // init

    override fun update(frameRateScale: Float) {
        super.update(frameRateScale)

        saucerLaunch.tick(frameRateScale)
    } // update

    private fun launchSaucer(saucerAggressiveness: Int): () -> Unit {
        return {
            Saucer(this, saucerAggressiveness)
            val nextSaucerDelay = Random.nextDouble(6.0, 14.0).seconds
            saucerLaunch.reset(nextSaucerDelay)
        }
    } // launcherSaucer
} // class SaucerAttack