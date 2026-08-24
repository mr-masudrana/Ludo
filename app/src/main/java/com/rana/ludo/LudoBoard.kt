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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.rana.ludo.engine.BoardCell
import com.rana.ludo.engine.BoardPath
import com.rana.ludo.model.Player
import com.rana.ludo.model.PlayerColor
import com.rana.ludo.model.Token
import kotlin.math.min

private const val BOARD_CELLS = 15

private val Green = Color(0xFF43A047)
private val Red = Color(0xFFE53935)
private val Yellow = Color(0xFFFDD835)
private val Blue = Color(0xFF1E88E5)

private val Background = Color(0xFFF3F3F3)
private val PathColor = Color.White
private val GridColor = Color(0xFF9E9E9E)
private val SafeColor = Color(0xFFFFC107)
private val CenterColor = Color.White

@Composable
fun LudoBoard(
    players: List<Player>,
    movableTokenIds: Set<String> = emptySet(),
    onTokenClick: (Token) -> Unit = {},
    modifier: Modifier = Modifier
) {

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(
                players,
                movableTokenIds
            ) {

                val boardSize =
                    min(
                        size.width.toFloat(),
                        size.height.toFloat()
                    )

                val cellSize =
                    boardSize / BOARD_CELLS

                detectTapGestures { tap ->

                    val column =
                        (tap.x / cellSize)
                            .toInt()
                            .coerceIn(
                                0,
                                BOARD_CELLS - 1
                            )

                    val row =
                        (tap.y / cellSize)
                            .toInt()
                            .coerceIn(
                                0,
                                BOARD_CELLS - 1
                            )

                    val clickedToken =
                        players
                            .flatMap {
                                it.tokens
                            }
                            .firstOrNull { token ->

                                if (
                                    token.isFinished ||
                                    token.isHome
                                ) {
                                    false
                                } else {

                                    val cell =
                                        BoardPath.getTokenCell(
                                            token.color,
                                            token.position
                                        )

                                    cell != null &&
                                            cell.row == row &&
                                            cell.column == column &&
                                            token.uniqueId in
                                                movableTokenIds
                                }
                            }

                    if (
                        clickedToken != null
                    ) {
                        onTokenClick(
                            clickedToken
                        )
                    }
                }
            }
    ) {

        val boardSize =
            min(
                size.width,
                size.height
            )

        val cellSize =
            boardSize / BOARD_CELLS

        // Background
        drawRect(
            color = Background,
            size = Size(
                boardSize,
                boardSize
            )
        )

        // Four home areas
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

        // Outer path
        drawPathCells(
            cellSize = cellSize
        )

        // Colored home lanes
        drawHomeLanes(
            cellSize = cellSize
        )

        // Safe cells
        drawSafeCells(
            cellSize = cellSize
        )

        // Center
        drawCenter(
            cellSize = cellSize
        )

        // Grid
        drawGrid(
            cellSize = cellSize,
            boardSize = boardSize
        )

        val allTokens =
            players.flatMap {
                it.tokens
            }

        // Tokens inside home
        allTokens
            .filter {
                it.isHome
            }
            .forEach { token ->

                drawHomeToken(
                    token = token,
                    cellSize = cellSize
                )
            }

        // Movable token highlight
        allTokens
            .filter {
                it.uniqueId in movableTokenIds &&
                        !it.isHome &&
                        !it.isFinished
            }
            .forEach { token ->

                val cell =
                    BoardPath.getTokenCell(
                        token.color,
                        token.position
                    )

                if (cell != null) {

                    drawCircle(
                        color = SafeColor.copy(
                            alpha = 0.35f
                        ),
                        radius =
                            cellSize * 0.43f,
                        center =
                            cellCenter(
                                cell,
                                cellSize
                            )
                    )
                }
            }

        // Active tokens
        allTokens
            .filter {
                !it.isHome &&
                        !it.isFinished
            }
            .forEach { token ->

                drawBoardToken(
                    token = token,
                    allTokens = allTokens,
                    cellSize = cellSize
                )
            }
    }
}

