package com.rana.ludo.engine

import com.rana.ludo.model.GameState
import com.rana.ludo.model.Player

class GameEngine(
    val state: GameState
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

        state.diceValue = 0
    }

    fun currentPlayer(): Player {
        return state.players[state.currentPlayerIndex]
    }
}
