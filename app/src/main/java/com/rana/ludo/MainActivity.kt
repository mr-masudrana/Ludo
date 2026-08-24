package com.rana.ludo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {

            MaterialTheme {

                var screen by mutableStateOf(
                    AppScreen.HOME
                )

                var playerCount by
                    mutableIntStateOf(4)

                when (screen) {

                    AppScreen.HOME -> {

                        HomeScreen(
                            onNewGame = {
                                screen =
                                    AppScreen.SETUP
                            }
                        )
                    }

                    AppScreen.SETUP -> {

                        GameSetupScreen(

                            selectedPlayers =
                                playerCount,

                            onPlayersSelected = {
                                playerCount = it
                            },

                            onStartGame = {
                                screen =
                                    AppScreen.GAME
                            },

                            onBack = {
                                screen =
                                    AppScreen.HOME
                            }
                        )
                    }

                    AppScreen.GAME -> {

                        GameScreen(
                            playerCount =
                                playerCount,

                            onBack = {
                                screen =
                                    AppScreen.SETUP
                            }
                        )
                    }
                }
            }
        }
    }
}

enum class AppScreen {
    HOME,
    SETUP,
    GAME
}
