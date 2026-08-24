package com.rana.ludo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rana.ludo.model.AiDifficulty
import com.rana.ludo.model.GameMode

@Composable
fun GameSetupScreen(
    selectedPlayers: Int,
    selectedMode: GameMode,
    selectedDifficulty: AiDifficulty,

    onPlayersSelected: (Int) -> Unit,
    onModeSelected: (GameMode) -> Unit,
    onDifficultySelected: (AiDifficulty) -> Unit,

    onStartGame: () -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "🎲 Game Setup",
            style =
                MaterialTheme.typography
                    .headlineMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        /*
         * -----------------------------------------
         * PLAYER COUNT
         * -----------------------------------------
         */

        Text(
            text = "Number of Players",
            style =
                MaterialTheme.typography
                    .titleMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            PlayerButton(
                text = "2",
                selected =
                    selectedPlayers == 2,

                onClick = {
                    onPlayersSelected(2)
                },

                modifier =
                    Modifier.weight(1f)
            )

            PlayerButton(
                text = "3",
                selected =
                    selectedPlayers == 3,

                onClick = {
                    onPlayersSelected(3)
                },

                modifier =
                    Modifier.weight(1f)
            )

            PlayerButton(
                text = "4",
                selected =
                    selectedPlayers == 4,

                onClick = {
                    onPlayersSelected(4)
                },

                modifier =
                    Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "$selectedPlayers Players",
            style =
                MaterialTheme.typography
                    .bodyMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        /*
         * -----------------------------------------
         * GAME MODE
         * -----------------------------------------
         */

        Text(
            text = "Game Mode",
            style =
                MaterialTheme.typography
                    .titleMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            ModeButton(
                text = "👥 Local",
                selected =
                    selectedMode ==
                            GameMode.LOCAL,

                onClick = {
                    onModeSelected(
                        GameMode.LOCAL
                    )
                },

                modifier =
                    Modifier.weight(1f)
            )

            ModeButton(
                text = "🤖 Computer",
                selected =
                    selectedMode ==
                            GameMode.VS_COMPUTER,

                onClick = {
                    onModeSelected(
                        GameMode.VS_COMPUTER
                    )
                },

                modifier =
                    Modifier.weight(1f)
            )
        }

        /*
         * -----------------------------------------
         * AI DIFFICULTY
         * -----------------------------------------
         */

        if (
            selectedMode ==
            GameMode.VS_COMPUTER
        ) {

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Text(
                text = "AI Difficulty",
                style =
                    MaterialTheme.typography
                        .titleMedium,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {

                DifficultyButton(
                    text = "Easy",
                    selected =
                        selectedDifficulty ==
                                AiDifficulty.EASY,

                    onClick = {
                        onDifficultySelected(
                            AiDifficulty.EASY
                        )
                    },

                    modifier =
                        Modifier.weight(1f)
                )

                DifficultyButton(
                    text = "Medium",
                    selected =
                        selectedDifficulty ==
                                AiDifficulty.MEDIUM,

                    onClick = {
                        onDifficultySelected(
                            AiDifficulty.MEDIUM
                        )
                    },

                    modifier =
                        Modifier.weight(1f)
                )

                DifficultyButton(
                    text = "Hard",
                    selected =
                        selectedDifficulty ==
                                AiDifficulty.HARD,

                    onClick = {
                        onDifficultySelected(
                            AiDifficulty.HARD
                        )
                    },

                    modifier =
                        Modifier.weight(1f)
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )

        /*
         * -----------------------------------------
         * SUMMARY
         * -----------------------------------------
         */

        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Column(
                modifier =
                    Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Game Summary",
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Players: $selectedPlayers"
                )

                Text(
                    text =
                        when (selectedMode) {

                            GameMode.LOCAL ->
                                "Mode: Local Multiplayer"

                            GameMode.VS_COMPUTER ->
                                "Mode: vs Computer"
                        }
                )

                if (
                    selectedMode ==
                    GameMode.VS_COMPUTER
                ) {

                    Text(
                        text =
                            "Difficulty: ${
                                selectedDifficulty.name
                            }"
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        /*
         * -----------------------------------------
         * START GAME
         * -----------------------------------------
         */

        Button(
            onClick = onStartGame,
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text = "🎮 Start Game"
            )
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        OutlinedButton(
            onClick = onBack,
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text = "← Back"
            )
        }
    }
}

/* ------------------------------------------------ */
/* PLAYER BUTTON                                   */
/* ------------------------------------------------ */

@Composable
private fun PlayerButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (selected) {

        Button(
            onClick = onClick,
            modifier = modifier
        ) {

            Text(
                text = text
            )
        }

    } else {

        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {

            Text(
                text = text
            )
        }
    }
}

/* ------------------------------------------------ */
/* MODE BUTTON                                     */
/* ------------------------------------------------ */

@Composable
private fun ModeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (selected) {

        Button(
            onClick = onClick,
            modifier = modifier
        ) {

            Text(
                text = text
            )
        }

    } else {

        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {

            Text(
                text = text
            )
        }
    }
}

/* ------------------------------------------------ */
/* DIFFICULTY BUTTON                               */
/* ------------------------------------------------ */

@Composable
private fun DifficultyButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (selected) {

        Button(
            onClick = onClick,
            modifier = modifier
        ) {

            Text(
                text = text
            )
        }

    } else {

        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {

            Text(
                text = text
            )
        }
    }
}