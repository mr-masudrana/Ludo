package com.rana.ludo.engine

data class DiceResult(
    val value: Int,
    val thirdSix: Boolean = false
)