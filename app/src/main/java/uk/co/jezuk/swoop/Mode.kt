package uk.co.jezuk.swoop

import android.graphics.Color
import android.graphics.Paint
import uk.co.jezuk.swoop.craft.Bullet
import uk.co.jezuk.swoop.craft.Ship
import uk.co.jezuk.swoop.geometry.Point
import uk.co.jezuk.swoop.geometry.Rotation

data class Mode(
    val rezPoint: Point,
    val initialRotation: Rotation,
    val shipShape: FloatArray,
    val shipColor: Paint,
    val bulletColor: Paint
)

typealias GameMode = List<Mode>

val OnePlayer = listOf(Mode(Point(0.0, 0.0), Rotation.Up, Ship.Dart, Ship.GreenBrush, Bullet.MagentaBrush))
val TwoPlayer = listOf(
    Mode(Point(200.0, 0.0), Rotation.Right, Ship.Dart, Ship.GreenBrush, Bullet.MagentaBrush),
    Mode(Point(-200.0, 0.0), Rotation.Left, Ship.Speeder, Ship.PinkBrush, Bullet.GreenBrush)
)
