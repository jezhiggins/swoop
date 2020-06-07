package uk.co.jezuk.swoop.craft

import android.graphics.Canvas

interface Target {
    fun update(fps: Long)
    fun draw(canvas: Canvas)
}