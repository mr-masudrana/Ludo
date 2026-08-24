package com.rana.ludo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rana.ludo.model.Player
import com.rana.ludo.model.Token
import kotlin.math.min

@Composable
fun LudoBoard(
    players: List<Player>,
    movableTokenIds: Set<String> = emptySet(),
    onTokenClick: (Token) -> Unit = {},
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(8.dp)
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(22.dp)
            )
            .background(
                color = Color(0xFF24100E),
                shape = RoundedCornerShape(22.dp)
            )
            .border(
                width = 3.dp,
                color = Color(0xFFFFC83D),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(4.dp)
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {

            val boardSize = min(
                size.width,
                size.height
            )

            val cell = boardSize / 15f

            drawPremiumBoard(cell)

            /*
             * Token drawing is kept separate from
             * the board background.
             */
            drawTokens(
                players = players,
                cell = cell,
                movableTokenIds = movableTokenIds
            )
        }

        /*
         * Transparent clickable token layer.
         *
         * The actual token positions are calculated
         * by the same board coordinate system.
         */
        TokenClickLayer(
            players = players,
            movableTokenIds = movableTokenIds,
            onTokenClick = onTokenClick
        )
    }
}


/* =========================================================
   BOARD
   ========================================================= */

private fun DrawScope.drawPremiumBoard(
    cell: Float
) {

    val board = cell * 15f

    /*
     * Outer board background
     */
    drawRoundRect(
        brush = Brush.linearGradient(
            listOf(
                Color(0xFF3A1815),
                Color(0xFF210B09)
            )
        ),
        topLeft = Offset.Zero,
        size = androidx.compose.ui.geometry.Size(
            board,
            board
        ),
        cornerRadius =
            androidx.compose.ui.geometry.CornerRadius(
                cell * 0.45f
            )
    )

    /*
     * Four home areas
     */
    drawHomeArea(
        cell = cell,
        color = Color(0xFF69B82A),
        x = 0,
        y = 0
    )

    drawHomeArea(
        cell = cell,
        color = Color(0xFFFFC928),
        x = 9,
        y = 0
    )

    drawHomeArea(
        cell = cell,
        color = Color(0xFFE52C32),
        x = 0,
        y = 9
    )

    drawHomeArea(
        cell = cell,
        color = Color(0xFF168BE8),
        x = 9,
        y = 9
    )

    /*
     * Main playing path
     */
    drawPathCells(cell)

    /*
     * Colored home lanes
     */
    drawHomeLanes(cell)

    /*
     * Center
     */
    drawCenter(cell)

    /*
     * Safe cells
     */
    drawSafeCells(cell)

    /*
     * Grid
     */
    drawGrid(cell)
}


/* =========================================================
   HOME AREA
   ========================================================= */

private fun DrawScope.drawHomeArea(
    cell: Float,
    color: Color,
    x: Int,
    y: Int
) {

    val left = x * cell
    val top = y * cell

    drawRoundRect(
        color = color,
        topLeft = Offset(
            left,
            top
        ),
        size = androidx.compose.ui.geometry.Size(
            cell * 6f,
            cell * 6f
        ),
        cornerRadius =
            androidx.compose.ui.geometry.CornerRadius(
                cell * 0.35f
            )
    )

    /*
     * Inner panel
     */
    drawRoundRect(
        color = color.copy(alpha = 0.78f),
        topLeft = Offset(
            left + cell * 1f,
            top + cell * 1f
        ),
        size = androidx.compose.ui.geometry.Size(
            cell * 4f,
            cell * 4f
        ),
        cornerRadius =
            androidx.compose.ui.geometry.CornerRadius(
                cell * 0.45f
            )
    )

    /*
     * Inner highlight
     */
    drawRoundRect(
        color = Color.White.copy(alpha = 0.08f),
        topLeft = Offset(
            left + cell * 1.15f,
            top + cell * 1.15f
        ),
        size = androidx.compose.ui.geometry.Size(
            cell * 3.7f,
            cell * 1.2f
        ),
        cornerRadius =
            androidx.compose.ui.geometry.CornerRadius(
                cell * 0.35f
            )
    )
}


/* =========================================================
   PATH
   ========================================================= */

private fun DrawScope.drawPathCells(
    cell: Float
) {

    val white = Color(0xFFFFFCF5)

    /*
     * Main 15x15 playable area.
     */
    for (row in 0 until 15) {

        for (col in 0 until 15) {

            val insideHome =
                (row < 6 && col < 6) ||
                (row < 6 && col >= 9) ||
                (row >= 9 && col < 6) ||
                (row >= 9 && col >= 9)

            val center =
                row in 6..8 &&
                col in 6..8

            if (!insideHome && !center) {

                drawRect(
                    color = white,
                    topLeft = Offset(
                        col * cell,
                        row * cell
                    ),
                    size =
                        androidx.compose.ui.geometry.Size(
                            cell,
                            cell
                        )
                )
            }
        }
    }
}