/* ------------------------------------------------ */
/* HOME AREA                                        */
/* ------------------------------------------------ */

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

    val positions =
        listOf(
            Pair(2, 2),
            Pair(2, 4),
            Pair(4, 2),
            Pair(4, 4)
        )

    positions.forEach { (row, column) ->

        drawCircle(
            color = Color.White,
            radius = cellSize * 0.38f,
            center = Offset(
                (
                    startColumn +
                            column +
                            0.5f
                ) * cellSize,

                (
                    startRow +
                            row +
                            0.5f
                ) * cellSize
            )
        )
    }
}

private fun DrawScope.drawHomeToken(
    token: Token,
    cellSize: Float
) {

    val area =
        when (token.color) {

            PlayerColor.GREEN ->
                Pair(0, 0)

            PlayerColor.RED ->
                Pair(0, 9)

            PlayerColor.YELLOW ->
                Pair(9, 0)

            PlayerColor.BLUE ->
                Pair(9, 9)
        }

    val positions =
        listOf(
            Pair(2, 2),
            Pair(2, 4),
            Pair(4, 2),
            Pair(4, 4)
        )

    val index =
        token.id.coerceIn(
            0,
            3
        )

    val (row, column) =
        positions[index]

    val center =
        Offset(
            (
                area.second +
                        column +
                        0.5f
            ) * cellSize,

            (
                area.first +
                        row +
                        0.5f
            ) * cellSize
        )

    drawTokenCircle(
        center = center,
        radius = cellSize * 0.30f,
        color = token.color.toColor(),
        cellSize = cellSize
    )
}

/* ------------------------------------------------ */
/* OUTER PATH                                       */
/* ------------------------------------------------ */

private fun DrawScope.drawPathCells(
    cellSize: Float
) {

    BoardPath.path.forEach { cell ->

        drawRect(
            color = PathColor,
            topLeft = Offset(
                cell.column * cellSize,
                cell.row * cellSize
            ),
            size = Size(
                cellSize,
                cellSize
            )
        )
    }
}

/* ------------------------------------------------ */
/* HOME LANES                                       */
/* ------------------------------------------------ */

private fun DrawScope.drawHomeLanes(
    cellSize: Float
) {

    drawLane(
        cellSize = cellSize,
        color = Green,
        cells =
            BoardPath.homeLane(
                PlayerColor.GREEN
            )
    )

    drawLane(
        cellSize = cellSize,
        color = Red,
        cells =
            BoardPath.homeLane(
                PlayerColor.RED
            )
    )

    drawLane(
        cellSize = cellSize,
        color = Blue,
        cells =
            BoardPath.homeLane(
                PlayerColor.BLUE
            )
    )

    drawLane(
        cellSize = cellSize,
        color = Yellow,
        cells =
            BoardPath.homeLane(
                PlayerColor.YELLOW
            )
    )
}

private fun DrawScope.drawLane(
    cellSize: Float,
    color: Color,
    cells: List<BoardCell>
) {

    cells.forEach { cell ->

        drawRect(
            color = color,
            topLeft = Offset(
                cell.column * cellSize,
                cell.row * cellSize
            ),
            size = Size(
                cellSize,
                cellSize
            )
        )
    }
}

/* ------------------------------------------------ */
/* SAFE CELLS                                       */
/* ------------------------------------------------ */

private fun DrawScope.drawSafeCells(
    cellSize: Float
) {

    val safePositions =
        listOf(
            0,
            8,
            13,
            21,
            26,
            34,
            39,
            47
        )

    safePositions.forEach { position ->

        val cell =
            BoardPath.getCell(
                position
            ) ?: return@forEach

        val center =
            cellCenter(
                cell,
                cellSize
            )

        drawCircle(
            color = SafeColor,
            radius = cellSize * 0.20f,
            center = center
        )

        drawStar(
            center = center,
            radius = cellSize * 0.13f
        )
    }
}

