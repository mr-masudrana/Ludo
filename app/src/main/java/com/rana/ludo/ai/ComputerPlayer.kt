package com.rana.ludo.ai

import com.rana.ludo.engine.GameEngine
import com.rana.ludo.model.Token

object ComputerPlayer {

    fun chooseToken(
        engine: GameEngine
    ): Token? {

        val movable =
            engine.getMovableTokens()

        if (movable.isEmpty()) {
            return null
        }

        /*
         * Priority 1:
         * Finish করার কাছাকাছি token
         */
        val finishingToken =
            movable.maxByOrNull {
                it.position
            }

        if (finishingToken != null) {
            return finishingToken
        }

        /*
         * Priority 2:
         * Random valid token
         */
        return movable.randomOrNull()
    }
}
