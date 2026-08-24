package com.rana.ludo.model

object GameFactory {

    fun createGame(): GameState {

        val players = mutableListOf<Player>()

        PlayerColor.entries.forEach { color ->

            val tokens = mutableListOf<Token>()

            repeat(4) { index ->

                tokens.add(
                    Token(
                        id = index,
                        color = color
                    )
                )
            }

            players.add(
                Player(
                    color = color,
                    tokens = tokens
                )
            )
        }

        return GameState(
            players = players
        )
    }
}
