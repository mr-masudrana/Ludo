package com.rana.ludo.model

data class GameState(

    val players: MutableList<Player>,

    var currentPlayerIndex: Int = 0,

    var diceValue: Int = 0,

    var winner: PlayerColor? = null,

    var consecutiveSixes: Int = 0
)