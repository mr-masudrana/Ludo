package com.rana.ludo.engine

data class BoardCell(
    val row: Int,
    val column: Int
)

object BoardPath {

    val path: List<BoardCell> = listOf(

        BoardCell(6, 1),
        BoardCell(6, 2),
        BoardCell(6, 3),
        BoardCell(6, 4),
        BoardCell(6, 5),

        BoardCell(5, 6),
        BoardCell(4, 6),
        BoardCell(3, 6),
        BoardCell(2, 6),
        BoardCell(1, 6),
        BoardCell(0, 6),

        BoardCell(0, 7),

        BoardCell(0, 8),
        BoardCell(1, 8),
        BoardCell(2, 8),
        BoardCell(3, 8),
        BoardCell(4, 8),
        BoardCell(5, 8),

        BoardCell(6, 9),
        BoardCell(6, 10),
        BoardCell(6, 11),
        BoardCell(6, 12),
        BoardCell(6, 13),
        BoardCell(6, 14),

        BoardCell(7, 14),

        BoardCell(8, 14),
        BoardCell(8, 13),
        BoardCell(8, 12),
        BoardCell(8, 11),
        BoardCell(8, 10),
        BoardCell(8, 9),

        BoardCell(9, 8),
        BoardCell(10, 8),
        BoardCell(11, 8),
        BoardCell(12, 8),
        BoardCell(13, 8),
        BoardCell(14, 8),

        BoardCell(14, 7),

        BoardCell(14, 6),
        BoardCell(13, 6),
        BoardCell(12, 6),
        BoardCell(11, 6),
        BoardCell(10, 6),
        BoardCell(9, 6),

        BoardCell(8, 5),
        BoardCell(8, 4),
        BoardCell(8, 3),
        BoardCell(8, 2),
        BoardCell(8, 1),
        BoardCell(8, 0),

        BoardCell(7, 0),

        BoardCell(6, 0)
    )

    fun getCell(position: Int): BoardCell? {

        if (position !in path.indices) {
            return null
        }

        return path[position]
    }
}
