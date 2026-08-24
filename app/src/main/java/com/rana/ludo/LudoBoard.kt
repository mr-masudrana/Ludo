package com.rana.ludo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import com.rana.ludo.engine.BoardPath
import com.rana.ludo.model.PlayerColor
import com.rana.ludo.model.Token
import kotlin.math.min

private const val BOARD_SIZE = 15

private val Red = Color(0xFFE53935)
private val Green = Color(0xFF43A047)
private val Yellow = Color(0xFFFDD835)
private val Blue = Color(0xFF1E88E5)

private val BoardBackground = Color(0xFFF5F5F5)
private val GridColor = Color(0xFF9E9E9E)
private val PathColor = Color.White
private val HighlightColor = Color(0xFFFFC107)

@Composable
fun LudoBoard(
    tokens: List<Token> = emptyList(),
    movableTokenIds: Set<Int> = emptySet(),
    onTokenClick: (Token) -> Unit = {},
    modifier: Modifier = Modifier
) {

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(tokens, movableTokenIds) {

                detectTapGestures { tapOffset ->

                    val boardSize = min(
                        size.width.toFloat(),
                        size.height.toFloat()
                    )

                    val cellSize =
                        boardSize / BOARD_SIZE

                    val column =
                        (tapOffset.x / cellSize).toInt()

                    val row =
                        (tapOffset.y / cellSize).toInt()

                    val clickedToken =
                        tokens.firstOrNull { token ->

                            if (token.position < 0) {
                                false
                            } else {

                                val cell =
                                    BoardPath.getCell(token.position)

                                cell != null &&
                                        cell.row == row &&
                                        cell.column == column
                            }
                        }

                    if (
                        clickedToken != null &&
                        clickedToken.id in movableTokenIds
                    ) {
                        onTokenClick(clickedToken)
                    }
                }
            }
    ) {

        val boardSize = min(
            size.width,
            size.height
        )

        val cellSize =
            boardSize / BOARD_SIZE

        drawRect(
            color = BoardBackground,
            size = Size(boardSize, boardSize)
        )

        drawHomeArea(
            cellSize,
            0,
            0,
            Green
        )

        drawHomeArea(
            cellSize,
            0,
            9,
            Red
        )

        drawHomeArea(
            cellSize,
            9,
            0,
            Yellow
        )

        drawHomeArea(
            cellSize,
            9,
            9,
            Blue
        )

        drawPathCells(cellSize)

        drawCenter(cellSize)

        drawGrid(
            cellSize,
            boardSize
        )

        drawHomeTokens(
            cellSize,
            0,
            0,
            Green
        )

        drawHomeTokens(
            cellSize,
            0,
            9,
            Red
        )

        drawHomeTokens(
            cellSize,
            9,
            0,
            Yellow
        )

        drawHomeTokens(
            cellSize,
            9,
            9,
            Blue
        )

        // Highlight movable tokens
        tokens.forEach { token ->

            if (
                token.id in movableTokenIds &&
                token.position >= 0
            ) {

                val cell =
                    BoardPath.getCell(token.position)

                if (cell != null) {

                    drawCircle(
                        color = HighlightColor,
                        radius = cellSize * 0.42f,
                        center = Offset(
                            (cell.column + 0.5f) * cellSize,
                            (cell.row + 0.5f) * cellSize
                        ),
                        alpha = 0.35f
                    )
                }
            }
        }

        // Draw active tokens
        tokens.forEach { token ->

            if (
                token.position >= 0 &&
                !token.isFinished
            ) {

                drawTokenOnBoard(
                    token = token,
                    cellSize = cellSize
                )
            }
        }
    }
}

private fun DrawScope.drawTokenOnBoard(
    token: Token,
    cellSize: Float
) {

    val cell =
        BoardPath.getCell(token.position)
            ?: return

    val center = Offset(
        (cell.column + 0.5f) * cellSize,
        (cell.row + 0.5f) * cellSize
    )

    val color =
        token.color.toColor()

    drawCircle(
        color = color,
        radius = cellSize * 0.32f,
        center = center
    )

    drawCircle(
        color = Color.White,
        radius = cellSize * 0.32f,
        center = center,
        style = androidx.compose.ui.graphics.drawscope.Stroke(
            width = cellSize * 0.05f
        )
    )

    drawCircle(
        color = Color.Black.copy(alpha = 0.15f),
        radius = cellSize * 0.12f,
        center = center
    )
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

    positions.forEach { (row, column) ->

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
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = cellSize * 0.05f
            )
        )
    }
}

private fun DrawScope.drawPathCells(
    cellSize: Float
) {

    for (row in 6..8) {

        for (column in 0 until BOARD_SIZE) {

            if (
                column !in 0..5 &&
                column !in 9..14
            ) {

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

    for (column in 6..8) {

        for (row in 0 until BOARD_SIZE) {

            if (
                row !in 0..5 &&
                row !in 9..14
            ) {

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
}

private fun DrawScope.drawCenter(
    cellSize: Float
) {

    val left = 6 * cellSize
    val top = 6 * cellSize
    val right = 9 * cellSize
    val bottom = 9 * cellSize

    val green = Path().apply {
        moveTo(left, top)
        lineTo(right, top)
        lineTo(right, bottom)
        close()
    }

    drawPath(
        path = green,
        color = Green
    )

    val red = Path().apply {
        moveTo(right, top)
        lineTo(right, bottom)
        lineTo(left, bottom)
        close()
    }

    drawPath(
        path = red,
        color = Red
    )

    val yellow = Path().apply {
        moveTo(left, top)
        lineTo(left, bottom)
        lineTo(right, bottom)
        close()
    }

    drawPath(
        path = yellow,
        color = Yellow
    )

    val blue = Path().apply {
        moveTo(left, top)
        lineTo(right, top)
        lineTo(left, bottom)
        close()
    }

    drawPath(
        path = blue,
        color = Blue
    )
}

private fun DrawScope.drawGrid(
    cellSize: Float,
    boardSize: Float
) {

    for (i in 0..BOARD_SIZE) {

        val position =
            i * cellSize

        drawLine(
            color = GridColor,
            start = Offset(
                position,
                0f
            ),
            end = Offset(
                position,
                boardSize
            ),
            strokeWidth = 1f,
            cap = StrokeCap.Square
        )

        drawLine(
            color = GridColor,
            start = Offset(
                0f,
                position
            ),
            end = Offset(
                boardSize,
                position
            ),
            strokeWidth = 1f,
            cap = StrokeCap.Square
        )
    }
}

private fun PlayerColor.toColor(): Color {

    return when (this) {

        PlayerColor.GREEN ->
            Green

        PlayerColor.RED ->
            Red

        PlayerColor.YELLOW ->
            Yellow

        PlayerColor.BLUE ->
            Blue
    }
}
