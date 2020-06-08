package uk.co.jezuk.swoop.craft

interface Target: Craft {
    fun shot()
    fun explode()

    fun checkShipCollision(ship: Ship)
}