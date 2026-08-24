package com.rana.ludo.engine

import com.rana.ludo.model.GameState
import com.rana.ludo.model.Player
import com.rana.ludo.model.Token

class GameEngine(
    val state: GameState
) {

    fun rollDice(): Int {

        val value = Dice.roll()

        state.diceValue = value

        return value
    }

    fun currentPlayer(): Player {
        return state.players[state.currentPlayerIndex]
    }

    fun getMovableTokens(): List<Token> {

        val player = currentPlayer()

        return player.tokens.filter { token ->
            LudoRules.canMove(
                token = token,
                dice = state.diceValue
            )
        }
    }

    fun moveToken(tokenId: Int): Boolean {

        val player = currentPlayer()

        val token = player.tokens.find {
            it.id == tokenId
        } ?: return false

        val dice = state.diceValue

        if (!LudoRules.canMove(token, dice)) {
            return false
        }

        token.position =
            LudoRules.calculateNewPosition(
                token = token,
                dice = dice
            )

        if (token.position == LudoRules.FINISH_POSITION) {
            token.isFinished = true
        }

        return true
    }

    fun hasMovableToken(): Boolean {
        return getMovableTokens().isNotEmpty()
    }

    fun playerHasWon(): Boolean {

        return currentPlayer()
            .tokens
            .all { it.isFinished }
    }

    fun nextTurn() {

        state.currentPlayerIndex++

        if (state.currentPlayerIndex >= state.players.size) {
            state.currentPlayerIndex = 0
        }

        state.diceValue = 0
    }
}
