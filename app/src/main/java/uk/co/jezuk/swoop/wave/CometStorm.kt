package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.utils.Latch

class CometStorm(
    game: Game,
    starField: StarField
) : WaveWithShip(game, starField, false) {
    private var comets = 0
    private var survivalBonus = true

    private var cometGun: Latch = Latch(150) { launchComet() }

    init {
        players.forEach { it.onLifeLost { survivalBonus = false } }
    } // init

    private fun launchComet() {
        Comet(this)
        ++comets
        if (comets != 15)
            cometGun = Latch(70) { launchComet() }
        else
            targets.onEliminated { endOfLevel() }
    } // launchComet

    /////
    override fun update(frameRateScale: Float) {
        players.forEach { it.gunOff() }
        cometGun.tick(frameRateScale)

        super.update(frameRateScale)
    } // update

    /////
    override fun endOfLevel() {
        if (survivalBonus) players.forEach { it.scored(5000) }
        players.forEach { it.gunOn() }

        super.endOfLevel()
    } // endOfLevel
} // FlyAround