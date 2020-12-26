package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.*
import uk.co.jezuk.swoop.geometry.angleFromOffsets

abstract class WaveWithShip(
    protected val game: Game,
    private val starField: StarField,
    g: Gun? = null,
    private val activeGun: Boolean = true
): WaveWithTargets() {
    final override val ship: Ship = Ship(game)
    private val gun: Gun = Gun(game, this, ship, g)
    private val projectiles: Projectiles = Projectiles()

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
        if (activeGun)
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
    protected open fun endOfLevel() {
        game.endOfWave(starField, ship, projectiles, gun)
    } // endOfLevel

    private fun updateProjectiles(frameRateScale: Float) {
        projectiles.update(frameRateScale)
    } // updateProjectiles

    private fun checkCollisions(ship: Ship) {
        targets.iterator().forEach {
            t -> t.checkProjectileCollision(projectiles)
        }
        targets.iterator().forEach {
            t -> t.checkShipCollision(ship)
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