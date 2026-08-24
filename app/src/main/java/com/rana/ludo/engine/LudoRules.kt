package com.rana.ludo.engine

import com.rana.ludo.model.Token

object LudoRules {

    const val HOME_POSITION = -1

    const val START_POSITION = 0

    /*
     * 0..51
     * Main board
     *
     * 52..55
     * Home lane
     *
     * 56
     * Finish
     */
    const val LAST_POSITION = 56

    fun canLeaveHome(
        token: Token,
        dice: Int
    ): Boolean {

        return token.isHome &&
                dice == 6
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

        /*
         * Home থেকে বের হতে 6 লাগবে
         */
        if (token.isHome) {

            return dice == 6
        }

        /*
         * Exact finish
         */
        return token.position + dice <=
                LAST_POSITION
    }

    fun calculateNewPosition(
        token: Token,
        dice: Int
    ): Int {

        if (
            !canMove(
                token,
                dice
            )
        ) {
            return token.position
        }

        /*
         * Home → Start
         */
        if (
            token.isHome &&
            dice == 6
        ) {
            return START_POSITION
        }

        return token.position + dice
    }

    fun shouldFinish(
        position: Int
    ): Boolean {

        return position ==
                LAST_POSITION
    }

    /*
     * পথে opponent blockade আছে কিনা
     */
    fun pathBlocked(
        token: Token,
        dice: Int,
        isBlocked: (Int) -> Boolean
    ): Boolean {

        if (token.isHome) {
            return false
        }

        val target =
            token.position + dice

        /*
         * Home lane-এর মধ্যে
         * blockade check দরকার নেই
         */
        if (target > 51) {
            return false
        }

        for (
            position in
            (token.position + 1)..target
        ) {

            if (
                isBlocked(position)
            ) {
                return true
            }
        }

        return false
    }
}