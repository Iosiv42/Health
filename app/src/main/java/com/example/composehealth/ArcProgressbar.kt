package com.example.composehealth

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs

val maxProgressPerLevel = 200 // you can change this to any max value that you want
val progressLimit = 300f

fun calculate(
    score: Float,
    level: Int,
) : Float {
    return (abs(score - (maxProgressPerLevel * level)) / maxProgressPerLevel) * progressLimit
}

@Composable
fun ArcProgressbar(
    modifier: Modifier = Modifier,
    calories: Float,
    dailyCaloriesTarget: Int,
) {
    Box(modifier = modifier) {
        PointsProgress(
            progress = calories / dailyCaloriesTarget.toFloat()
        )

        CollectorLevel(
            Modifier.align(Alignment.Center),
            calories,
            dailyCaloriesTarget
        )
    }
}

@Composable
fun CollectorLevel(
    modifier : Modifier = Modifier,
    calories: Float,
    dailyCaloriesTarget: Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var caloriesLeft = dailyCaloriesTarget - calories
        caloriesLeft = if (caloriesLeft < 0) 0f else caloriesLeft

        Text(
            text = caloriesLeft.toInt().toString() + " ккал",
        )

        Text(
            text = "осталось",
        )
    }
}

@Composable
fun BoxScope.PointsProgress(
    progress: Float,
//    startDegree: Float = 120f,
//    endDegree: Float = 300f,
//    thickness: Dp = 8.dp,
//
) {

    val start = 140f
    //val end = 360f - 2*(start - 90)
    val thickness = 8.dp
    val sweep = 360f - 2*(start - 90)
    var sweep2 = progress * sweep
    sweep2 = if (sweep2 > sweep) sweep else sweep2

    val fgArcColor = MaterialTheme.colorScheme.primary
    val bgArcColor = Color.LightGray

    var bgStart = start + sweep2 + 10
    bgStart = if (bgStart > start + sweep - 10) start + sweep2 else bgStart
    var bgSweep = (start + sweep) - bgStart
    bgSweep = if (bgSweep < 0) 0f else bgSweep

    Canvas(
        modifier = Modifier
//            .fillMaxWidth()
            .padding(10.dp)
            .aspectRatio(1f)
            .align(Alignment.Center),
        onDraw = {
            // Background Arc
            drawArc(
                color = bgArcColor,
                startAngle = bgStart,
                sweepAngle = bgSweep,
                useCenter = false,
                style = Stroke(thickness.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )

            // Foreground Arc
            drawArc(
                color = fgArcColor,
                startAngle = start,
                sweepAngle = sweep2,
                useCenter = false,
                style = Stroke(thickness.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }
    )
}