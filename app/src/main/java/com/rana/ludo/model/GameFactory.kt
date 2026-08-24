package com.rana.ludo.model

object GameFactory {

    fun createGame(
        playerCount: Int = 4,
        gameMode: GameMode = GameMode.LOCAL
    ): GameState {

        val count =
            playerCount.coerceIn(2, 4)

        val colors =
            PlayerColor.entries
                .take(count)

        val players =
            colors.mapIndexed { index, color ->

                val tokens =
                    MutableList(4) { tokenId ->

                        Token(
                            id = tokenId,
                            color = color
                        )
                    }

                Player(
                    color = color,
                    tokens = tokens,

                    /*
                     * VS Computer mode-এ
                     * প্রথম player = Human
                     * পরের player = Computer
                     */
                    isComputer =
                        gameMode ==
                                GameMode.VS_COMPUTER &&
                                index > 0
                )
            }.toMutableList()

        return GameState(
            players = players
        )
    }
}