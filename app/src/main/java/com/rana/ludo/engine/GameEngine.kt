package com.rana.ludo.engine

import com.rana.ludo.model.GameState
import com.rana.ludo.model.Player
import com.rana.ludo.model.Token

data class MoveResult(
    val success: Boolean = false,
    val captured: Boolean = false,
    val capturedTokenId: Int? = null,
    val finished: Boolean = false,
    val playerWon: Boolean = false,
    val extraTurn: Boolean = false,
    val nextPlayer: Boolean = false,
    val message: String = ""
)

class GameEngine(
    val state: GameState
) {

    private var diceRolled = false

    /*
     * Current player
     */
    fun currentPlayer(): Player {

        return state.players[
            state.currentPlayerIndex
        ]
    }

    /*
     * Current player-এর token
     */
    fun getToken(
        tokenId: Int
    ): Token? {

        return currentPlayer()
            .tokens
            .find {
                it.id == tokenId
            }
    }

    /*
     * Dice roll
     */
    fun rollDiceResult(): DiceResult {

        /*
         * একই turn-এ আবার roll করা যাবে না
         */
        if (diceRolled) {

            return DiceResult(
                value = state.diceValue,
                thirdSix = false
            )
        }

        val value =
            Dice.roll()

        /*
         * Six counter
         */
        if (value == 6) {

            state.consecutiveSixes++

        } else {

            state.consecutiveSixes = 0
        }

        diceRolled = true

        state.diceValue =
            value

        /*
         * তিনবার consecutive 6
         */
        val thirdSix =
            state.consecutiveSixes >= 3

        return DiceResult(
            value = value,
            thirdSix = thirdSix
        )
    }

    /*
     * Compatibility function
     */
    fun rollDice(): Int {

        return rollDiceResult().value
    }

    fun hasRolledDice(): Boolean {
        return diceRolled
    }

    /*
     * কোন token move করতে পারবে
     */
    fun getMovableTokens(): List<Token> {

        if (!diceRolled) {
            return emptyList()
        }

        return currentPlayer()
            .tokens
            .filter { token ->

                if (
                    !LudoRules.canMove(
                        token,
                        state.diceValue
                    )
                ) {
                    return@filter false
                }

                /*
                 * Opponent blockade check
                 */
                val blocked =
                    LudoRules.pathBlocked(
                        token = token,
                        dice = state.diceValue
                    ) { localPosition ->

                        opponentHasBlockadeAt(
                            token,
                            localPosition
                        )
                    }

                !blocked
            }
    }

    fun getMovableTokenIds(): Set<String> {

        return getMovableTokens()
            .map {
                it.uniqueId
            }
            .toSet()
    }

    fun hasMovableToken(): Boolean {

        return getMovableTokens()
            .isNotEmpty()
    }

    /*
     * Token কত position পর্যন্ত যাবে
     */
    fun getMovePath(
        tokenId: Int
    ): List<Int> {

        val token =
            getToken(tokenId)
                ?: return emptyList()

        val dice =
            state.diceValue

        if (
            !LudoRules.canMove(
                token,
                dice
            )
        ) {
            return emptyList()
        }

        /*
         * Home → Start
         */
        if (token.isHome) {

            return listOf(
                LudoRules.START_POSITION
            )
        }

        val target =
            LudoRules.calculateNewPosition(
                token,
                dice
            )

        return (
            token.position + 1..target
        ).toList()
    }

    /*
     * Token move
     */
    fun moveToken(
        tokenId: Int
    ): MoveResult {

        if (!diceRolled) {

            return MoveResult(
                message =
                    "Roll the dice first."
            )
        }

        val player =
            currentPlayer()

        val token =
            player.tokens.find {
                it.id == tokenId
            }
                ?: return MoveResult(
                    message =
                        "Token not found."
                )

        val dice =
            state.diceValue

        /*
         * Valid move?
         */
        if (
            !LudoRules.canMove(
                token,
                dice
            )
        ) {

            return MoveResult(
                message =
                    "This token cannot move."
            )
        }

        /*
         * Blockade
         */
        val blocked =
            LudoRules.pathBlocked(
                token = token,
                dice = dice
            ) { localPosition ->

                opponentHasBlockadeAt(
                    token,
                    localPosition
                )
            }

        if (blocked) {

            return MoveResult(
                message =
                    "A blockade is blocking the path."
            )
        }

        val newPosition =
            LudoRules.calculateNewPosition(
                token,
                dice
            )

        token.position =
            newPosition

        /*
         * Finish
         */
        val finished =
            LudoRules.shouldFinish(
                newPosition
            )

        if (finished) {

            token.isFinished = true
        }

        /*
         * Capture
         */
        val captureResult =
            if (finished) {

                CaptureResult()

            } else {

                captureOpponent(
                    token
                )
            }

        /*
         * Winner
         */
        val playerWon =
            player.tokens.all {
                it.isFinished
            }

        if (playerWon) {

            state.winner =
                player.color

            diceRolled = false
            state.diceValue = 0

            return MoveResult(

                success = true,

                captured =
                    captureResult.captured,

                capturedTokenId =
                    captureResult.capturedTokenId,

                finished = finished,

                playerWon = true,

                message =
                    "🏆 ${player.color.name} wins!"
            )
        }

        /*
         * Three consecutive six
         *
         * এই অবস্থাটি সাধারণত
         * roll করার সময় handle হবে।
         */

        /*
         * 6 হলে extra turn
         */
        if (dice == 6) {

            diceRolled = false
            state.diceValue = 0

            return MoveResult(

                success = true,

                captured =
                    captureResult.captured,

                capturedTokenId =
                    captureResult.capturedTokenId,

                finished = finished,

                extraTurn = true,

                message =
                    if (
                        captureResult.captured
                    ) {

                        "💥 Captured! Roll again."

                    } else {

                        "🎲 You rolled 6. Roll again."
                    }
            )
        }

        /*
         * সাধারণ turn শেষ
         */
        nextTurn()

        return MoveResult(

            success = true,

            captured =
                captureResult.captured,

            capturedTokenId =
                captureResult.capturedTokenId,

            finished = finished,

            nextPlayer = true,

            message =
                if (
                    captureResult.captured
                ) {

                    "💥 Token captured!"

                } else {

                    "Next player's turn."
                }
        )
    }

    /*
     * কোনো valid move না থাকলে
     */
    fun finishTurnIfNoMove(): Boolean {

        if (!diceRolled) {
            return false
        }

        if (hasMovableToken()) {
            return false
        }

        nextTurn()

        return true
    }

    /*
     * Opponent token capture
     */
    private fun captureOpponent(
        movingToken: Token
    ): CaptureResult {

        /*
         * Home lane-এ capture হবে না
         */
        if (
            movingToken.position > 51
        ) {
            return CaptureResult()
        }

        val globalPosition =
            BoardPath.globalPosition(
                movingToken.color,
                movingToken.position
            )

        /*
         * Safe cell
         */
        if (
            BoardPath.isSafeCell(
                globalPosition
            )
        ) {
            return CaptureResult()
        }

        for (
            player in state.players
        ) {

            /*
             * নিজের token বাদ
             */
            if (
                player.color ==
                movingToken.color
            ) {
                continue
            }

            for (
                opponent in player.tokens
            ) {

                if (
                    opponent.isHome ||
                    opponent.isFinished
                ) {
                    continue
                }

                if (
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

                    opponent.position =
                        LudoRules.HOME_POSITION

                    return CaptureResult(

                        captured = true,

                        capturedTokenId =
                            opponent.id
                    )
                }
            }
        }

        return CaptureResult()
    }

    /*
     * Opponent-এর দুই token
     * একই cell-এ আছে কিনা
     */
    private fun opponentHasBlockadeAt(
        movingToken: Token,
        localPosition: Int
    ): Boolean {

        if (
            localPosition > 51
        ) {
            return false
        }

        val globalPosition =
            BoardPath.globalPosition(
                movingToken.color,
                localPosition
            )

        for (
            player in state.players
        ) {

            if (
                player.color ==
                movingToken.color
            ) {
                continue
            }

            val count =
                player.tokens.count { token ->

                    if (
                        token.isHome ||
                        token.isFinished ||
                        token.position > 51
                    ) {
                        false
                    } else {

                        BoardPath.globalPosition(
                            token.color,
                            token.position
                        ) == globalPosition
                    }
                }

            if (count >= 2) {
                return true
            }
        }

        return false
    }

    /*
     * পরের player
     */
    fun nextTurn() {

        state.currentPlayerIndex++

        if (
            state.currentPlayerIndex >=
            state.players.size
        ) {

            state.currentPlayerIndex = 0
        }

        state.diceValue = 0
        state.consecutiveSixes = 0

        diceRolled = false
    }

    fun playerHasWon(): Boolean {

        return currentPlayer()
            .tokens
            .all {
                it.isFinished
            }
    }
}