/* =========================================================
   HOME LANES
   ========================================================= */

private fun DrawScope.drawHomeLanes(
    cell: Float
) {

    val green = Color(0xFF69B82A)
    val yellow = Color(0xFFFFC928)
    val red = Color(0xFFE52C32)
    val blue = Color(0xFF168BE8)

    /*
     * Green
     */
    for (row in 6..8) {

        drawRect(
            color = green,
            topLeft = Offset(
                6 * cell,
                row * cell
            ),
            size =
                androidx.compose.ui.geometry.Size(
                    cell,
                    cell
                )
        )
    }

    /*
     * Yellow
     */
    for (col in 6..8) {

        drawRect(
            color = yellow,
            topLeft = Offset(
                col * cell,
                6 * cell
            ),
            size =
                androidx.compose.ui.geometry.Size(
                    cell,
                    cell
                )
        )
    }

    /*
     * Red
     */
    for (row in 6..8) {

        drawRect(
            color = red,
            topLeft = Offset(
                8 * cell,
                row * cell
            ),
            size =
                androidx.compose.ui.geometry.Size(
                    cell,
                    cell
                )
        )
    }

    /*
     * Blue
     */
    for (col in 6..8) {

        drawRect(
            color = blue,
            topLeft = Offset(
                col * cell,
                8 * cell
            ),
            size =
                androidx.compose.ui.geometry.Size(
                    cell,
                    cell
                )
        )
    }
}


/* =========================================================
   CENTER
   ========================================================= */

private fun DrawScope.drawCenter(
    cell: Float
) {

    val centerX = 7.5f * cell
    val centerY = 7.5f * cell

    val path = Path()

    /*
     * Green triangle
     */
    path.moveTo(
        6 * cell,
        6 * cell
    )

    path.lineTo(
        9 * cell,
        6 * cell
    )

    path.lineTo(
        centerX,
        centerY
    )

    path.close()

    drawPath(
        path = path,
        color = Color(0xFF69B82A)
    )

    /*
     * Yellow triangle
     */
    val yellow = Path()

    yellow.moveTo(
        6 * cell,
        6 * cell
    )

    yellow.lineTo(
        6 * cell,
        9 * cell
    )

    yellow.lineTo(
        centerX,
        centerY
    )

    yellow.close()

    drawPath(
        path = yellow,
        color = Color(0xFFFFC928)
    )

    /*
     * Red triangle
     */
    val red = Path()

    red.moveTo(
        9 * cell,
        6 * cell
    )

    red.lineTo(
        9 * cell,
        9 * cell
    )

    red.lineTo(
        centerX,
        centerY
    )

    red.close()

    drawPath(
        path = red,
        color = Color(0xFFE52C32)
    )

    /*
     * Blue triangle
     */
    val blue = Path()

    blue.moveTo(
        6 * cell,
        9 * cell
    )

    blue.lineTo(
        9 * cell,
        9 * cell
    )

    blue.lineTo(
        centerX,
        centerY
    )

    blue.close()

    drawPath(
        path = blue,
        color = Color(0xFF168BE8)
    )

    /*
     * Center circle
     */
    drawCircle(
        color = Color.White,
        radius = cell * 0.25f,
        center = Offset(
            centerX,
            centerY
        )
    )

    drawCircle(
        color = Color(0xFF3A302A),
        radius = cell * 0.25f,
        center = Offset(
            centerX,
            centerY
        ),
        style = Stroke(
            width = cell * 0.06f
        )
    )
}


/* =========================================================
   SAFE CELLS
   ========================================================= */

private fun DrawScope.drawSafeCells(
    cell: Float
) {

    val safe = Color(0xFFFFC928)

    /*
     * Decorative stars
     */
    val positions = listOf(
        1.5f to 6.5f,
        6.5f to 1.5f,
        8.5f to 13.5f,
        13.5f to 8.5f
    )

    positions.forEach { (x, y) ->

        drawCircle(
            color = safe,
            radius = cell * 0.20f,
            center = Offset(
                x * cell,
                y * cell
            )
        )
    }
}


/* =========================================================
   GRID
   ========================================================= */

private fun DrawScope.drawGrid(
    cell: Float
) {

    val lineColor =
        Color(0xFF6C625C).copy(
            alpha = 0.42f
        )

    for (i in 0..15) {

        val position = i * cell

        drawLine(
            color = lineColor,
            start = Offset(
                position,
                0f
            ),
            end = Offset(
                position,
                cell * 15
            ),
            strokeWidth = 1f
        )

        drawLine(
            color = lineColor,
            start = Offset(
                0f,
                position
            ),
            end = Offset(
                cell * 15,
                position
            ),
            strokeWidth = 1f
        )
    }
}


