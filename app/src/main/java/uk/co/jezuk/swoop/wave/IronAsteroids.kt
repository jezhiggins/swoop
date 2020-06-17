package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.craft.asteroid.IronAsteroid
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import uk.co.jezuk.swoop.utils.Latch
import kotlin.math.min

class IronAsteroids (
    private val game: Game,
    private val starField: StarField,
    private val initialAsteroids: Int = 5,
    g: Gun? = null
) : WaveWithProjectilesAndTargets() {
    private val ship: Ship = Ship(game)
    private val gun: Gun = Gun(game, this, ship, g)

    init {
        IronAsteroid(game, this, game.extent.randomPointOnEdge())
        targets.onEliminated { endOfLevel() }
    } // init

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
        gun.update(frameRateScale)
        ship.update(frameRateScale)

        updateTargets(frameRateScale)
        updateProjectiles(frameRateScale)

        checkCollisions(ship)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)

        drawTargets(canvas)
        drawProjectiles(canvas)

        ship.draw(canvas)
    } // draw

    override fun upgrade() {
        gun.upgrade()
    } // upgrade

    /////
    private fun endOfLevel() {
        val newStarField = StarField(game.extent)

        val numberOfAsteroids = initialAsteroids+1
        val nextWave = if (numberOfAsteroids != 8)
            AsteroidsAndComets(game, newStarField, min(numberOfAsteroids, 11), gun)
        else
            CometStorm(game, newStarField)

        game.nextWave(LevelTransition(
            game,
            starField,
            newStarField,
            ship,
            projectiles,
            nextWave
        ))
    } // endOfLevel
} // IronAsteroids