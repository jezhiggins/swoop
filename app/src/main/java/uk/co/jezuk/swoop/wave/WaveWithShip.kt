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
    val gunReset: Boolean = false,
    val gunActive: Boolean = true
): WaveWithTargets() {
    final override val player: Player = game.player
    private val projectiles: Projectiles = Projectiles()

    init {
        if (gunReset)
            player.gunReset()
        player.gunActive = gunActive
    }

    /////
    override fun onSingleTapUp(event: MotionEvent) = player.thrust()
    override fun onScroll(offsetX: Float, offsetY: Float) {
        player.rotateTowards(
            angleFromOffsets(offsetX, offsetY)
        )
    } // onScroll
    override fun onLongPress() = player.thrust()

    /////
    override fun update(frameRateScale: Float) {
        player.update(frameRateScale)

        updateTargets(frameRateScale)
        updateProjectiles(frameRateScale)

        checkCollisions(player)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)

        drawTargets(canvas)
        drawProjectiles(canvas)

        player.draw(canvas)
    } // draw

    /////
    protected open fun endOfLevel() {
        game.endOfWave(starField, projectiles)
    } // endOfLevel

    private fun updateProjectiles(frameRateScale: Float) {
        projectiles.update(frameRateScale)
    } // updateProjectiles

    private fun checkCollisions(player: Player) {
        targets.iterator().forEach {
            t -> t.checkProjectileCollision(projectiles)
        }
        targets.iterator().forEach {
            t -> t.checkShipCollision(player)
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