package com.rana.ludo.ai

import com.rana.ludo.engine.BoardPath
import com.rana.ludo.engine.GameEngine
import com.rana.ludo.engine.LudoRules
import com.rana.ludo.model.AiDifficulty
import com.rana.ludo.model.Token
import kotlin.random.Random

object ComputerPlayer {

    fun chooseToken(
        engine: GameEngine,
        difficulty: AiDifficulty
    ): Token? {

        val movableTokens =
            engine.getMovableTokens()

        if (movableTokens.isEmpty()) {
            return null
        }

        return when (difficulty) {

            AiDifficulty.EASY ->
                chooseEasy(movableTokens)

            AiDifficulty.MEDIUM ->
                chooseMedium(
                    engine,
                    movableTokens
                )

            AiDifficulty.HARD ->
                chooseHard(
                    engine,
                    movableTokens
                )
        }
    }

    /*
     * Easy:
     * যেকোনো valid token
     */
    private fun chooseEasy(
        tokens: List<Token>
    ): Token {

        return tokens.random()
    }

    /*
     * Medium:
     *
     * 1. Finish
     * 2. Capture
     * 3. Home থেকে বের করা
     * 4. Safe cell
     * 5. সামনে থাকা token
     */
    private fun chooseMedium(
        engine: GameEngine,
        tokens: List<Token>
    ): Token {

        tokens.firstOrNull { token ->

            canFinish(
                engine,
                token
            )

        }?.let {
            return it
        }

        tokens.firstOrNull { token ->

            canCapture(
                engine,
                token
            )

        }?.let {
            return it
        }

        tokens.firstOrNull { token ->

            token.isHome &&
                    engine.state.diceValue == 6

        }?.let {
            return it
        }

        tokens.firstOrNull { token ->

            reachesSafeCell(
                engine,
                token
            )

        }?.let {
            return it
        }

        return tokens.maxByOrNull {
            it.position
        } ?: tokens.first()
    }

    /*
     * Hard:
     * প্রতিটি valid token-এর score
     * হিসাব করে best move নেয়।
     */
    private fun chooseHard(
        engine: GameEngine,
        tokens: List<Token>
    ): Token {

        return tokens.maxByOrNull { token ->

            calculateScore(
                engine,
                token
            )

        } ?: tokens.first()
    }

    private fun calculateScore(
        engine: GameEngine,
        token: Token
    ): Int {

        val dice =
            engine.state.diceValue

        val newPosition =
            LudoRules.calculateNewPosition(
                token,
                dice
            )

        var score = 0

        /*
         * Finish
         */
        if (
            newPosition ==
            LudoRules.LAST_POSITION
        ) {
            score += 1000
        }

        /*
         * Capture
         */
        if (
            canCapture(
                engine,
                token
            )
        ) {
            score += 800
        }

        /*
         * Home থেকে বের করা
         */
        if (
            token.isHome &&
            dice == 6
        ) {
            score += 400
        }

        /*
         * Safe cell
         */
        if (
            reachesSafeCell(
                engine,
                token
            )
        ) {
            score += 300
        }

        /*
         * Finish-এর কাছাকাছি
         */
        score += newPosition * 5

        /*
         * ছোট random factor,
         * যাতে প্রতিবার একই move না নেয়।
         */
        score += Random.nextInt(0, 10)

        return score
    }

    private fun canFinish(
        engine: GameEngine,
        token: Token
    ): Boolean {

        val newPosition =
            LudoRules.calculateNewPosition(
                token,
                engine.state.diceValue
            )

        return newPosition ==
                LudoRules.LAST_POSITION
    }

    private fun reachesSafeCell(
        engine: GameEngine,
        token: Token
    ): Boolean {

        val newPosition =
            LudoRules.calculateNewPosition(
                token,
                engine.state.diceValue
            )

        if (
            newPosition > 51
        ) {
            return false
        }

        val globalPosition =
            BoardPath.globalPosition(
                token.color,
                newPosition
            )

        return BoardPath.isSafeCell(
            globalPosition
        )
    }

    private fun canCapture(
        engine: GameEngine,
        token: Token
    ): Boolean {

        val newPosition =
            LudoRules.calculateNewPosition(
                token,
                engine.state.diceValue
            )

        /*
         * Home lane-এ capture নেই
         */
        if (
            newPosition > 51
        ) {
            return false
        }

        val globalPosition =
            BoardPath.globalPosition(
                token.color,
                newPosition
            )

        /*
         * Safe cell-এ capture নেই
         */
        if (
            BoardPath.isSafeCell(
                globalPosition
            )
        ) {
            return false
        }

        /*
         * Opponent token খুঁজে দেখা
         */
        for (
            player in engine.state.players
        ) {

            if (
                player.color ==
                token.color
            ) {
                continue
            }

            for (
                opponent in player.tokens
            ) {

                if (
                    opponent.isHome ||
                    opponent.isFinished ||
                    opponent.position > 51
                ) {
                    continue
                }

                val opponentGlobal =
                    BoardPath.globalPosition(
                        opponent.color,
                        opponent.position
                    )

                if (
                    opponentGlobal ==
                    globalPosition
                ) {
                    return true
                }
            }
        }

        return false
    }
}