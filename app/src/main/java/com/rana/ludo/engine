package com.rana.ludo.engine

import com.rana.ludo.model.GameState

class GameEngine(
    private val state: GameState
) {

    fun rollDice(): Int {

        val value = Dice.roll()

        state.diceValue = value

        return value
    }

    fun nextTurn() {

        state.currentPlayerIndex++

        if (state.currentPlayerIndex >= state.players.size) {
            state.currentPlayerIndex = 0
        }
    }

    fun currentPlayer() =
        state.players[state.currentPlayerIndex]
}
