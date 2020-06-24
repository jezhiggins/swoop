package uk.co.jezuk.swoop.craft.spaceman

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.Wave
import uk.co.jezuk.swoop.R

class OrangeSpaceman(
    private val game: Game,
    private val wave: Wave,
    pos: Point
) : Spaceman(game, wave, pos, R.drawable.orangespaceman) {
    private val savedSound = { game.sound(R.raw.spacemansaved, position) }

    /////
    override fun shipCollision(ship: Ship) {
        savedSound()
        game.scored(1000)
        wave.upgrade()
        wave.removeTarget(this)
    } // shipCollision
} // OrangeSpaceman