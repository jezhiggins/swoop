package uk.co.jezuk.swoop.craft.spaceman

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.Player
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.Wave
import uk.co.jezuk.swoop.R

class BlueSpaceman(
    private val game: Game,
    private val wave: Wave,
    pos: Point
) : Spaceman(game, wave, pos, R.drawable.bluespaceman) {
    /////
    override fun playerCollision(player: Player) {
        game.scored(1500)
        wave.upgrade()

        super.playerCollision(player)
    } // playerCollision
} // OrangeSpaceman