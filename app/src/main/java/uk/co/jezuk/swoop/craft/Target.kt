package uk.co.jezuk.swoop.craft

interface Target: Craft {
    fun shipCollision(ship: Ship)
    fun shot()
    fun explode()

    fun checkShipCollision(ship: Ship): Boolean {
        val hit = Craft.collision(this, ship)
        if (hit) shipCollision(ship)
        return hit
    } // checkShipCollision
    fun checkProjectileCollision(projectiles: Projectiles): Boolean {
        val hit = projectiles.collision(this)
        if (hit) shot()
        return hit
    } // checkProjectCollisions
} // Target