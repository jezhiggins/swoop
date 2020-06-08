package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.Projectile
import uk.co.jezuk.swoop.craft.Projectiles
import uk.co.jezuk.swoop.craft.Targets

abstract class WaveWithProjectilesAndTargets(
    protected val projectiles: Projectiles = Projectiles(),
    targets: Targets = Targets()
): WaveWithTargets(targets) {
    protected fun updateProjectiles(fps: Long) {
        projectiles.update(fps)
    } // updateProjectiles

    protected fun checkCollisions(ship: Ship) {
        targets.iterator().forEach {
            t -> t.checkProjectileCollision(projectiles) ||
                 t.checkShipCollision(ship)
        }
    } // checkCollisions

    protected fun drawProjectiles(canvas: Canvas) {
        projectiles.draw(canvas)
    } // drawProjectiles

    override fun addProjectile(projectile: Projectile) {
        projectiles.add(projectile)
    } // addProjectile
    override fun removeProjectile(projectile: Projectile) {
        projectiles.remove(projectile)
    } // removeProjectile
} // WaveWithProjectiles