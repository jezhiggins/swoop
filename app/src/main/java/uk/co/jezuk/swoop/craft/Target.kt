package uk.co.jezuk.swoop.craft

interface Target: Craft {
    enum class Impact { HARD, SOFT, NONE }

    fun shipCollision(ship: Ship)
    fun shot(): Impact
    fun explode()

    fun checkShipCollision(ship: Ship) {
        if (Craft.collision(this, ship))
            shipCollision(ship)
    } // checkShipCollision
    fun checkProjectileCollision(projectiles: Projectiles) {
        val hitWith = projectiles.collision(this)
        hitWith?.hit(shot())
    } // checkProjectCollisions
} // Target