package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Minelayer
import uk.co.jezuk.swoop.craft.asteroid.IronAsteroid
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.utils.Latch

class TholianWeb(
    game: Game,
    starField: StarField
): WaveWithShip(game, starField, false) {
    private val nothing = { }

    private val latches = arrayListOf(
        Latch(50, ::verticalTraverse),
        Latch(250, ::horizontalTraverse),
        Latch(500, ::cornerTraverse),
        Latch(750, ::hereComeTheAsteroids),
        Latch(2000, ::letsGoIron)
    )

    private fun verticalTraverse() {
        val offsetX = Game.extent.width / 6.0
        val traverses = arrayOf(
            arrayOf(Point(offsetX, Game.extent.top), Point(offsetX, Game.extent.bottom)),
            arrayOf(Point(-offsetX, Game.extent.bottom), Point(-offsetX, Game.extent.top))
        )
        traverses.forEach { t -> Minelayer(game, this, nothing, t, true) }
    } // verticalTraverse

    private fun horizontalTraverse() {
        val offsetY = Game.extent.height / 6.0
        val traverses = arrayOf(
            arrayOf(Point(Game.extent.left, offsetY), Point(Game.extent.right, offsetY)),
            arrayOf(Point(Game.extent.right, -offsetY), Point(Game.extent.top, -offsetY))
        )
        traverses.forEach { t -> Minelayer(game, this, nothing, t, true) }
    } // horizontalTraverse

    private fun cornerTraverse() {
        val offsetX = Game.extent.width / 6.0
        val offsetY = Game.extent.height / 6.0
        val traverses = arrayOf(
            arrayOf(Point(Game.extent.left, -offsetY), Point(-offsetX, Game.extent.top)),
            arrayOf(Point(Game.extent.left, offsetY), Point(-offsetX, Game.extent.bottom)),
            arrayOf(Point(Game.extent.right, -offsetY), Point(offsetX, Game.extent.top)),
            arrayOf(Point(Game.extent.right, offsetY), Point(offsetX, Game.extent.bottom))
        )
        traverses.forEach { t -> Minelayer(game, this, nothing, t, true) }
    } // cornerTraverse

    private fun hereComeTheAsteroids() {
        StonyAsteroid(game, this, Point(0.0, Game.extent.top))
        StonyAsteroid(game, this, Point(0.0, Game.extent.bottom))
        StonyAsteroid(game, this, Point(Game.extent.left, 0.0))
        StonyAsteroid(game, this, Point(Game.extent.right, 0.0))

        targets.onEliminated { endOfLevel() }
    } // hereComeTheAsteroids

    private fun letsGoIron() {
        IronAsteroid(game, this, Point(Game.extent.left, Game.extent.top))
        IronAsteroid(game, this, Point(Game.extent.left, Game.extent.bottom))
        IronAsteroid(game, this, Point(Game.extent.right, Game.extent.top))
        IronAsteroid(game, this, Point(Game.extent.right, Game.extent.bottom))
    } // letsGoIron

    /////
    override fun update(frameRateScale: Float) {
        latches.forEach { it.tick(frameRateScale) }

        super.update(frameRateScale)
    } // update
} // Emptiness