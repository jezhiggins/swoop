package uk.co.jezuk.swoop.craft

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Vector
import uk.co.jezuk.swoop.utils.Repeat

class Gun(
    private val game: Game,
    private val ship: Ship,
    private val targets: Targets
) {
    private val bullets = mutableListOf<Bullet>()
    private var trigger = Repeat(15, { fire() })
    private var ageOut = 75

    private fun fire() {
        if (!ship.armed) return

        val bullet = Bullet(
            game,
            ship.position,
            ship.orientation,
            ship.velocity
        )
        bullets.add(bullet)
    } // fire

    fun update(fps: Long) {
        bullets.forEach { b -> b.update(fps) }
        checkForHits()
        bullets.removeIf { b -> b.age >= ageOut }

        trigger.tick()
    } // update

    fun draw(canvas: Canvas) {
        bullets.forEach { b -> b.draw(canvas)}
    } // draw

    //////////////////////
    private fun checkForHits() {
        for (t in targets) {
            for (b in bullets) {
                if (Craft.collision(t, b)) {
                    t.shot()
                    b.hit()
                }
            }
        }
    } // checkForHits
} // Gun