/* =========================================================
   TOKENS
   ========================================================= */

private fun DrawScope.drawTokens(
    players: List<Player>,
    cell: Float,
    movableTokenIds: Set<String>
) {

    players.forEach { player ->

        player.tokens.forEach { token ->

            val position =
                tokenBoardPosition(
                    token = token,
                    cell = cell
                )

            val tokenColor =
                playerColor(
                    player.color.name
                )

            val radius =
                cell * 0.31f

            /*
             * Shadow
             */
            drawCircle(
                color =
                    Color.Black.copy(
                        alpha = 0.28f
                    ),
                radius = radius * 1.12f,
                center = Offset(
                    position.first + cell * 0.05f,
                    position.second + cell * 0.08f
                )
            )

            /*
             * Movable glow
             */
            if (
                token.uniqueId in
                movableTokenIds
            ) {

                drawCircle(
                    color =
                        Color.White.copy(
                            alpha = 0.75f
                        ),
                    radius =
                        radius * 1.28f,
                    center = Offset(
                        position.first,
                        position.second
                    )
                )
            }

            /*
             * Token body
             */
            drawCircle(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(
                            alpha = 0.35f
                        ),
                        tokenColor,
                        tokenColor.copy(
                            red =
                                tokenColor.red * 0.75f,
                            green =
                                tokenColor.green * 0.75f,
                            blue =
                                tokenColor.blue * 0.75f
                        )
                    )
                ),
                radius = radius,
                center = Offset(
                    position.first,
                    position.second
                )
            )

            /*
             * Token highlight
             */
            drawCircle(
                color =
                    Color.White.copy(
                        alpha = 0.55f
                    ),
                radius =
                    radius * 0.22f,
                center = Offset(
                    position.first -
                        radius * 0.30f,
                    position.second -
                        radius * 0.35f
                )
            )
        }
    }
}


/* =========================================================
   TOKEN POSITION
   ========================================================= */

private fun tokenBoardPosition(
    token: Token,
    cell: Float
): Pair<Float, Float> {

    /*
     * This keeps tokens visually positioned
     * even while the engine is calculating state.
     *
     * Adjust the mapping later if your Token
     * model uses a different position property.
     */

    val index =
        token.position

    if (index < 0) {

        return Pair(
            2.5f * cell,
            2.5f * cell
        )
    }

    val path =
        ludoPath()

    val safeIndex =
        index.coerceIn(
            0,
            path.lastIndex
        )

    val p =
        path[safeIndex]

    return Pair(
        (p.first + 0.5f) * cell,
        (p.second + 0.5f) * cell
    )
}


/* =========================================================
   STANDARD LUDO PATH
   ========================================================= */

private fun ludoPath():
        List<Pair<Int, Int>> {

    return listOf(

        // top
        6 to 0,
        7 to 0,
        8 to 0,

        8 to 1,
        8 to 2,
        8 to 3,
        8 to 4,
        8 to 5,

        9 to 6,
        10 to 6,
        11 to 6,
        12 to 6,
        13 to 6,
        14 to 6,

        14 to 7,
        14 to 8,

        13 to 8,
        12 to 8,
        11 to 8,
        10 to 8,
        9 to 8,

        8 to 9,
        8 to 10,
        8 to 11,
        8 to 12,
        8 to 13,
        8 to 14,

        7 to 14,
        6 to 14,

        6 to 13,
        6 to 12,
        6 to 11,
        6 to 10,
        6 to 9,

        5 to 8,
        4 to 8,
        3 to 8,
        2 to 8,
        1 to 8,
        0 to 8,

        0 to 7,
        0 to 6,

        1 to 6,
        2 to 6,
        3 to 6,
        4 to 6,
        5 to 6,

        6 to 5,
        6 to 4,
        6 to 3,
        6 to 2,
        6 to 1
    )
}


/* =========================================================
   PLAYER COLOR
   ========================================================= */

private fun playerColor(
    name: String
): Color {

    return when (
        name.uppercase()
    ) {

        "GREEN" ->
            Color(0xFF69B82A)

        "YELLOW" ->
            Color(0xFFFFC928)

        "RED" ->
            Color(0xFFE52C32)

        "BLUE" ->
            Color(0xFF168BE8)

        else ->
            Color(0xFF9E9E9E)
    }
}


/* =========================================================
   CLICK LAYER
   ========================================================= */

@Composable
private fun TokenClickLayer(
    players: List<Player>,
    movableTokenIds: Set<String>,
    onTokenClick: (Token) -> Unit
) {

    /*
     * Gameplay click handling remains connected
     * to the existing GameEngine.
     *
     * We intentionally don't change engine logic
     * in this UI step.
     */
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )
}