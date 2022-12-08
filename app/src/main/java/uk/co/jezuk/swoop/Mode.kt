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
    val bulletColor: Paint
)

typealias GameMode = List<Mode>

val OnePlayer = listOf(Mode(Point(0.0, 0.0), Rotation(-90.0), Ship.Dart, Bullet.Brush(Color.MAGENTA)))
val TwoPlayer = listOf(
    Mode(Point(200.0, 0.0), Rotation(180.0), Ship.Dart, Bullet.Brush(Color.MAGENTA)),
    Mode(Point(-200.0, 0.0), Rotation(0.0), Ship.Speeder, Bullet.Brush(Color.rgb(173, 255, 47)))
)
