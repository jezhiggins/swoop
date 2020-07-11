package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Minelayer
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.asteroid.IronAsteroid
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.angleFromOffsets
import uk.co.jezuk.swoop.utils.Latch

class TholianWeb(
    private val game: Game,
    private val starField: StarField,
    g: Gun?
): WaveWithProjectilesAndTargets() {
    private val ship = Ship(game)
    private val gun = Gun(game, this, ship, g)

    private val nothing = { }

    private val latches = arrayListOf(
        Latch(50, ::verticalTraverse),
        Latch(250, ::horizontalTraverse),
        Latch(500, ::cornerTraverse),
        Latch(750, ::hereComeTheAsteroids),
        Latch(2000, ::letsGoIron)
    )

    private fun verticalTraverse() {
        val offsetX = game.extent.width / 6.0;
        val traverses = arrayOf(
            arrayOf(Point(offsetX, game.extent.top), Point(offsetX, game.extent.bottom)),
            arrayOf(Point(-offsetX, game.extent.bottom), Point(-offsetX, game.extent.top))
        )
        traverses.forEach { t -> Minelayer(game, this, nothing, t, true) }
    } // verticalTraverse

    private fun horizontalTraverse() {
        val offsetY = game.extent.height / 6.0;
        val traverses = arrayOf(
            arrayOf(Point(game.extent.left, offsetY), Point(game.extent.right, offsetY)),
            arrayOf(Point(game.extent.right, -offsetY), Point(game.extent.top, -offsetY))
        )
        traverses.forEach { t -> Minelayer(game, this, nothing, t, true) }
    } // horizontalTraverse

    private fun cornerTraverse() {
        val offsetX = game.extent.width / 6.0;
        val offsetY = game.extent.height / 6.0;
        val traverses = arrayOf(
            arrayOf(Point(game.extent.left, -offsetY), Point(-offsetX, game.extent.top)),
            arrayOf(Point(game.extent.left, offsetY), Point(-offsetX, game.extent.bottom)),
            arrayOf(Point(game.extent.right, -offsetY), Point(offsetX, game.extent.top)),
            arrayOf(Point(game.extent.right, offsetY), Point(offsetX, game.extent.bottom))
        )
        traverses.forEach { t -> Minelayer(game, this, nothing, t, true) }
    } // cornerTraverse

    private fun hereComeTheAsteroids() {
        StonyAsteroid(game, this, Point(0.0, game.extent.top))
        StonyAsteroid(game, this, Point(0.0, game.extent.bottom))
        StonyAsteroid(game, this, Point(game.extent.left, 0.0))
        StonyAsteroid(game, this, Point(game.extent.right, 0.0))

        targets.onEliminated { endOfLevel() }
    } // hereComeTheAsteroids

    private fun letsGoIron() {
        IronAsteroid(game, this, Point(game.extent.left, game.extent.top))
        IronAsteroid(game, this, Point(game.extent.left, game.extent.bottom))
        IronAsteroid(game, this, Point(game.extent.right, game.extent.top))
        IronAsteroid(game, this, Point(game.extent.right, game.extent.bottom))
    } // letsGoIron

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
        latches.forEach { it.tick(frameRateScale) }

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
        game.endOfWave(starField, ship, projectiles, gun)
    } // endOfLevel
} // Emptiness