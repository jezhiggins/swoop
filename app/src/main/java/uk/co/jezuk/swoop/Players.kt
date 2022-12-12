package uk.co.jezuk.swoop

class Players {
    private val players = mutableListOf<Player>()

    fun add(onDied: () -> Unit, mode: Mode) = players.add(Player(onDied, mode))
    fun clear() = players.clear()

    fun forAll(fn: (Player) -> Unit) = players.forEach(fn)
    fun forAlive(fn: (Player) -> Unit) = players.forEach { if (it.alive) fn(it) }

    fun forTouched(x: Float, y: Float, fn: (Player) -> Unit) =
        players.filter { it.touchArea.contains(x.toInt(), y.toInt()) }
            .forEach(fn)

    fun randomChoice(): Player = players.filter { it.alive }.random()

    fun allDead() = players.none { it.alive }
}