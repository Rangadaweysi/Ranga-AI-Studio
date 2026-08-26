package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SparklineWave(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
    ) {
        val width = size.width
        val height = size.height
        val midY = height * 0.5f

        val path = Path().apply {
            moveTo(0f, midY + height * 0.2f)
            cubicTo(
                width * 0.15f, midY - height * 0.35f,
                width * 0.25f, midY + height * 0.35f,
                width * 0.40f, midY - height * 0.15f
            )
            cubicTo(
                width * 0.55f, midY - height * 0.45f,
                width * 0.70f, midY + height * 0.30f,
                width * 0.85f, midY - height * 0.20f
            )
            cubicTo(
                width * 0.92f, midY + height * 0.10f,
                width * 0.96f, midY - height * 0.10f,
                width, midY
            )
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}
