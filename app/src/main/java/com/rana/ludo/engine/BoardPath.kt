package com.rana.ludo.engine

import com.rana.ludo.model.PlayerColor

data class BoardCell(
    val row: Int,
    val column: Int
)

object BoardPath {

    /*
     * 52-cell outer path
     *
     * Global positions:
     *
     * 0..51
     */

    val path: List<BoardCell> = listOf(

        // 0 - 4
        BoardCell(6, 1),
        BoardCell(6, 2),
        BoardCell(6, 3),
        BoardCell(6, 4),
        BoardCell(6, 5),

        // 5 - 10
        BoardCell(5, 6),
        BoardCell(4, 6),
        BoardCell(3, 6),
        BoardCell(2, 6),
        BoardCell(1, 6),
        BoardCell(0, 6),

        // 11 - 12
        BoardCell(0, 7),
        BoardCell(0, 8),

        // 13 - 17
        BoardCell(1, 8),
        BoardCell(2, 8),
        BoardCell(3, 8),
        BoardCell(4, 8),
        BoardCell(5, 8),

        // 18 - 23
        BoardCell(6, 9),
        BoardCell(6, 10),
        BoardCell(6, 11),
        BoardCell(6, 12),
        BoardCell(6, 13),
        BoardCell(6, 14),

        // 24 - 25
        BoardCell(7, 14),
        BoardCell(8, 14),

        // 26 - 30
        BoardCell(8, 13),
        BoardCell(8, 12),
        BoardCell(8, 11),
        BoardCell(8, 10),
        BoardCell(8, 9),

        // 31 - 36
        BoardCell(9, 8),
        BoardCell(10, 8),
        BoardCell(11, 8),
        BoardCell(12, 8),
        BoardCell(13, 8),
        BoardCell(14, 8),

        // 37 - 38
        BoardCell(14, 7),
        BoardCell(14, 6),

        // 39 - 43
        BoardCell(13, 6),
        BoardCell(12, 6),
        BoardCell(11, 6),
        BoardCell(10, 6),
        BoardCell(9, 6),

        // 44 - 49
        BoardCell(8, 5),
        BoardCell(8, 4),
        BoardCell(8, 3),
        BoardCell(8, 2),
        BoardCell(8, 1),
        BoardCell(8, 0),

        // 50 - 51
        BoardCell(7, 0),
        BoardCell(6, 0)
    )

    /*
     * Player-specific starting positions
     */
    fun startPosition(
        color: PlayerColor
    ): Int {

        return when (color) {

            PlayerColor.GREEN ->
                0

            PlayerColor.RED ->
                13

            PlayerColor.BLUE ->
                26

            PlayerColor.YELLOW ->
                39
        }
    }

    /*
     * Global board position
     * থেকে cell বের করা
     */
    fun getCell(
        globalPosition: Int
    ): BoardCell? {

        return path.getOrNull(
            globalPosition
        )
    }

    /*
     * Player-এর local position
     * → global position
     */
    fun globalPosition(
        color: PlayerColor,
        localPosition: Int
    ): Int {

        if (
            localPosition !in 0..51
        ) {
            return -1
        }

        return (
            startPosition(color) +
                    localPosition
            ) % 52
    }

    /*
     * Final colored home lane
     *
     * Local position:
     *
     * 52
     * 53
     * 54
     * 55
     *
     * 56 = center
     */
    fun homeLane(
        color: PlayerColor
    ): List<BoardCell> {

        return when (color) {

            PlayerColor.GREEN -> listOf(
                BoardCell(7, 1),
                BoardCell(7, 2),
                BoardCell(7, 3),
                BoardCell(7, 4),
                BoardCell(7, 5)
            )

            PlayerColor.RED -> listOf(
                BoardCell(1, 7),
                BoardCell(2, 7),
                BoardCell(3, 7),
                BoardCell(4, 7),
                BoardCell(5, 7)
            )

            PlayerColor.BLUE -> listOf(
                BoardCell(7, 13),
                BoardCell(7, 12),
                BoardCell(7, 11),
                BoardCell(7, 10),
                BoardCell(7, 9)
            )

            PlayerColor.YELLOW -> listOf(
                BoardCell(13, 7),
                BoardCell(12, 7),
                BoardCell(11, 7),
                BoardCell(10, 7),
                BoardCell(9, 7)
            )
        }
    }

    /*
     * Token-এর local position থেকে
     * actual board cell বের করা
     */
    fun getTokenCell(
        color: PlayerColor,
        position: Int
    ): BoardCell? {

        if (
            position !in 0..56
        ) {
            return null
        }

        // Main board
        if (position <= 51) {

            val global =
                globalPosition(
                    color,
                    position
                )

            return getCell(global)
        }

        // Home lane
        if (position in 52..55) {

            val laneIndex =
                position - 52

            return homeLane(
                color
            ).getOrNull(laneIndex)
        }

        // 56 = center
        if (position == 56) {
            return BoardCell(7, 7)
        }

        return null
    }

    /*
     * Safe cells
     */
    fun isSafeCell(
        globalPosition: Int
    ): Boolean {

        return globalPosition in setOf(
            0,
            8,
            13,
            21,
            26,
            34,
            39,
            47
        )
    }
}