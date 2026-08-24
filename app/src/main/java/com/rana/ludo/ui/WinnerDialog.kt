package com.rana.ludo.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.rana.ludo.model.PlayerColor

@Composable
fun WinnerDialog(
    winner: PlayerColor,
    onNewGame: () -> Unit,
    onExit: () -> Unit
) {

    AlertDialog(

        onDismissRequest = {},

        title = {
            Text(
                text = "🏆 Congratulations!"
            )
        },

        text = {
            Text(
                text =
                    "${winner.name} player won the game!"
            )
        },

        confirmButton = {

            Button(
                onClick = onNewGame
            ) {

                Text(
                    text = "New Game"
                )
            }
        },

        dismissButton = {

            OutlinedButton(
                onClick = onExit
            ) {

                Text(
                    text = "Exit"
                )
            }
        }
    )
}