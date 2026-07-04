package com.foretmagique.ui.tracing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.foretmagique.content.tracing.NormalizedPoint

@Composable
fun TracingCanvas(
    checkpoints: List<NormalizedPoint>,
    resetKey: Int,
    onTraceFinished: (List<NormalizedPoint>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var drawnPoints by remember(resetKey) { mutableStateOf(listOf<Offset>()) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    Canvas(
        modifier = modifier
            .onSizeChanged { canvasSize = it }
            .pointerInput(resetKey) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        change.consume()
                        drawnPoints = drawnPoints + change.position
                    },
                    onDragEnd = {
                        val width = canvasSize.width
                        val height = canvasSize.height
                        if (width > 0 && height > 0) {
                            val normalized = drawnPoints.map { point ->
                                NormalizedPoint(point.x / width, point.y / height)
                            }
                            onTraceFinished(normalized)
                        }
                    },
                )
            },
    ) {
        if (checkpoints.isNotEmpty()) {
            val guidePath = Path().apply {
                val first = checkpoints.first()
                moveTo(first.x * size.width, first.y * size.height)
                checkpoints.drop(1).forEach { point ->
                    lineTo(point.x * size.width, point.y * size.height)
                }
            }
            drawPath(
                path = guidePath,
                color = Color.Gray.copy(alpha = 0.45f),
                style = Stroke(
                    width = 14f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 18f)),
                ),
            )
        }

        if (drawnPoints.size > 1) {
            val userPath = Path().apply {
                moveTo(drawnPoints.first().x, drawnPoints.first().y)
                drawnPoints.drop(1).forEach { lineTo(it.x, it.y) }
            }
            drawPath(
                path = userPath,
                color = Color(0xFF2E7D32),
                style = Stroke(width = 20f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }
    }
}
