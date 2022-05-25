package uk.co.jezuk.swoop.craft

interface Projectile: Craft {
    fun hit(effect: Target.Effect)
} // Projectile