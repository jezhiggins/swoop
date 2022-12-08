package uk.co.jezuk.swoop.craft

import uk.co.jezuk.swoop.Player

interface Target: Craft {
    enum class Impact { HARD, SOFT, NONE }
    data class Effect(val impact: Impact, val score: Int)

    fun playerCollision(player: Player)
    fun shot(): Effect
    fun explode()

    fun checkPlayerCollision(players: List<Player>) {
        players.forEach {
            if (Craft.collision(this, it))
                playerCollision(it)
        }
    } // checkPlayerCollision
    fun checkProjectileCollision(projectiles: Projectiles) {
        val hitWith = projectiles.collision(this)
        hitWith?.hit(shot())
    } // checkProjectCollisions

    companion object {
        val NoEffect = Effect(Impact.NONE, 0)
        fun Soft(score: Int = 0) = Effect(Impact.SOFT, score)
        fun Hard(score: Int = 0) = Effect(Impact.HARD, score)
    }
} // Target