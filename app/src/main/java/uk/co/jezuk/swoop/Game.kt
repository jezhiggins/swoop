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
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Projectiles
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.Extent
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.wave.*
import uk.co.jezuk.swoop.wave.transition.Attract
import uk.co.jezuk.swoop.wave.transition.Emptiness
import uk.co.jezuk.swoop.wave.transition.GameOver
import kotlin.math.max
import kotlin.math.min

class Game(private val context: Context) {
    enum class NextShip { Continue, End }
    private var wave: Wave = Emptiness()
    val extent = Extent(1920, 1080)
    private var scaleMatrix = Matrix()
    private var touchMatrix = Matrix()
    private val sounds = Sounds(context)
    private var lives = 0
    private var score = -1
    private var pure = false

    init {
        val soundIds = listOf(
            R.raw.banglarge,
            R.raw.bangmedium,
            R.raw.bangsmall,
            R.raw.cometslap,
            R.raw.rezin,
            R.raw.rezout,
            R.raw.shipexplosion,
            R.raw.spaceman,
            R.raw.spacemanfallen,
            R.raw.spacemansaved,
            R.raw.thrust,
            R.raw.ting,
            R.raw.minedrop,
            R.raw.mineexplosion,
            R.raw.minelayershieldhit,
            R.raw.minelayerexplosion,
            R.raw.minelayeralarm,
            R.raw.sauceralarm,
            R.raw.saucerexplosion,
            R.raw.saucerfire,
            R.raw.missileexplosion
        )
        soundIds.forEach({ sounds.load(it) })
    }

    fun setExtent(width: Int, height: Int) {
        val screenExt = Extent(width, height)
        val scaleX = screenExt.width.toFloat() / extent.width
        val scaleY = screenExt.height.toFloat() / extent.height
        val screenScale = min(scaleX, scaleY)

        val scaledWidth = (extent.width * screenScale).toInt()
        val scaledHeight = (extent.height * screenScale).toInt()

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

    fun start(startWave: Int) {
        lives = startLives(startWave)
        score = startScore(startWave)
        newHighScore = false
        pure = (startWave == 0)
    } // start

    fun end() {
        lives = 0
        score = -1
    } // end

    fun endOfWave(starField: StarField, ship: Ship, projectiles: Projectiles? = null, gun: Gun? = null) {
        nextWave(Waves.transition(this, starField, ship, projectiles, gun))
    } // endOfWave
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
    fun sound(soundResId: Int, position: Point) {
        val soundFn = sounds.load(soundResId)
        val pan = position.x.toFloat() / extent.canvasOffsetX
        soundFn(pan)
    } // sound

    fun loadBitmap(bitmapId: Int): BitmapDrawable =
        context.resources.getDrawable(bitmapId, null) as BitmapDrawable

    private val prefs: SharedPreferences
        get() = context.getSharedPreferences("swoop", Context.MODE_PRIVATE)

    var highScore: Int
        get() = prefs.getInt(highscoreTag(), 0)
        private set(value) = prefs.edit { putInt(highscoreTag(), value) }
    private var newHighScore: Boolean
        get() = prefs.getBoolean("newHighscore", false)
        set(value) = prefs.edit { putBoolean("newHighscore", value) }

    fun pureHighScore(): Int = prefs.getInt("purehighscore", 0)
    fun restartHighScore(): Int = prefs.getInt("restarthighscore", 0)
    private fun highscoreTag(): String = if (pure) "purehighscore" else "restarthighscore"

    fun checkpointScore(waveIndex: Int) {
        if (score < startScore(waveIndex))
            return

        prefs.edit {
            val highest = max(waveIndex, prefs.getInt("maxwave", 0))

            putInt("score${waveIndex}", score)
            putInt("lives${waveIndex}", lives)
            putInt("maxwave", highest)
        }
    }
    fun startScore(waveIndex: Int): Int =
        prefs.getInt("score${waveIndex}", 0)
    fun startLives(waveIndex: Int): Int =
        prefs.getInt("lives${waveIndex}", 3)
    var highWave: Int
        get() = prefs.getInt("maxwave", 0)
        private set(value) = prefs.edit { putInt("maxwave", value) }

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