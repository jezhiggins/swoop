package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.geometry.angleFromOffsets

abstract class WaveWithShip(
    protected val game: Game,
    private val starField: StarField,
    gunReset: Boolean = false
): WaveWithTargets() {
    final override val players: List<Player> = game.players
    private val projectiles: Projectiles = Projectiles()

    init {
        if (gunReset)
            players.forEach { it.gunReset() }
    }

    /////
    override fun onSingleTapUp(event: MotionEvent) = players.forEach { it.thrust() }
    override fun onScroll(offsetX: Float, offsetY: Float) {
        players.forEach { it.rotateTowards(
            angleFromOffsets(offsetX, offsetY)
        ) }
    } // onScroll
    override fun onLongPress() = players.forEach { it.thrust() }

    /////
    override fun update(frameRateScale: Float) {
        players.forEach { it.update(frameRateScale) }

        updateTargets(frameRateScale)
        updateProjectiles(frameRateScale)

        checkCollisions(players)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)

        drawTargets(canvas)
        drawProjectiles(canvas)

        players.forEach { it.draw(canvas) }
    } // draw

    /////
    protected open fun endOfLevel() {
        game.endOfWave(starField, projectiles)
    } // endOfLevel

    private fun updateProjectiles(frameRateScale: Float) {
        projectiles.update(frameRateScale)
    } // updateProjectiles

    private fun checkCollisions(players: List<Player>) {
        targets.iterator().forEach {
            it.checkProjectileCollision(projectiles)
        }
        targets.iterator().forEach {
            it.checkPlayerCollision(players)
        }
    } // checkCollisions

    private fun drawProjectiles(canvas: Canvas) {
        projectiles.draw(canvas)
    } // drawProjectiles

    override fun addProjectile(projectile: Projectile) {
        projectiles.add(projectile)
    } // addProjectile
    override fun removeProjectile(projectile: Projectile) {
        projectiles.remove(projectile)
    } // removeProjectile
} // WaveWithProjectiles