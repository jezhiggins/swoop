package uk.co.jezuk.swoop.wave.transition

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import uk.co.jezuk.swoop.*
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.craft.Comet
import uk.co.jezuk.swoop.craft.Saucer
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.utils.Repeat
import uk.co.jezuk.swoop.wave.StarField
import uk.co.jezuk.swoop.wave.WaveWithTargets
import kotlin.math.min
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class Attract(
    private val game: Game,
    highscore: HighScore
) : WaveWithTargets() {
    private val extent = Game.extent
    private val pureHighScore = highscore.pure()
    private val restartHighScore = highscore.restart()
    private val highWave = game.highWave
    private fun startScore(waveIndex: Int): Int = game.startScore(waveIndex)
    private fun startLives(waveIndex: Int): Int = game.startLives(waveIndex)

    private var starField = StarField(Game.extent)
    private val cometGun = Repeat(15.seconds) { Comet(this) }
    private val saucerGun = Repeat(40.seconds) { Saucer(this, Random.nextInt(1, 4)) }
    private var mode: AttractMode = TitleScreen()

    init {
        StonyAsteroid.field(
            game,
            this,
            Random.nextInt(2, 5),
            Random.nextInt(2, 7),
            Random.nextInt(2,6)
        ) { Game.extent.randomPoint() }
    }

    override fun onTap(x: Float, y: Float) {
        mode = mode.onSingleTapUp(x, y, this)
    } // onDown

    /////
    override fun update(frameRateScale: Float) {
        updateTargets(frameRateScale)
        cometGun.tick(frameRateScale)
        saucerGun.tick(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        drawTargets(canvas)
        titleDressing(canvas)

        mode.draw(canvas, this)
    } // draw

    private fun newGame(fromWave: Int, mode: GameMode) {
        game.nextWave(
            EndAttract(game, starField, targets, fromWave, mode)
        )
    } // newGame

    private interface AttractMode {
        fun onSingleTapUp(x: Float, y: Float, attract: Attract): AttractMode
        fun draw(canvas: Canvas, attract: Attract)
    } // AttractMode

    private fun titleDressing(canvas: Canvas) {
        val margin = 40.0
        val almostLeft = (extent.left + margin).toFloat()
        val almostRight = (extent.right - margin).toFloat()
        val justOffBottom = (extent.bottom - margin).toFloat()
        val justOffTop = (extent.top + margin*2).toFloat()
        //canvas.drawText("Forest Road Game Krew", 0.0f, justOffBottom, smallPen)
        tinyPen.textAlign = Paint.Align.LEFT
        canvas.drawText("Alright Bab!", almostLeft, justOffBottom, tinyPen)
        tinyPen.textAlign = Paint.Align.RIGHT
        canvas.drawText("Made in Birmingham and Wales", almostRight, justOffBottom, tinyPen)

        if (pureHighScore != 0)
            canvas.drawText(
                "High Score " + "$pureHighScore".padStart(6, '0'),
                0.0f,
                justOffTop,
                scorePen
            )
        if (restartHighScore != 0)
            canvas.drawText(
                "Jumpstart High Score " + "$restartHighScore".padStart(6, '0'),
                0.0f,
                justOffTop + 50,
                scorePen
            )
    } // draw

    private class TitleScreen: AttractMode {
        override fun onSingleTapUp(x: Float, y: Float, attract: Attract): AttractMode {
            if (tappedOnInfo(x, y, attract))
                return InfoScreen()
            return PlayerSelectScreen()
        } // onDown

        override fun draw(canvas: Canvas, attract: Attract) {
            canvas.drawText("SWOOP", 0.0f, 0.0f, pen)

            val infoX = (attract.extent.right - 120).toFloat()
            val infoY = (attract.extent.top + 120).toFloat()
            canvas.drawCircle(infoX, infoY, 100f, infoBrush)
            infoPen.style = Paint.Style.STROKE
            infoPen.strokeWidth = 8f
            canvas.drawCircle(infoX, infoY, 100f, infoPen)
            infoPen.style = Paint.Style.FILL_AND_STROKE
            infoPen.strokeWidth = 4f
            canvas.drawText("i", infoX, infoY+30f, infoPen)
        } // draw

        private fun tappedOnInfo(x: Float, y: Float, attract: Attract): Boolean {
            val extent = attract.extent

            val infoX = (extent.right - 120).toFloat()
            val infoY = (extent.top + 120).toFloat()

            return ((x >= infoX-120f && x <= infoX+120f) &&
                    (y >= infoY-120f && y <= infoY+120f))
        } // tappedOnInfo
    } // class TitleScreen

    private class PlayerSelectScreen(): AttractMode {
        override fun onSingleTapUp(x: Float, y: Float, attract: Attract): AttractMode {
            val mode = if (x < 0) Setup.OnePlayer else Setup.TwoPlayer
            return WaveStartScreen(attract, mode)
        }

        override fun draw(canvas: Canvas, attract: Attract) {
            canvas.drawText("Player Select",0.0f, -100.0f, infoPen)

            drawShip(Ship.Dart, Ship.GreenBrush, canvas, -325.0f, 90.0f)

            drawShip(Ship.Dart, Ship.GreenBrush, canvas, 280.0f, 90.0f)
            drawShip(Ship.Speeder, Ship.PinkBrush, canvas, 370.0f, 90.0f)
        } // draw

        private fun drawShip(ship: FloatArray, brush: Paint, canvas: Canvas, x: Float, y: Float) {
            canvas.save()

            canvas.translate(x, y)
            canvas.scale(1.25f, 1.25f)
            canvas.rotate(-90f)
            canvas.drawLines(ship, brush)

            canvas.restore()
        }
    }

    private class WaveStartScreen(attract: Attract, private val gameMode: GameMode): AttractMode {
        private val waveStride = 4
        private val maxWave = attract.highWave
        private val pads = mutableMapOf<Int, Float>()
        init {
            if(maxWave <= waveStride)
                attract.newGame(0, gameMode)

            val steps = (maxWave-1)/waveStride

            var x = (if (steps%2 != 0) -100.0f else 0.0f) - (200.0f * (steps/2))
            for (i in 0 until maxWave step waveStride) {
                pads[i] = x
                x += 200.0f
            }
        }

        override fun onSingleTapUp(x: Float, y: Float, attract: Attract): AttractMode {
            if (y > 0 && y < 300) {
                pads.forEach { (i, padX) ->
                    if ((padX > x-75) && (padX < x+75))
                        attract.newGame(i, gameMode)
                }
            }

            return this
        }

        override fun draw(canvas: Canvas, attract: Attract) {
            canvas.drawText("Start from Wave",0.0f, -100.0f, infoPen)

            for (i in 0 until maxWave step waveStride) {
                val x = pads[i]!!
                canvas.drawText("${i+1}", x, 270.0f, infoPen)
                canvas.drawText("${attract.startScore(i)}", x, 165.0f, scorePen)
                drawTinyLives(attract.startLives(i), canvas, x)
            }
        } // draw

        private fun drawTinyLives(lives: Int, canvas: Canvas, x: Float) {
            canvas.save()
            canvas.translate(x, 0f)

            val xoffset = 35f
            val yoffset = 45f
            canvas.translate(-xoffset, 75f)

            for (r in 0 until (lives/3) + 1) {
                for (c in (r * 3) until min(lives, (r * 3) + 3)) {
                    canvas.save()
                    canvas.rotate(-90f)
                    canvas.scale(0.25f, 0.25f)
                    canvas.drawLines(Ship.Dart, Ship.GreenBrush)
                    canvas.restore()
                    canvas.translate(xoffset, 0f)
                } // for
                canvas.translate(-xoffset*3, -yoffset)
            }

            canvas.restore()
        } // drawTinyLives
    } // AttractMode

    private class InfoScreen: AttractMode {
        override fun onSingleTapUp(x: Float, y: Float, attract: Attract) =
                TitleScreen()

        override fun draw(canvas: Canvas, attract: Attract) {
            infoPen.style = Paint.Style.FILL_AND_STROKE
            infoPen.strokeWidth = 4f

            val info = listOf(
                    "Swipe to steer. Tap to thrust.",
                    "Shoot what you can. Avoid what you can't.",
                    "Rescue those in peril."
            )

            for (i in info.indices)
                canvas.drawText(
                        info[i],
                        0f,
                        -120f + (120 * i),
                        infoPen
                )
        } // draw
    } // class InfoScreen

    companion object {
        private val pen = Paint()
        private val infoPen = Paint()
        private val infoBrush = Paint()
        private val smallPen = Paint()
        private val tinyPen = Paint()
        private val scorePen = Paint()

        init {
            pen.color = Color.WHITE
            pen.alpha = 255
            pen.textSize = 256f
            pen.textAlign = Paint.Align.CENTER

            infoPen.color = Color.WHITE
            infoPen.alpha = 255
            infoPen.textSize = 80f
            infoPen.textAlign = Paint.Align.CENTER
            infoPen.strokeWidth = 8f
            infoPen.style = Paint.Style.STROKE

            infoBrush.setARGB(255, 0, 0, 200)
            infoBrush.style = Paint.Style.FILL_AND_STROKE

            smallPen.setARGB(255, 0, 200, 0)
            smallPen.textSize = 48f
            smallPen.textSkewX = -.2f
            smallPen.textAlign = Paint.Align.CENTER

            tinyPen.setARGB(255, 0, 200, 0)
            tinyPen.textSize = 24f
            tinyPen.textSkewX = -.2f
            tinyPen.textAlign = Paint.Align.RIGHT

            scorePen.color = Color.CYAN
            scorePen.alpha = 255
            scorePen.textSize = 48f
            scorePen.textAlign = Paint.Align.CENTER
        } // init
    } // companion object
} // Attract

