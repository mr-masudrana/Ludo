package com.rana.ludo.model

object GameFactory {

    fun createGame(
        playerCount: Int = 4
    ): GameState {

        val colors =
            PlayerColor.entries
                .take(playerCount.coerceIn(2, 4))

        val players =
            colors.map { color ->

                val tokens =
                    MutableList(4) { index ->

                        Token(
                            id = index,
                            color = color
                        )
                    }

                Player(
                    color = color,
                    tokens = tokens
                )
            }.toMutableList()

        return GameState(
            players = players
        )
    }
}
