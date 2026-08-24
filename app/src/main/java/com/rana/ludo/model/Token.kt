package com.rana.ludo.model

data class Token(
    val id: Int,
    val color: PlayerColor,
    var position: Int = -1,
    var isFinished: Boolean = false
) {

    val isHome: Boolean
        get() = position == -1

    val isOnBoard: Boolean
        get() = position >= 0 && !isFinished
}
