package uk.co.jezuk.swoop.craft

interface Projectile: Craft {
    fun hit(impact: Target.Impact)
} // Projectile