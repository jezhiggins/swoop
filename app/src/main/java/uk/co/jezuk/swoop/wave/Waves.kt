package uk.co.jezuk.swoop.wave

import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Projectiles
import uk.co.jezuk.swoop.wave.transition.LevelTransition

typealias WaveMaker = (Game, StarField) -> Wave

fun AsteroidsAndCometsMaker(initialAsteroids: Int): WaveMaker {
    return { game: Game, starField: StarField ->
        AsteroidsAndComets(game, starField, initialAsteroids)
    }
} // AsteroidsAndCometsMaker

fun CometStormMaker(game: Game, starField: StarField): Wave {
    return CometStorm(game, starField)
} // CometStormMaker

fun IronAsteroidsMaker(stonyAsteroids: Int, ironAsteroids: Int, gunReset: Boolean = false): WaveMaker {
    return { game: Game, starField: StarField ->
        IronAsteroids(game, starField, stonyAsteroids, ironAsteroids, gunReset)
    }
} // IronAsteroidsMaker

fun MinefieldMaker(minelayerDelay: Int, minelayerCount: Int, gunReset: Boolean = false): WaveMaker {
    return { game: Game, starField: StarField ->
        Minefield(game, starField, minelayerDelay, minelayerCount, gunReset)
    }
} // MinefieldMaker

fun TholianWebMaker(game: Game, starField: StarField): Wave {
    return TholianWeb(game, starField)
} // TholianWebMaker

fun TumblersAttackMaker(initialAsteroids: Int, gunReset: Boolean = false): WaveMaker {
    return { game: Game, starField: StarField ->
        TumblersAttack(game, starField, initialAsteroids, gunReset)
    }
} // SaucerAttackMaker

fun SaucerAttackMaker(initialAsteroids: Int, saucerAggressiveness: Int, gunReset: Boolean = false): WaveMaker {
    return { game: Game, starField: StarField ->
        SaucerAttack(game, starField, initialAsteroids, saucerAggressiveness, gunReset)
    }
} // SaucerAttackMaker

fun MagneticAsteroidsMaker(stonyAsteroids: Int, magneticAsteroids: Int, gunReset: Boolean = false): WaveMaker {
    return { game: Game, starField: StarField ->
        MagneticAsteroids(game, starField, stonyAsteroids, magneticAsteroids, gunReset)
    }
}

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
            TumblersAttackMaker(5, true),
            TumblersAttackMaker(6),
            TumblersAttackMaker(7),
            TumblersAttackMaker(8),
            SaucerAttackMaker(5, 1, true),
            SaucerAttackMaker(5, 2),
            SaucerAttackMaker(6, 3),
            SaucerAttackMaker(7, 4),
            MagneticAsteroidsMaker(0, 2, true),
            MagneticAsteroidsMaker(3, 2),
            MagneticAsteroidsMaker(3, 3),
            MagneticAsteroidsMaker(5, 3),
            IronAsteroidsMaker(4,8)
        )

        private var wave = 0

        fun from(game: Game, fromWave: Int, starField: StarField): Wave {
            wave = fromWave
            return waves[wave](game, starField)
        } // first

        fun next(game: Game, starField: StarField): Wave {
            ++wave
            if (wave == waves.size) wave = waves.size-1
            return waves[wave](game, starField)
        } // next

        fun transition(
            game: Game,
            starField: StarField,
            projectiles: Projectiles?
        ): Wave {
            val newStarField = StarField(Game.extent)
            val nextWave = next(game, newStarField)

            return LevelTransition(
                game,
                starField,
                newStarField,
                projectiles,
                nextWave,
                wave
            )
        } // transition
    } // companionObject
} // Waves