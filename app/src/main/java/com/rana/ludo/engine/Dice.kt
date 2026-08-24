package com.rana.ludo.engine

import kotlin.random.Random

object Dice {

    fun roll(): Int {
        return Random.nextInt(1, 7)
    }
}
