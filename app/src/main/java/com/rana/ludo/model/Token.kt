package com.rana.ludo.model

data class Token(
    val id: Int,
    val color: PlayerColor,
    var position: Int = -1,
    var isFinished: Boolean = false
)
