package com.rana.ludo.ai

import com.rana.ludo.engine.BoardPath
import com.rana.ludo.engine.GameEngine
import com.rana.ludo.engine.LudoRules
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

        // 1. Finish করার সুযোগ
        movable.firstOrNull { token ->

            token.position >= 50 &&
                    token.position +
                    engine.state.diceValue >=
                    LudoRules.LAST_POSITION

        }?.let {
            return it
        }

        // 2. Home থেকে বের করার সুযোগ
        movable.firstOrNull { token ->
            token.isHome &&
                    engine.state.diceValue == 6
        }?.let {
            return it
        }

        // 3. Safe cell-এ যাওয়ার চেষ্টা
        movable.firstOrNull { token ->

            val newPosition =
                LudoRules.calculateNewPosition(
                    token,
                    engine.state.diceValue
                )

            if (newPosition > 51) {
                false
            } else {

                val global =
                    BoardPath.globalPosition(
                        token.color,
                        newPosition
                    )

                BoardPath.isSafeCell(
                    global
                )
            }

        }?.let {
            return it
        }

        // 4. সবচেয়ে এগিয়ে থাকা token
        return movable.maxByOrNull {
            it.position
        }
    }
}
