package com.rana.ludo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
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

    var currentPlayerIndex by remember {
        mutableIntStateOf(0)
    }

    var refresh by remember {
        mutableIntStateOf(0)
    }

    val currentPlayer =
        gameEngine.state.players[currentPlayerIndex]

    val movableTokens =
        gameEngine.getMovableTokens()

    val movableTokenIds =
        movableTokens.map { it.id }.toSet()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "🎲 Offline Ludo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        PlayerTurnCard(
            color = currentPlayer.color
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = if (diceValue == 0) {
                "Roll the dice"
            } else {
                "Dice: $diceValue"
            },
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LudoBoard(
            tokens = currentPlayer.tokens,
            movableTokenIds = movableTokenIds,
            onTokenClick = { token ->

                val moved =
                    gameEngine.moveToken(token.id)

                if (moved) {

                    diceValue =
                        gameEngine.state.diceValue

                    refresh++

                    /*
                     * আপাতত move করার পরে turn
                     * manually End Turn দিয়ে পরিবর্তন হবে।
                     */
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            horizontalArrangement =
                Arrangement.Center,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = if (diceValue == 0) {
                    "🎲"
                } else {
                    diceFace(diceValue)
                },
                fontSize = 42.sp
            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            Button(
                onClick = {

                    val result =
                        gameEngine.rollDice()

                    diceValue = result

                    refresh++

                }
            ) {
                Text("Roll Dice")
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        if (diceValue > 0) {

            Text(
                text =
                    if (movableTokens.isEmpty()) {
                        "No valid move"
                    } else {
                        "Tap a highlighted token"
                    },
                color =
                    if (movableTokens.isEmpty()) {
                        Color.Red
                    } else {
                        Color.Unspecified
                    }
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            OutlinedButton(
                onClick = {

                    gameEngine.nextTurn()

                    currentPlayerIndex =
                        gameEngine.state.currentPlayerIndex

                    diceValue = 0

                    refresh++

                }
            ) {
                Text("End Turn")
            }

            OutlinedButton(
                onClick = onBack
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

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),

            horizontalArrangement =
                Arrangement.Center,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "${color.name} PLAYER'S TURN",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun diceFace(
    value: Int
): String {

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
