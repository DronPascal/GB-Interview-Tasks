package com.rhinemann.caranimation

import androidx.compose.ui.unit.IntOffset
import dev.benedikt.math.bezier.vector.Vector2D
import kotlin.math.PI
import kotlin.math.atan2

/**
 * Created by dronpascal on 17.04.2022.
 */
internal fun Vector2D.toIntOffset(w: Float, h: Float): IntOffset {
    println("size $w, $h")
    println("offset ${(this.x * w).toInt()}, ${(this.y * h).toInt()}")
    return IntOffset((this.x * w).toInt(), (this.y * h).toInt())
}

internal fun Vector2D.toDegree(): Float {
    return (atan2(this.y, this.x) * 180 / PI).toFloat()
}