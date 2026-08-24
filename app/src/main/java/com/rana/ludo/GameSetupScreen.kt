package com.rana.ludo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun GameSetupScreen(
    selectedPlayers: Int,
    onPlayersSelected: (Int) -> Unit,
    onStartGame: () -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "🎲 Offline Ludo",
            style =
                MaterialTheme.typography.headlineLarge,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Choose Number of Players",
            style =
                MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            PlayerCountButton(
                count = 2,
                selected =
                    selectedPlayers == 2,
                onClick = {
                    onPlayersSelected(2)
                }
            )

            PlayerCountButton(
                count = 3,
                selected =
                    selectedPlayers == 3,
                onClick = {
                    onPlayersSelected(3)
                }
            )

            PlayerCountButton(
                count = 4,
                selected =
                    selectedPlayers == 4,
                onClick = {
                    onPlayersSelected(4)
                }
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text =
                    "Players: $selectedPlayers",
                modifier =
                    Modifier.padding(16.dp),
                fontWeight =
                    FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = onStartGame,
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text("Start Game")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedButton(
            onClick = onBack,
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
private fun PlayerCountButton(
    count: Int,
    selected: Boolean,
    onClick: () -> Unit
) {

    if (selected) {

        Button(
            onClick = onClick,
            modifier =
                Modifier.weight(1f)
        ) {
            Text("$count Players")
        }

    } else {

        OutlinedButton(
            onClick = onClick,
            modifier =
                Modifier.weight(1f)
        ) {
            Text("$count Players")
        }
    }
}
