package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import uk.co.jezuk.swoop.utils.Latch
import kotlin.math.min

class CometStorm(
    private val game: Game,
    private val starField: StarField
) : WaveWithProjectilesAndTargets() {
    private val ship = Ship(game)
    private var comets = 0
    private var survivalBonus = true

    private var cometGun: Latch = Latch(120, { launchComet() })

    init {
        ship.onLifeLost { survivalBonus = false }
    } // init

    private fun launchComet() {
        Comet(game, this)
        ++comets
        if (comets != 15)
            cometGun = Latch(70, { launchComet() })
        else
            targets.onEliminated { endOfLevel() }
    } // launchComet

    /////
    override fun onSingleTapUp(event: MotionEvent) = ship.thrust()
    override fun onScroll(offsetX: Float, offsetY: Float) {
        ship.rotateTowards(
            angleFromOffsets(offsetX, offsetY)
        )
    } // onScroll
    override fun onLongPress() = ship.thrust()

    /////
    override fun update(frameRateScale: Float) {
        ship.update(frameRateScale)

        updateTargets(frameRateScale)
        updateProjectiles(frameRateScale)

        cometGun.tick(frameRateScale)

        checkCollisions(ship)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)

        drawTargets(canvas)
        drawProjectiles(canvas)

        ship.draw(canvas)
    } // draw

    /////
    private fun endOfLevel() {
        if (survivalBonus) game.scored(5000)

        game.endOfWave(starField, ship)
    } // endOfLevel
} // FlyAround