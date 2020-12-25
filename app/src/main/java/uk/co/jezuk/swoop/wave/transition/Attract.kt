package uk.co.jezuk.swoop.wave.transition

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.asteroid.StonyAsteroid
import uk.co.jezuk.swoop.craft.Comet
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.utils.Repeat
import uk.co.jezuk.swoop.wave.StarField
import uk.co.jezuk.swoop.wave.WaveWithTargets
import kotlin.math.min
import kotlin.random.Random

class Attract(
    private val game: Game
) : WaveWithTargets() {
    private val extent = game.extent
    private val highScore = game.highScore
    private val highWave = game.highWave
    private fun startScore(waveIndex: Int): Int = game.startScore(waveIndex)
    private fun startLives(waveIndex: Int): Int = game.startLives(waveIndex)

    private var starField = StarField(game.extent)
    val cometGun = Repeat(750, { Comet(game, this) })
    private var mode: AttractMode = TitleScreen()

    init {
        StonyAsteroid.field(
            game,
            this,
            Random.nextInt(2, 5),
            Random.nextInt(2, 7),
            Random.nextInt(2,6),
            { game.extent.randomPoint() }
        )
    }

    override fun onSingleTapUp(event: MotionEvent) {
        mode = mode.onSingleTapUp(event, this)
    } // onDown

    /////
    override fun update(frameRateScale: Float) {
        updateTargets(frameRateScale)
        cometGun.tick(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        drawTargets(canvas)

        mode.draw(canvas, this)
    } // draw

    private fun newGame(fromWave: Int) {
        game.nextWave(
            EndAttract(game, starField, targets, fromWave)
        )
    } // newGame

    private interface AttractMode {
        fun onSingleTapUp(event: MotionEvent, attract: Attract): AttractMode
        fun draw(canvas: Canvas, attract: Attract)
    } // AttractMode

    private abstract class TitleDressing: AttractMode {
        override fun draw(canvas: Canvas, attract: Attract) {
            val margin = 40.0
            val almostLeft = attract.extent.left + margin
            val almostRight = attract.extent.right - margin
            val justOffBottom = attract.extent.bottom - margin
            val justOffTop = attract.extent.top + margin*2
            drawText("Forest Road Game Krew", canvas, 0.0, justOffBottom, smallPen)
            tinyPen.textAlign = Paint.Align.LEFT
            drawText("Alright Bab!", canvas, almostLeft, justOffBottom, tinyPen)
            tinyPen.textAlign = Paint.Align.RIGHT
            drawText("Made in Birmingham", canvas, almostRight, justOffBottom, tinyPen)

            if (attract.highScore != 0)
                drawText(
                        "High Score " + "${attract.highScore}".padStart(6, '0'),
                        canvas,
                        0.0,
                        justOffTop,
                        scorePen
                )
        } // draw

        protected fun drawText(text: String, canvas: Canvas, x: Double, y: Double, pen: Paint) {
            canvas.drawText(text, x.toFloat(), y.toFloat(), pen)
        } // drawText
    } // class TitleScreen

    private class TitleScreen: TitleDressing() {
        override fun onSingleTapUp(event: MotionEvent, attract: Attract): AttractMode {
            if (tappedOnInfo(event, attract))
                return InfoScreen()
            return WaveStartScreen(attract)
        } // onDown

        override fun draw(canvas: Canvas, attract: Attract) {
            super.draw(canvas, attract)

            drawText("SWOOP", canvas,0.0, 0.0, pen)

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

        private fun tappedOnInfo(event: MotionEvent, attract: Attract): Boolean {
            val (x, y) = event

            val extent = attract.extent

            val infoX = (extent.right - 120).toFloat()
            val infoY = (extent.top + 120).toFloat()

            return ((x >= infoX-120f && x <= infoX+120f) &&
                    (y >= infoY-120f && y <= infoY+120f))
        } // tappedOnInfo
    } // class TitleScreen

    private class WaveStartScreen(attract: Attract): TitleDressing() {
        private val waveStride = 4
        private val maxWave = attract.highWave;
        private val pads = mutableMapOf<Int, Double>()
        init {
            if(maxWave <= waveStride)
                attract.newGame(0)

            val steps = (maxWave-1)/waveStride

            var x = (if (steps%2 != 0) -100.0 else 0.0) - (200.0 * (steps/2))
            for (i in 0 until maxWave step waveStride) {
                pads.put(i, x)
                x += 200.0
            }
        }

        override fun onSingleTapUp(event: MotionEvent, attract: Attract): AttractMode {
            var (x, y) = event

            if (y > 0 && y < 300) {
                pads.forEach { i, padX ->
                    if ((padX > x-75) && (padX < x+75))
                        attract.newGame(i)
                }
            }

            return this
        }

        override fun draw(canvas: Canvas, attract: Attract) {
            super.draw(canvas, attract)

            drawText("Start from Wave", canvas,0.0, -100.0, infoPen)

            for (i in 0 until maxWave step waveStride) {
                val x = pads[i]!!
                drawText("${i+1}", canvas, x, 270.0, infoPen)
                drawText("${attract.startScore(i)}", canvas, x, 165.0, scorePen)
                drawTinyLives(attract.startLives(i), canvas, x)
            }
        } // draw

        private fun drawTinyLives(lives: Int, canvas: Canvas, x: Double) {
            canvas.save()
            canvas.translate(x.toFloat(), 0f)

            val xoffset = 35f
            val yoffset = 45f
            canvas.translate(-xoffset, 75f)

            for (r in 0 until (lives/3) + 1) {
                for (c in (r * 3) until min(lives, (r * 3) + 3)) {
                    canvas.save()
                    canvas.rotate(-90f)
                    canvas.scale(0.25f, 0.25f)
                    canvas.drawLines(Ship.shape, Ship.shipBrush)
                    canvas.restore()
                    canvas.translate(xoffset, 0f)
                } // for
                canvas.translate(-xoffset*3, -yoffset)
            }

            canvas.restore()
        } // drawTinyLives
    } // AttractMode

    private class InfoScreen: AttractMode {
        override fun onSingleTapUp(event: MotionEvent, attract: Attract) =
                TitleScreen()

        override fun draw(canvas: Canvas, attract: Attract) {
            infoPen.style = Paint.Style.FILL_AND_STROKE
            infoPen.strokeWidth = 4f

            val info = listOf(
                    "Swipe to steer. Tap to thrust.",
                    "Shoot what you can. Avoid what you can't.",
                    "Rescue those in peril."
            )

            for (i in 0 until info.size)
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

private operator fun MotionEvent.component1(): Float = x
private operator fun MotionEvent.component2(): Float = y
