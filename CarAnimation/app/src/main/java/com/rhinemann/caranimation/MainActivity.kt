package com.rhinemann.caranimation

import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.rhinemann.caranimation.ui.theme.CarAnimationTheme
import dev.benedikt.math.bezier.spline.BezierSpline
import dev.benedikt.math.bezier.spline.DoubleBezierSpline
import dev.benedikt.math.bezier.vector.Vector2D
import kotlin.math.PI
import kotlin.math.atan2


class MainActivity : ComponentActivity() {

    private var getCoordinatesAt: (Double) -> Vector2D
    private var getTangentAt: (Double) -> Vector2D

    init {
        val spline: BezierSpline<Double, Vector2D> = DoubleBezierSpline(false)

        spline.addKnots(
            Vector2D(0.0, 1.0),
            Vector2D(0.3, 0.8),
            Vector2D(0.1, 0.6),
            Vector2D(0.8, 0.4),
            Vector2D(0.2, 0.2),
            Vector2D(1.0, 0.0)
        )

        getCoordinatesAt = { value -> spline.getCoordinatesAt(value) }
        getTangentAt = { value -> spline.getTangentAt(value) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarAnimationApp(getCoordinatesAt, getTangentAt)
        }
    }
}

enum class CarState {
    Start,
    End
}

@Composable
fun CarAnimationApp(
    getCoordinatesAt: (Double) -> Vector2D,
    getTangentAt: (Double) -> Vector2D
) {
    CarAnimationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Car(getCoordinatesAt, getTangentAt)
            }
        }
    }
}

@Composable
fun Car(
    getCoordinatesAt: (Double) -> Vector2D,
    getTangentAt: (Double) -> Vector2D,
) {
    val carSize = 64
    val animationDuration = 5000
    val carRotateOffset = 90

    var carState by remember { mutableStateOf(CarState.Start) }
    val transition = updateTransition(targetState = carState, label = "")
    val progress by transition.animateFloat(
        transitionSpec = { tween(animationDuration, 0, LinearEasing) },
        label = ""
    ) { state ->
        when (state) {
            CarState.Start -> 0F
            CarState.End -> 1F
        }
    }

    val config = LocalConfiguration.current
    val (w, h) = (config.screenWidthDp - carSize) * config.densityDpi / 160f to
            (config.screenHeightDp - carSize) * config.densityDpi / 160f
    val offset = getCoordinatesAt(progress.toDouble()).toIntOffset(w, h)
    val rotation = getTangentAt(progress.toDouble()).toDegree()

    Image(
        painter = painterResource(id = R.drawable.top_car_view),
        contentDescription = stringResource(R.string.car_image),
        modifier = Modifier
            .size(carSize.dp)
            .offset { offset }
            .rotate(carRotateOffset + rotation)
            .clickable {
                carState = when (carState) {
                    CarState.Start -> CarState.End
                    CarState.End -> CarState.Start
                }
            }
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_TYPE_NORMAL, device = Devices.DEFAULT)
@Composable
fun DefaultPreview(
    getCoordinatesAt: (Double) -> Vector2D = { Vector2D(0.0, 0.0) },
    getTangentAt: (Double) -> Vector2D = { Vector2D(0.0, 0.0) }
) {
    CarAnimationApp(getCoordinatesAt, getTangentAt)
}