package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Projectiles
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid

typealias WaveMaker = (Game, StarField, Gun?) -> Wave

fun AsteroidsAndCometsMaker(initialAsteroids: Int): WaveMaker {
    return { game: Game, starField: StarField, gun: Gun? ->
        AsteroidsAndComets(game, starField, initialAsteroids, gun)
    }
} // AsteroidsAndCometsMaker

fun CometStormMaker(game: Game, starField: StarField, gun: Gun?): Wave {
    return CometStorm(game, starField)
} // CometStormMaker

fun IronAsteroidsMaker(stonyAsteroid: Int, ironAsteroids: Int, gunReset: Boolean = false): WaveMaker {
    return { game: Game, starField: StarField, gun: Gun? ->
        val g = if (gunReset) gun else null
        IronAsteroids(game, starField, stonyAsteroid, ironAsteroids, g)
    }
} // IronAsteroidsMaker

class Waves {
    companion object {
        private val waves = listOf<WaveMaker>(
            AsteroidsAndCometsMaker(5),
            AsteroidsAndCometsMaker(6),
            AsteroidsAndCometsMaker(7),
            ::CometStormMaker,
            IronAsteroidsMaker(5, 1, true),
            IronAsteroidsMaker(6, 2),
            IronAsteroidsMaker(6, 4),
            IronAsteroidsMaker(0,8)
        )

        private var wave = 0

        fun first(game: Game, starField: StarField): Wave {
            wave = 0
            return waves[wave](game, starField, null)
        } // first

        fun next(game: Game, starField: StarField, gun: Gun?): Wave {
            if (wave != waves.size) ++wave
            return waves[wave](game, starField, gun)
        } // next

        fun transition(
            game: Game,
            starField: StarField,
            ship: Ship,
            projectiles: Projectiles?,
            gun: Gun?): Wave {

            val newStarField = StarField(game.extent)
            val nextWave = next(game, newStarField, gun)

            return LevelTransition(
                game,
                starField,
                newStarField,
                ship,
                projectiles,
                nextWave
            )
        } // transition
    } // companionObject
} // Waves