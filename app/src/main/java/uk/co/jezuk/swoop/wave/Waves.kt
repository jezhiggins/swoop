package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Projectiles
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.wave.transition.LevelTransition

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
        val g = if (gunReset) null else gun
        IronAsteroids(game, starField, stonyAsteroid, ironAsteroids, g)
    }
} // IronAsteroidsMaker

fun MinefieldMaker(minelayerDelay: Int, minelayerCount: Int, gunReset: Boolean = false): WaveMaker {
    return { game: Game, starField: StarField, gun: Gun? ->
        val g = if (gunReset) null else gun
        Minefield(game, starField, minelayerDelay, minelayerCount, g)
    }
} // AsteroidsAndCometsMaker

fun TholianWebMaker(game: Game, starField: StarField, gun: Gun?): Wave {
    return TholianWeb(game, starField, gun)
} // TholianWebMaker

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
            IronAsteroidsMaker(0,8),
            MinefieldMaker(400, 5, true),
            MinefieldMaker(300, 5),
            MinefieldMaker(250, 7),
            ::TholianWebMaker,
            IronAsteroidsMaker(4,8)
        )

        private var wave = 0

        fun from(game: Game, fromWave: Int, starField: StarField): Wave {
            wave = fromWave
            return waves[wave](game, starField, null)
        } // first

        fun next(game: Game, starField: StarField, gun: Gun?): Wave {
            ++wave
            if (wave == waves.size) wave = waves.size-1
            return waves[wave](game, starField, gun)
        } // next

        fun transition(
            game: Game,
            starField: StarField,
            ship: Ship,
            projectiles: Projectiles?,
            gun: Gun?
        ): Wave {
            val newStarField = StarField(game.extent)
            val nextWave = next(game, newStarField, gun)

            return LevelTransition(
                game,
                starField,
                newStarField,
                ship,
                projectiles,
                nextWave,
                wave
            )
        } // transition
    } // companionObject
} // Waves