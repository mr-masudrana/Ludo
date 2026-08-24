package com.rana.ludo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rana.ludo.model.AiDifficulty
import com.rana.ludo.model.GameMode

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {

            MaterialTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme
                        .colorScheme
                        .background
                ) {

                    /*
                     * App navigation state
                     */
                    var screen by remember {
                        mutableStateOf(
                            AppScreen.HOME
                        )
                    }

                    /*
                     * Game settings
                     */
                    var playerCount by remember {
                        mutableIntStateOf(4)
                    }

                    var gameMode by remember {
                        mutableStateOf(
                            GameMode.LOCAL
                        )
                    }

                    var aiDifficulty by remember {
                        mutableStateOf(
                            AiDifficulty.MEDIUM
                        )
                    }

                    /*
                     * Navigation
                     */
                    when (screen) {

                        /*
                         * HOME
                         */
                        AppScreen.HOME -> {

                            HomeScreen(
                                onNewGame = {

                                    screen =
                                        AppScreen.SETUP
                                },

                                onExit = {

                                    finish()
                                }
                            )
                        }

                        /*
                         * SETUP
                         */
                        AppScreen.SETUP -> {

                            GameSetupScreen(

                                selectedPlayers =
                                    playerCount,

                                selectedMode =
                                    gameMode,

                                selectedDifficulty =
                                    aiDifficulty,

                                onPlayersSelected = {
                                    playerCount = it
                                },

                                onModeSelected = {
                                    gameMode = it
                                },

                                onDifficultySelected = {
                                    aiDifficulty = it
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

                        /*
                         * GAME
                         */
                        AppScreen.GAME -> {

                            GameScreen(

                                playerCount =
                                    playerCount,

                                gameMode =
                                    gameMode,

                                aiDifficulty =
                                    aiDifficulty,

                                onNewGame = {

                                    screen =
                                        AppScreen.SETUP
                                },

                                onBack = {

                                    screen =
                                        AppScreen.HOME
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


/*
 * App screens
 */
enum class AppScreen {

    HOME,

    SETUP,

    GAME
}