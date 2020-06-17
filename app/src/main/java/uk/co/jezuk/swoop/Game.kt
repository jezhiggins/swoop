package uk.co.jezuk.swoop

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import androidx.core.content.edit
import androidx.core.graphics.withMatrix
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.Extent
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.*
import kotlin.math.min

class Game(private val context: Context) {
    enum class NextShip { Continue, End }
    private var wave: Wave = Emptiness()
    private val ext = Extent(1920, 1080)
    private var scaleMatrix = Matrix()
    private var touchMatrix = Matrix()
    private val sounds = Sounds(context)
    private var lives = 0
    private var score = -1

    fun setExtent(width: Int, height: Int) {
        val screenExt = Extent(width, height)
        val scaleX = screenExt.width.toFloat() / ext.width
        val scaleY = screenExt.height.toFloat() / ext.height
        val screenScale = min(scaleX, scaleY)

        val scaledWidth = (ext.width * screenScale).toInt()
        val scaledHeight = (ext.height * screenScale).toInt()

        val screenOffsetX = (screenExt.width - scaledWidth) / 2f
        val screenOffsetY = (screenExt.height - scaledHeight) / 2f

        scaleMatrix = Matrix()
        scaleMatrix.preTranslate(screenOffsetX, screenOffsetY)
        scaleMatrix.preScale(screenScale, screenScale)
        scaleMatrix.preTranslate(extent.canvasOffsetX, extent.canvasOffsetY)

        touchMatrix = Matrix()
        touchMatrix.preTranslate(-extent.canvasOffsetX, -extent.canvasOffsetY)
        touchMatrix.preScale(1/screenScale, 1/screenScale)
        touchMatrix.preTranslate(-screenOffsetX, -screenOffsetY)

        attract()
    } // setExtent

    fun attract() {
        wave = Attract(this)
    } // attract

    fun start() {
        lives = 3
        score = 0
        newHighScore = false
    } // start

    fun end() {
        lives = 0
        score = -1
    } // end

    val extent get() = ext

    fun nextWave(w: Wave) { wave = w }

    fun lifeLost(): NextShip {
        if (--lives > 0) return NextShip.Continue

        nextWave(GameOver(this, wave))

        return NextShip.End
    } // lifeLost
    fun lifeGained() {
        lives = min(lives + 1, 9)
    } // livesGained
    fun scored(add: Int) {
        score += add

        if (score > highScore) {
            highScore = score
            newHighScore = true
        }
    }// scored

    /////
    fun onSingleTapUp(ev: MotionEvent) {
        ev.transform(touchMatrix)
        wave.onSingleTapUp(ev)
    }
    fun onScroll(offsetX: Float, offsetY: Float) = wave.onScroll(offsetX, offsetY)
    fun onLongPress() = wave.onLongPress()

    /////
    fun update(frameRateScale: Float) = wave.update(frameRateScale)
    fun draw(canvas: Canvas) {
        canvas.save()
        canvas.setMatrix(scaleMatrix)
        canvas.clipRect(extent.bounds)

        wave.draw(canvas)

        drawScore(canvas)
        drawLives(canvas)

        canvas.restore()
    } // draw

    private fun drawScore(canvas: Canvas) {
        if (score == -1) return

        canvas.drawText(
            "${score}".padStart(6, '0'),
            -extent.canvasOffsetX + 50,
            extent.canvasOffsetY - 50,
            scorePen
        )
        if (!newHighScore) return

        scorePen.textSize = 32f
        canvas.drawText(
            "High Score",
            -extent.canvasOffsetX + 60,
            extent.canvasOffsetY - 160,
            scorePen
        )
        scorePen.textSize = 128f
    } // drawScore

    private fun drawLives(canvas: Canvas) {
        canvas.translate(extent.canvasOffsetX - 50, extent.canvasOffsetY - 90)
        canvas.rotate(-90f)
        canvas.scale(0.75f, 0.75f)
        for (l in 0 until lives) {
            canvas.drawLines(Ship.shape, Ship.shipBrush)
            canvas.translate(0f, -105f)
        } // for
    } // drawLives

    /////
    fun loadSound(soundResId: Int): (Point) -> Unit {
        val soundFn = sounds.load(soundResId)

        val pannedSoundFn = { position: Point ->
            val pan = position.x.toFloat() / extent.canvasOffsetX
            soundFn(pan)
        } // pannedSoundFn

        return pannedSoundFn
    } // loadSound

    fun loadBitmap(bitmapId: Int): BitmapDrawable =
        context.resources.getDrawable(bitmapId, null) as BitmapDrawable

    private val prefs: SharedPreferences
        get() = context.getSharedPreferences("swoop", Context.MODE_PRIVATE)

    var highScore: Int
        get() = prefs.getInt("highscore", 0)
        private set(value) = prefs.edit { putInt("highscore", value) }
    private var newHighScore: Boolean
        get() = prefs.getBoolean("newHighscore", false)
        set(value) = prefs.edit { putBoolean("newHighscore", value) }

    companion object {
        private val scorePen = Paint()

        init {
            scorePen.color = Color.CYAN
            scorePen.alpha = 255
            scorePen.textSize = 128f
            scorePen.textAlign = Paint.Align.LEFT
        }
    }
} // Game