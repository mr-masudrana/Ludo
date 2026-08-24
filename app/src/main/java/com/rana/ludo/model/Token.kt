package com.rana.ludo.model

data class Token(
    val id: Int,
    val color: PlayerColor,

    /*
     * -1  = Home
     * 0..51 = Main board
     * 52..55 = Home lane
     * 56 = Finish
     */
    var position: Int = -1,

    var isFinished: Boolean = false
) {

    val uniqueId: String
        get() = "${color.name}_$id"

    val isHome: Boolean
        get() = position == -1

    val isOnBoard: Boolean
        get() = position >= 0 && !isFinished
}