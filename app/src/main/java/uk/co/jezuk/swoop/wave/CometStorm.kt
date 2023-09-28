package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.utils.Latch
import kotlin.time.Duration.Companion.seconds

class CometStorm(
    game: Game,
    starField: StarField
) : WaveWithShip(game, starField, false) {
    private var comets = 0
    private var survivalBonus = true

    private var cometGun: Latch = Latch(3.seconds) { launchComet() }

    init {
        players.forAlive { it.onLifeLost { survivalBonus = false } }
    } // init

    private fun launchComet() {
        Comet(this)
        ++comets
        if (comets != 15)
            cometGun = Latch(1.5.seconds) { launchComet() }
        else
            targets.onEliminated { endOfLevel() }
    } // launchComet

    /////
    override fun update(frameRateScale: Float) {
        players.forAlive { it.gunOff() }
        cometGun.tick(frameRateScale)

        super.update(frameRateScale)
    } // update

    /////
    override fun endOfLevel() {
        if (survivalBonus) players.forAlive { it.scored(5000) }
        players.forAll { it.gunOn() }

        super.endOfLevel()
    } // endOfLevel
} // FlyAround