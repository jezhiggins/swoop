package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Comet
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Missile
import uk.co.jezuk.swoop.craft.Saucer
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.utils.Latch
import uk.co.jezuk.swoop.utils.RepeatN
import kotlin.random.Random

class SaucerAttack(
    game: Game,
    starField: StarField,
    initialAsteroids: Int,
    saucerAggressiveness: Int,
    g: Gun?
) : WaveWithShip(game, starField, g) {
    private val saucerLaunch = RepeatN(500, saucerAggressiveness + 3, launchSaucer(saucerAggressiveness))

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
            Saucer(game, this, saucerAggressiveness)
            saucerLaunch.reset(Random.nextInt(300, 700))
        }
    } // launcherSaucer
} // class SaucerAttack