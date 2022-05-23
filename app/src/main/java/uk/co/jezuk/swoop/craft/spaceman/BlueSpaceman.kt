package uk.co.jezuk.swoop.craft.spaceman

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.Wave
import uk.co.jezuk.swoop.R

class BlueSpaceman(
    private val game: Game,
    private val wave: Wave,
    pos: Point
) : Spaceman(game, wave, pos, R.drawable.bluespaceman) {
    private val savedSound = { Game.sound(R.raw.spacemansaved, position) }

    /////
    override fun shipCollision(ship: Ship) {
        game.scored(1500)
        wave.upgrade()

        super.shipCollision(ship)
    } // shipCollision
} // OrangeSpaceman