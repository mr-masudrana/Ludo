package com.rana.ludo

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rana.ludo.engine.GameEngine
import com.rana.ludo.model.GameFactory
import com.rana.ludo.model.PlayerColor

@Composable
fun GameScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }

    val gameEngine = remember {
        GameEngine(
            GameFactory.createGame()
        )
    }

    var diceValue by remember {
        mutableIntStateOf(0)
    }

    var isRolling by remember {
        mutableStateOf(false)
    }

    var currentPlayerIndex by remember {
        mutableIntStateOf(0)
    }

    val rotation by animateFloatAsState(
        targetValue = if (isRolling) 360f else 0f,
        label = "dice_rotation"
    )

    val currentColor =
        gameEngine.state.players[currentPlayerIndex].color

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "🎲 Offline Ludo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        PlayerTurnCard(
            color = currentColor
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            LudoBoard()
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .rotate(rotation)
                    .background(
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = if (diceValue == 0) "🎲" else diceFace(diceValue),
                    fontSize = 38.sp
                )
            }

            Spacer(
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = {

                    if (!isRolling) {

                        isRolling = true

                        val result = gameEngine.rollDice()

                        diceValue = result

                        isRolling = false
                    }
                },
                enabled = !isRolling
            ) {
                Text("Roll Dice")
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedButton(
                onClick = {

                    gameEngine.nextTurn()

                    currentPlayerIndex =
                        gameEngine.state.currentPlayerIndex

                    diceValue = 0
                }
            ) {
                Text("End Turn")
            }

            OutlinedButton(
                onClick = {
                    onBack()
                }
            ) {
                Text("Exit")
            }
        }
    }
}

@Composable
private fun PlayerTurnCard(
    color: PlayerColor
) {

    val playerColor = color.toComposeColor()

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(
                        color = playerColor,
                        shape = MaterialTheme.shapes.small
                    )
            )

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Text(
                text = "${color.name} PLAYER'S TURN",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun diceFace(value: Int): String {

    return when (value) {
        1 -> "⚀"
        2 -> "⚁"
        3 -> "⚂"
        4 -> "⚃"
        5 -> "⚄"
        6 -> "⚅"
        else -> "🎲"
    }
}

private fun PlayerColor.toComposeColor(): Color {

    return when (this) {

        PlayerColor.GREEN ->
            Color(0xFF43A047)

        PlayerColor.RED ->
            Color(0xFFE53935)

        PlayerColor.YELLOW ->
            Color(0xFFFDD835)

        PlayerColor.BLUE ->
            Color(0xFF1E88E5)
    }
}
