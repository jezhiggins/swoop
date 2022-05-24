package uk.co.jezuk.swoop

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import androidx.core.content.edit
import uk.co.jezuk.swoop.craft.Gun
import uk.co.jezuk.swoop.craft.Projectiles
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
    private var scaleMatrix = Matrix()
    private var touchMatrix = Matrix()
    private var pure = false

    val player = Player(this)

    init {
        loadSounds(context);
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
        player.start(
            startLives(startWave),
            startScore(startWave)
        )

        newHighScore = false
        pure = (startWave == 0)
    } // start

    fun end() {
        player.end()
    } // end

    fun endOfWave(starField: StarField, projectiles: Projectiles? = null, gun: Gun? = null) {
        wave = Waves.transition(this, starField, projectiles, gun)
    } // endOfWave
    fun nextWave(w: Wave) {
        player.newWave()
        wave = w
    }

    fun gameOver() = nextWave(GameOver(this, wave))

    fun lifeGained() {
        player.lifeGained()
    } // livesGained

    fun scored(add: Int) {
        player.scored(add)

        if (player.score > highScore) {
            highScore = player.score
            newHighScore = true
        }
    }

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

        player.draw(canvas, newHighScore)

        canvas.restore()
    } // draw

    /////
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
        if (player.score < startScore(waveIndex))
            return

        prefs.edit {
            val highest = max(waveIndex, prefs.getInt("maxwave", 0))

            putInt("score${waveIndex}", player.score)
            putInt("lives${waveIndex}", player.lives)
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
        val extent = Extent(1920, 1080)

        fun sound(soundResId: Int, position: Point) {
            val soundFn = sounds!!.load(soundResId)
            val pan = position.x.toFloat() / extent.canvasOffsetX
            soundFn(pan)
        } // sound

        private var sounds: Sounds? = null

        private fun loadSounds(context: Context) {
            if (sounds != null)
                return

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
            sounds = Sounds(context)
            soundIds.forEach({ sounds!!.load(it) })
        }
    }
} // Game