private fun DrawScope.drawStar(
    center: Offset,
    radius: Float
) {

    val path = Path()

    for (i in 0 until 10) {

        val angle =
            Math.toRadians(
                (-90 + i * 36).toDouble()
            )

        val r =
            if (i % 2 == 0) {
                radius
            } else {
                radius * 0.45f
            }

        val x =
            center.x +
                    kotlin.math.cos(angle)
                        .toFloat() * r

        val y =
            center.y +
                    kotlin.math.sin(angle)
                        .toFloat() * r

        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    path.close()

    drawPath(
        path = path,
        color = Color.White
    )
}

/* ------------------------------------------------ */
/* CENTER                                           */
/* ------------------------------------------------ */

private fun DrawScope.drawCenter(
    cellSize: Float
) {

    val left =
        6 * cellSize

    val top =
        6 * cellSize

    val right =
        9 * cellSize

    val bottom =
        9 * cellSize

    // Green
    drawPath(
        path = Path().apply {
            moveTo(left, top)
            lineTo(right, top)
            lineTo(
                7.5f * cellSize,
                7.5f * cellSize
            )
            close()
        },
        color = Green
    )

    // Red
    drawPath(
        path = Path().apply {
            moveTo(right, top)
            lineTo(right, bottom)
            lineTo(
                7.5f * cellSize,
                7.5f * cellSize
            )
            close()
        },
        color = Red
    )

    // Blue
    drawPath(
        path = Path().apply {
            moveTo(right, bottom)
            lineTo(left, bottom)
            lineTo(
                7.5f * cellSize,
                7.5f * cellSize
            )
            close()
        },
        color = Blue
    )

    // Yellow
    drawPath(
        path = Path().apply {
            moveTo(left, bottom)
            lineTo(left, top)
            lineTo(
                7.5f * cellSize,
                7.5f * cellSize
            )
            close()
        },
        color = Yellow
    )

    drawCircle(
        color = CenterColor,
        radius = cellSize * 0.22f,
        center = Offset(
            7.5f * cellSize,
            7.5f * cellSize
        )
    )
}

/* ------------------------------------------------ */
/* GRID                                             */
/* ------------------------------------------------ */

private fun DrawScope.drawGrid(
    cellSize: Float,
    boardSize: Float
) {

    for (i in 0..BOARD_CELLS) {

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
            strokeWidth = 1f
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
            strokeWidth = 1f
        )
    }
}

/* ------------------------------------------------ */
/* BOARD TOKEN                                      */
/* ------------------------------------------------ */

private fun DrawScope.drawBoardToken(
    token: Token,
    allTokens: List<Token>,
    cellSize: Float
) {

    val cell =
        BoardPath.getTokenCell(
            token.color,
            token.position
        ) ?: return

    val sameCell =
        allTokens.filter { other ->

            if (
                other.isHome ||
                other.isFinished
            ) {
                false
            } else {

                BoardPath.getTokenCell(
                    other.color,
                    other.position
                ) == cell
            }
        }

    val index =
        sameCell.indexOfFirst {
            it.uniqueId ==
                    token.uniqueId
        }.coerceAtLeast(0)

    val offsets =
        listOf(
            Offset(-0.22f, -0.22f),
            Offset(0.22f, -0.22f),
            Offset(-0.22f, 0.22f),
            Offset(0.22f, 0.22f)
        )

    val offset =
        offsets[
            index.coerceIn(
                0,
                offsets.lastIndex
            )
        ]

    val center =
        cellCenter(
            cell,
            cellSize
        ) + Offset(
            offset.x * cellSize,
            offset.y * cellSize
        )

    val radius =
        if (sameCell.size > 1) {
            cellSize * 0.22f
        } else {
            cellSize * 0.31f
        }

    drawTokenCircle(
        center = center,
        radius = radius,
        color = token.color.toColor(),
        cellSize = cellSize
    )
}

private fun DrawScope.drawTokenCircle(
    center: Offset,
    radius: Float,
    color: Color,
    cellSize: Float
) {

    drawCircle(
        color = Color.Black.copy(
            alpha = 0.15f
        ),
        radius = radius + cellSize * 0.04f,
        center = center + Offset(
            0f,
            cellSize * 0.04f
        )
    )

    drawCircle(
        color = color,
        radius = radius,
        center = center
    )

    drawCircle(
        color = Color.White,
        radius = radius,
        center = center,
        style = Stroke(
            width = cellSize * 0.045f
        )
    )

    drawCircle(
        color = Color.White.copy(
            alpha = 0.35f
        ),
        radius = radius * 0.28f,
        center = center + Offset(
            -radius * 0.25f,
            -radius * 0.25f
        )
    )
}

/* ------------------------------------------------ */
/* HELPERS                                          */
/* ------------------------------------------------ */

private fun cellCenter(
    cell: BoardCell,
    cellSize: Float
): Offset {

    return Offset(
        (cell.column + 0.5f) * cellSize,
        (cell.row + 0.5f) * cellSize
    )
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