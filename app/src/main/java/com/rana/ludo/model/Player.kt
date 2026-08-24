package com.rana.ludo.model

data class Player(
    val color: PlayerColor,
    val tokens: MutableList<Token>,
    val isComputer: Boolean = false
)