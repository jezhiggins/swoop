package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Minelayer
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import uk.co.jezuk.swoop.utils.RestartableLatch

class Minefield(
    game: Game,
    starField: StarField,
    minelayerDelay: Int,
    private val minelayerCount: Int,
    g: Gun?
): WaveWithShip(game, starField, g) {
    private val launcher = RestartableLatch(minelayerDelay, ::launchMinelayer)
    private var layerCount = 0

    init {
        StonyAsteroid.field(game, this, 6)

        launcher.start()
        targets.onEliminated { endOfLevel() }
    } // init

    private fun launchMinelayer() {
        ++layerCount
        Minelayer(game, this, { if (layerCount < minelayerCount) launcher.start() })
    } // launchMinelayer

    /////
    override fun update(frameRateScale: Float) {
        launcher.tick(frameRateScale)

        super.update(frameRateScale)
    } // update
} // Minefield