package com.rana.ludo.engine

import kotlin.random.Random

object Dice {

    fun roll(): Int {
        return Random.nextInt(
            from = 1,
            until = 7
        )
    }
}