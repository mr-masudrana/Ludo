package com.rana.ludo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rana.ludo.ai.ComputerPlayer
import com.rana.ludo.engine.GameEngine
import com.rana.ludo.model.AiDifficulty
import com.rana.ludo.model.GameFactory
import com.rana.ludo.model.GameMode
import com.rana.ludo.model.Player
import com.rana.ludo.model.Token
import com.rana.ludo.ui.DiceView
import com.rana.ludo.ui.WinnerDialog
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    playerCount: Int,
    gameMode: GameMode,
    aiDifficulty: AiDifficulty,
    onNewGame: () -> Unit,
    onBack: () -> Unit
) {

    val gameEngine = remember(
        playerCount,
        gameMode
    ) {
        GameEngine(
            GameFactory.createGame(
                playerCount = playerCount,
                gameMode = gameMode
            )
        )
    }

    var refresh by remember {
        mutableIntStateOf(0)
    }

    var diceValue by remember {
        mutableIntStateOf(0)
    }

    var isRolling by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf(
            "Roll the dice to start."
        )
    }

    val currentPlayer =
        gameEngine.currentPlayer()

    val winner =
        gameEngine.state.winner

    val computerTurn =
        gameMode == GameMode.VS_COMPUTER &&
                currentPlayer.isComputer

    /*
     * Current player's movable tokens
     */
    val movableTokenIds =
        if (
            !isRolling &&
            gameEngine.hasRolledDice()
        ) {
            gameEngine
                .getMovableTokenIds()
        } else {
            emptySet()
        }

    /*
     * ------------------------------------------
     * COMPUTER TURN
     * ------------------------------------------
     */

    LaunchedEffect(
        gameEngine.state.currentPlayerIndex,
        gameEngine.state.diceValue,
        winner
    ) {

        if (
            computerTurn &&
            winner == null
        ) {

            /*
             * Small thinking delay
             */
            delay(700)

            /*
             * Roll
             */
            isRolling = true

            delay(400)

            val diceResult =
                gameEngine.rollDiceResult()

            diceValue =
                diceResult.value

            isRolling = false

            refresh++

            /*
             * Three six
             */
            if (
                diceResult.thirdSix
            ) {

                message =
                    "🤖 Computer rolled three 6s!"

                delay(800)

                gameEngine.nextTurn()

                diceValue = 0

                message =
                    "Your turn."

                refresh++

                return@LaunchedEffect
            }

            /*
             * No move
             */
            if (
                !gameEngine.hasMovableToken()
            ) {

                message =
                    "🤖 No valid move."

                delay(800)

                gameEngine
                    .finishTurnIfNoMove()

                diceValue = 0

                message =
                    "Your turn."

                refresh++

                return@LaunchedEffect
            }

            delay(400)

            /*
             * AI chooses token
             */
            val token =
                ComputerPlayer.chooseToken(
                    engine = gameEngine,
                    difficulty = aiDifficulty
                )

            if (token == null) {

                gameEngine
                    .finishTurnIfNoMove()

                diceValue = 0

                refresh++

                return@LaunchedEffect
            }

            message =
                "🤖 Computer is moving..."

            delay(500)

            /*
             * Actual move
             */
            val result =
                gameEngine.moveToken(
                    token.id
                )

            message =
                result.message

            diceValue =
                gameEngine.state.diceValue

            refresh++
        }
    }

    /*
     * ------------------------------------------
     * UI
     * ------------------------------------------
     */

    Column(
        modifier = Modifier
            .padding(12.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "🎲 Offline Ludo",

            style =
                MaterialTheme.typography
                    .headlineMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )

        Text(
            text =
                when (gameMode) {

                    GameMode.LOCAL ->
                        "👥 Local Multiplayer"

                    GameMode.VS_COMPUTER ->
                        "🤖 vs Computer"
                }
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        PlayerTurnCard(
            player = currentPlayer,
            gameMode = gameMode,
            isComputer = computerTurn
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        /*
         * BOARD
         */
        LudoBoard(

            players =
                gameEngine.state.players,

            movableTokenIds =
                movableTokenIds,

            onTokenClick = { token ->

                /*
                 * Computer turn হলে
                 * human touch করতে পারবে না
                 */
                if (
                    computerTurn ||
                    isRolling ||
                    winner != null
                ) {
                    return@LudoBoard
                }

                /*
                 * Token move
                 */
                val result =
                    gameEngine.moveToken(
                        token.id
                    )

                message =
                    result.message

                diceValue =
                    gameEngine.state.diceValue

                refresh++
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        /*
         * DICE CARD
         */
        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Column(
                modifier =
                    Modifier.padding(10.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                DiceView(
                    value = diceValue,
                    rolling = isRolling
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text = message
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        /*
         * ROLL DICE
         */
        Button(

            enabled =
                !computerTurn &&
                        !isRolling &&
                        winner == null &&
                        !gameEngine.hasRolledDice(),

            onClick = {

                isRolling = true
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text = "🎲 Roll Dice"
            )
        }

        /*
         * Human dice roll
         */
        if (
            isRolling &&
            !computerTurn
        ) {

            LaunchedEffect(
                isRolling
            ) {

                delay(450)

                val diceResult =
                    gameEngine.rollDiceResult()

                diceValue =
                    diceResult.value

                isRolling = false

                refresh++

                /*
                 * Three consecutive six
                 */
                if (
                    diceResult.thirdSix
                ) {

                    message =
                        "🎲 Three 6s! Turn lost."

                    delay(800)

                    gameEngine.nextTurn()

                    diceValue = 0

                    message =
                        "Next player's turn."

                    refresh++

                    return@LaunchedEffect
                }

                /*
                 * No movable token
                 */
                if (
                    !gameEngine.hasMovableToken()
                ) {

                    message =
                        "No valid move."

                    delay(600)

                    gameEngine
                        .finishTurnIfNoMove()

                    diceValue = 0

                    message =
                        "Next player's turn."

                    refresh++

                } else {

                    message =
                        "Choose a highlighted token."
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        /*
         * BOTTOM BUTTONS
         */
        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            OutlinedButton(
                onClick = onNewGame,

                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = "🔄 New Game"
                )
            }

            OutlinedButton(
                onClick = onBack,

                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = "🚪 Exit"
                )
            }
        }
    }

    /*
     * WINNER
     */
    if (
        winner != null
    ) {

        WinnerDialog(

            winner = winner,

            onNewGame = {
                onNewGame()
            },

            onExit = {
                onBack()
            }
        )
    }

    /*
     * Prevent unused state warning
     */
    @Suppress("UNUSED_VARIABLE")
    val stateRefresh = refresh
}


/*
 * ------------------------------------------
 * PLAYER TURN CARD
 * ------------------------------------------
 */

@Composable
private fun PlayerTurnCard(
    player: Player,
    gameMode: GameMode,
    isComputer: Boolean
) {

    val title =
        when (gameMode) {

            GameMode.LOCAL ->
                "${player.color.name} PLAYER'S TURN"

            GameMode.VS_COMPUTER -> {

                if (isComputer) {
                    "🤖 COMPUTER'S TURN"
                } else {
                    "👤 YOUR TURN"
                }
            }
        }

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(10.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = title,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            Text(
                text =
                    "${player.color.name} • 4 Tokens"
            )
        }
    }
}