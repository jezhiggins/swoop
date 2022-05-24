package uk.co.jezuk.swoop.craft

import uk.co.jezuk.swoop.Player

interface Target: Craft {
    enum class Impact { HARD, SOFT, NONE }

    fun playerCollision(player: Player)
    fun shot(): Impact
    fun explode()

    fun checkShipCollision(player: Player) {
        if (Craft.collision(this, player))
            playerCollision(player)
    } // checkShipCollision
    fun checkProjectileCollision(projectiles: Projectiles) {
        val hitWith = projectiles.collision(this)
        hitWith?.hit(shot())
    } // checkProjectCollisions
} // Target