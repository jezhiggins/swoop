package uk.co.jezuk.swoop.wave

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import uk.co.jezuk.swoop.Game
import uk.co.jezuk.swoop.craft.Asteroid
import uk.co.jezuk.swoop.craft.Comet
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.utils.Repeat
import kotlin.random.Random

class Attract(
    private val game: Game
) : WaveWithTargets() {
    private var starField = StarField(game.extent)
    val cometGun = Repeat(750, { Comet(game, this) })
    private var infoMode = false

    init {
        Asteroid.field(
            game,
            this,
            Random.nextInt(2, 5),
            Random.nextInt(2, 7),
            Random.nextInt(2,6),
            { game.extent.randomPoint() }
        )
    }

    override fun onSingleTapUp(ev: MotionEvent) {
        val x = ev.x
        val y = ev.y

        if (!infoMode) {
            if ((x > game.extent.width - 250) && (y < 250))
                infoMode = true
            else
                game.nextWave(EndAttract(game, starField, targets))
        } else {
            infoMode = false
        }
    } // onDown

    /////
    override fun update(frameRateScale: Float) {
        updateTargets(frameRateScale)
        cometGun.tick(frameRateScale)
    } // update

    override fun draw(canvas: Canvas) {
        starField.draw(canvas)
        drawTargets(canvas)

        if (infoMode)
            drawInfo(canvas)
        else
            drawTitle(canvas)
    } // draw

    private fun drawTitle(canvas: Canvas) {
        drawText("SWOOP", canvas,0.0, 0.0, pen)

        val margin = 40.0
        val almostLeft = game.extent.left + margin
        val almostRight = game.extent.right - margin
        val justOffBottom = game.extent.bottom - margin
        val justOffTop = game.extent.top + margin*2
        drawText("Forest Road Game Krew", canvas, 0.0, justOffBottom, smallPen)
        tinyPen.textAlign = Paint.Align.LEFT
        drawText("Alright Bab!", canvas, almostLeft, justOffBottom, tinyPen)
        tinyPen.textAlign = Paint.Align.RIGHT
        drawText("Made in Birmingham", canvas, almostRight, justOffBottom, tinyPen)

        if (game.highScore != 0)
            drawText(
                "High Score " + "${game.highScore}".padStart(6, '0'),
                canvas,
                0.0,
                justOffTop,
                scorePen
            )

        val infoX = (game.extent.right - 120).toFloat()
        val infoY = (game.extent.top + 120).toFloat()
        canvas.drawCircle(infoX, infoY, 100f, infoBrush)
        infoPen.style = Paint.Style.STROKE
        infoPen.strokeWidth = 8f
        canvas.drawCircle(infoX, infoY, 100f, infoPen)
        infoPen.style = Paint.Style.FILL_AND_STROKE
        infoPen.strokeWidth = 4f
        canvas.drawText("i", infoX, infoY+30f, infoPen)
    } // drawTitle

    private fun drawInfo(canvas: Canvas) {
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
    } // drawInfo

    private fun drawText(text: String, canvas: Canvas, x: Double, y: Double, pen: Paint) {
        canvas.drawText(text, x.toFloat(), y.toFloat(), pen)
    } // drawText

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