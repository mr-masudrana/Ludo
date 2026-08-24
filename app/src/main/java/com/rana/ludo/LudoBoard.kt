package com.rana.ludo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.min

private const val BOARD_SIZE = 15

private val Red = Color(0xFFE53935)
private val Green = Color(0xFF43A047)
private val Yellow = Color(0xFFFDD835)
private val Blue = Color(0xFF1E88E5)

private val BoardBackground = Color(0xFFF5F5F5)
private val PathColor = Color.White
private val GridColor = Color(0xFF9E9E9E)

@Composable
fun LudoBoard(
    modifier: Modifier = Modifier
) {

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {

        val boardSize = min(size.width, size.height)
        val cellSize = boardSize / BOARD_SIZE

        // Board background
        drawRect(
            color = BoardBackground,
            size = Size(boardSize, boardSize)
        )

        // Home areas
        drawHomeArea(
            cellSize = cellSize,
            startRow = 0,
            startColumn = 0,
            color = Green
        )

        drawHomeArea(
            cellSize = cellSize,
            startRow = 0,
            startColumn = 9,
            color = Red
        )

        drawHomeArea(
            cellSize = cellSize,
            startRow = 9,
            startColumn = 0,
            color = Yellow
        )

        drawHomeArea(
            cellSize = cellSize,
            startRow = 9,
            startColumn = 9,
            color = Blue
        )

        // Playing path
        drawPathCells(cellSize)

        // Center
        drawCenter(cellSize)

        // Grid
        drawGrid(
            cellSize = cellSize,
            boardSize = boardSize
        )

        // Tokens
        drawHomeTokens(
            cellSize = cellSize,
            startRow = 0,
            startColumn = 0,
            color = Green
        )

        drawHomeTokens(
            cellSize = cellSize,
            startRow = 0,
            startColumn = 9,
            color = Red
        )

        drawHomeTokens(
            cellSize = cellSize,
            startRow = 9,
            startColumn = 0,
            color = Yellow
        )

        drawHomeTokens(
            cellSize = cellSize,
            startRow = 9,
            startColumn = 9,
            color = Blue
        )
    }
}

private fun DrawScope.drawHomeArea(
    cellSize: Float,
    startRow: Int,
    startColumn: Int,
    color: Color
) {

    drawRect(
        color = color,
        topLeft = Offset(
            startColumn * cellSize,
            startRow * cellSize
        ),
        size = Size(
            6 * cellSize,
            6 * cellSize
        )
    )

    // Inner white area
    drawRect(
        color = Color.White,
        topLeft = Offset(
            (startColumn + 1) * cellSize,
            (startRow + 1) * cellSize
        ),
        size = Size(
            4 * cellSize,
            4 * cellSize
        )
    )
}

private fun DrawScope.drawHomeTokens(
    cellSize: Float,
    startRow: Int,
    startColumn: Int,
    color: Color
) {

    val positions = listOf(
        Pair(2, 2),
        Pair(2, 4),
        Pair(4, 2),
        Pair(4, 4)
    )

    for ((row, column) in positions) {

        val center = Offset(
            (startColumn + column + 0.5f) * cellSize,
            (startRow + row + 0.5f) * cellSize
        )

        drawCircle(
            color = color,
            radius = cellSize * 0.32f,
            center = center
        )

        drawCircle(
            color = Color.White,
            radius = cellSize * 0.32f,
            center = center,
            style = Stroke(width = cellSize * 0.05f)
        )
    }
}

private fun DrawScope.drawPathCells(
    cellSize: Float
) {

    // Horizontal path
    for (row in 6..8) {

        for (column in 0 until BOARD_SIZE) {

            // Home areas remain untouched
            if (column !in 0..5 && column !in 9..14) {

                drawRect(
                    color = PathColor,
                    topLeft = Offset(
                        column * cellSize,
                        row * cellSize
                    ),
                    size = Size(
                        cellSize,
                        cellSize
                    )
                )
            }
        }
    }

    // Vertical path
    for (column in 6..8) {

        for (row in 0 until BOARD_SIZE) {

            if (row !in 0..5 && row !in 9..14) {

                drawRect(
                    color = PathColor,
                    topLeft = Offset(
                        column * cellSize,
                        row * cellSize
                    ),
                    size = Size(
                        cellSize,
                        cellSize
                    )
                )
            }
        }
    }

    // Green path
    for (row in 1..5) {

        drawRect(
            color = Green,
            topLeft = Offset(
                7 * cellSize,
                row * cellSize
            ),
            size = Size(
                cellSize,
                cellSize
            )
        )
    }

    // Red path
    for (column in 9..13) {

        drawRect(
            color = Red,
            topLeft = Offset(
                column * cellSize,
                7 * cellSize
            ),
            size = Size(
                cellSize,
                cellSize
            )
        )
    }

    // Blue path
    for (row in 9..13) {

        drawRect(
            color = Blue,
            topLeft = Offset(
                7 * cellSize,
                row * cellSize
            ),
            size = Size(
                cellSize,
                cellSize
            )
        )
    }

    // Yellow path
    for (column in 1..5) {

        drawRect(
            color = Yellow,
            topLeft = Offset(
                column * cellSize,
                7 * cellSize
            ),
            size = Size(
                cellSize,
                cellSize
            )
        )
    }
}

private fun DrawScope.drawCenter(
    cellSize: Float
) {

    val centerX = 7 * cellSize
    val centerY = 7 * cellSize

    // Green triangle
    val greenPath = androidx.compose.ui.graphics.Path().apply {

        moveTo(centerX, centerY)
        lineTo(
            centerX + 1.5f * cellSize,
            centerY
        )
        lineTo(
            centerX + 1.5f * cellSize,
            centerY + 3 * cellSize
        )
        close()
    }

    drawPath(
        path = greenPath,
        color = Green
    )

    // Red triangle
    val redPath = androidx.compose.ui.graphics.Path().apply {

        moveTo(centerX, centerY)
        lineTo(
            centerX + 3 * cellSize,
            centerY
        )
        lineTo(
            centerX + 3 * cellSize,
            centerY + 3 * cellSize
        )
        close()
    }

    drawPath(
        path = redPath,
        color = Red
    )

    // Blue triangle
    val bluePath = androidx.compose.ui.graphics.Path().apply {

        moveTo(centerX + 3 * cellSize, centerY + 3 * cellSize)
        lineTo(
            centerX,
            centerY + 3 * cellSize
        )
        lineTo(
            centerX + 3 * cellSize,
            centerY
        )
        close()
    }

    drawPath(
        path = bluePath,
        color = Blue
    )

    // Yellow triangle
    val yellowPath = androidx.compose.ui.graphics.Path().apply {

        moveTo(centerX, centerY)
        lineTo(
            centerX,
            centerY + 3 * cellSize
        )
        lineTo(
            centerX + 3 * cellSize,
            centerY + 3 * cellSize
        )
        close()
    }

    drawPath(
        path = yellowPath,
        color = Yellow
    )
}

private fun DrawScope.drawGrid(
    cellSize: Float,
    boardSize: Float
) {

    for (i in 0..BOARD_SIZE) {

        val position = i * cellSize

        drawLine(
            color = GridColor,
            start = Offset(position, 0f),
            end = Offset(position, boardSize),
            strokeWidth = 1.dp.toPx()
        )

        drawLine(
            color = GridColor,
            start = Offset(0f, position),
            end = Offset(boardSize, position),
            strokeWidth = 1.dp.toPx()
        )
    }
}
