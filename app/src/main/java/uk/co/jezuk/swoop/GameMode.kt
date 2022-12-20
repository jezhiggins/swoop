package uk.co.jezuk.swoop

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import uk.co.jezuk.swoop.craft.Bullet
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation

class Setup(
    val touchArea: Rect,
    val rezPoint: Point,
    val initialRotation: Rotation,
    val shipShape: FloatArray,
    val shipColor: Paint,
    val bulletColor: Paint,
    val livesDisplayPosition: (Canvas) -> Unit,
    val scoreDisplayPosition: (Canvas) -> Unit
) {
    companion object {
        val OnePlayer = listOf(
            Setup(
                Game.extent.WholeArea,
                Point(0.0, 0.0),
                Rotation.Up,
                Ship.Dart,
                Ship.GreenBrush,
                Bullet.MagentaBrush,
                Lives::SinglePlayer,
                Score::SinglePlayer
            )
        )
        val TwoPlayer = listOf(
            Setup(
                Game.extent.RightHalf,
                Point(200.0, 0.0),
                Rotation.Right,
                Ship.Dart,
                Ship.GreenBrush,
                Bullet.MagentaBrush,
                Lives::PlayerOne,
                Score::PlayerOne
            ),
            Setup(
                Game.extent.LeftHalf,
                Point(-200.0, 0.0),
                Rotation.Left,
                Ship.Speeder,
                Ship.PinkBrush,
                Bullet.GreenBrush,
                Lives::PlayerTwo,
                Score::PlayerTwo
            )
        )
    }
}

typealias GameMode = List<Setup>