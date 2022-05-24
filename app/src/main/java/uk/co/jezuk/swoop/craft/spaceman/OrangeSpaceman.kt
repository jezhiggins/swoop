package uk.co.jezuk.swoop.craft.spaceman

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.Wave
import uk.co.jezuk.swoop.R

class OrangeSpaceman(
    game: Game,
    wave: Wave,
    pos: Point
) : Spaceman(game, wave, pos, R.drawable.orangespaceman) {
} // OrangeSpaceman