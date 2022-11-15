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
        player.gunOff()
        player.onLifeLost { survivalBonus = false }
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
        cometGun.tick(frameRateScale)

        super.update(frameRateScale)
    } // update

    /////
    override fun endOfLevel() {
        if (survivalBonus) player.scored(5000)
        player.gunOn()

        super.endOfLevel()
    } // endOfLevel
} // FlyAround