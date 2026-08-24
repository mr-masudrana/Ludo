package com.rana.ludo.engine

import com.rana.ludo.model.Token

object LudoRules {

    const val HOME_POSITION = -1
    const val START_POSITION = 0
    const val FINISH_POSITION = 56

    fun canLeaveHome(
        token: Token,
        dice: Int
    ): Boolean {

        return token.isHome && dice == 6
    }

    fun canMove(
        token: Token,
        dice: Int
    ): Boolean {

        if (dice !in 1..6) {
            return false
        }

        if (token.isFinished) {
            return false
        }

        if (token.isHome) {
            return dice == 6
        }

        return token.position + dice <= FINISH_POSITION
    }

    fun calculateNewPosition(
        token: Token,
        dice: Int
    ): Int {

        if (!canMove(token, dice)) {
            return token.position
        }

        if (token.isHome && dice == 6) {
            return START_POSITION
        }

        return token.position + dice
    }
}
