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
import com.rana.ludo.model.PlayerColor
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

    /*
     * GameEngine শুধুমাত্র game শুরু হওয়ার সময়
     * তৈরি হবে।
     */
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

    /*
     * UI refresh trigger
     */
    var refresh by remember {
        mutableIntStateOf(0)
    }

    /*
     * Dice
     */
    var diceValue by remember {
        mutableIntStateOf(0)
    }

    var isRolling by remember {
        mutableStateOf(false)
    }

    /*
     * Token animation
     */
    var isAnimating by remember {
        mutableStateOf(false)
    }

    /*
     * Message
     */
    var message by remember {
        mutableStateOf(
            "Roll the dice to start."
        )
    }

    /*
     * Current player
     */
    val currentPlayer =
        gameEngine.currentPlayer()

    val currentPlayerIndex =
        gameEngine
            .state
            .currentPlayerIndex

    /*
     * Movable tokens
     */
    val movableTokenIds =
        if (
            !isAnimating &&
            gameEngine.hasRolledDice()
        ) {

            gameEngine
                .getMovableTokenIds()

        } else {

            emptySet()
        }

    /*
     * Winner
     */
    val winner =
        gameEngine.state.winner

    /*
     * Computer turn?
     */
    val computerTurn =
        gameMode ==
                GameMode.VS_COMPUTER &&
                currentPlayer.isComputer

    /*
     * ------------------------------------------------
     * COMPUTER TURN
     * ------------------------------------------------
     */

    LaunchedEffect(
        currentPlayerIndex,
        gameMode,
        winner,
        isAnimating
    ) {

        if (
            computerTurn &&
            winner == null &&
            !isAnimating
        ) {

            /*
             * একটু delay,
             * যেন AI হঠাৎ action না করে।
             */
            delay(700)

            /*
             * Dice animation শুরু
             */
            isRolling = true

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
                    "🤖 Computer rolled three 6s."

                delay(800)

                gameEngine.nextTurn()

                diceValue = 0

                message =
                    "Your turn."

                refresh++

                return@LaunchedEffect
            }

            delay(500)

            /*
             * কোনো valid token নেই
             */
            if (
                !gameEngine.hasMovableToken()
            ) {

                message =
                    "🤖 No valid move."

                delay(700)

                gameEngine
                    .finishTurnIfNoMove()

                diceValue = 0

                message =
                    "Your turn."

                refresh++

                return@LaunchedEffect
            }

            /*
             * AI token নির্বাচন
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

            delay(400)

            /*
             * Token movement animation
             */
            animateTokenMove(
                engine = gameEngine,
                token = token,
                onStart = {
                    isAnimating = true
                },
                onEnd = {
                    isAnimating = false
                }
            )

            /*
             * আসল movement
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

            /*
             * Extra turn হলে আবার AI turn হবে।
             *
             * সাধারণ move হলে GameEngine
             * next player করে দিয়েছে।
             */
        }
    }

    /*
     * ------------------------------------------------
     * UI
     * ------------------------------------------------
     */

    Column(
        modifier = Modifier
            .padding(12.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        /*
         * Header
         */
        Text(
            text = "🎲 Offline Ludo",
            style =
                MaterialTheme.typography.headlineMedium,
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
                },

            style =
                MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        /*
         * Current player
         */
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
         * Board
         */
        LudoBoard(
            players =
                gameEngine.state.players,

            movableTokenIds =
                movableTokenIds,

            onTokenClick = { token ->

                /*
                 * Computer বা animation চললে
                 * player token touch করতে পারবে না।
                 */
                if (
                    computerTurn ||
                    isAnimating ||
                    winner != null
                ) {
                    return@LudoBoard
                }

                /*
                 * Token move
                 */
                performHumanMove(
                    engine = gameEngine,
                    token = token,

                    onStart = {
                        isAnimating = true
                    },

                    onEnd = {
                        isAnimating = false
                    },

                    onMessage = {
                        message = it
                    }
                )

                diceValue =
                    gameEngine.state.diceValue

                refresh++
            },

            modifier =
                Modifier
                    .fillMaxWidth()
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        /*
         * Dice
         */
        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Column(
                modifier =
                    Modifier.padding(8.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                DiceView(
                    value = diceValue,
                    rolling = isRolling
                )

                Text(
                    text = message,
                    style =
                        MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        /*
         * Roll button
         */
        Button(
            enabled =
                !computerTurn &&
                        !isRolling &&
                        !isAnimating &&
                        winner == null &&
                        !gameEngine.hasRolledDice(),

            onClick = {

                isRolling = true

                /*
                 * Dice animation-এর জন্য
                 * UI thread block না করে
                 * coroutine ব্যবহার করা হবে।
                 */
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text = "🎲 Roll Dice"
            )
        }

        /*
         * Roll animation handling
         */
        if (
            isRolling &&
            !computerTurn
        ) {

            LaunchedEffect(
                isRolling
            ) {

                delay(450)

                val result =
                    gameEngine.rollDiceResult()

                diceValue =
                    result.value

                isRolling = false

                refresh++

                if (
                    result.thirdSix
                ) {

                    message =
                        "🎲 Three 6s! Turn lost."

                    delay(700)

                    gameEngine.nextTurn()

                    diceValue = 0

                    message =
                        "Next player's turn."

                    refresh++

                } else if (
                    !gameEngine.hasMovableToken()
                ) {

                    message =
                        "No valid move."

                    delay(500)

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
         * Bottom buttons
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
     * Winner Dialog
     */
    if (winner != null) {

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
     * refresh variable ব্যবহার করে
     * Compose-কে game state পরিবর্তন
     * সম্পর্কে জানানো হচ্ছে।
     */
    @Suppress("UNUSED_VARIABLE")
    val ignored = refresh
}

/*
 * ----------------------------------------------------
 * HUMAN TOKEN MOVE
 * ----------------------------------------------------
 */

private suspend fun performHumanMove(
    engine: GameEngine,
    token: Token,
    onStart: () -> Unit,
    onEnd: () -> Unit,
    onMessage: (String) -> Unit
) {

    onStart()

    animateTokenMove(
        engine = engine,
        token = token,
        onStart = {},
        onEnd = {}
    )

    val result =
        engine.moveToken(
            token.id
        )

    onMessage(
        result.message
    )

    onEnd()
}

/*
 * ----------------------------------------------------
 * TOKEN ANIMATION
 * ----------------------------------------------------
 */

private suspend fun animateTokenMove(
    engine: GameEngine,
    token: Token,
    onStart: () -> Unit,
    onEnd: () -> Unit
) {

    onStart()

    val path =
        engine.getMovePath(
            token.id
        )

    /*
     * প্রতিটি cell-এ ছোট delay।
     *
     * গুরুত্বপূর্ণ:
     * GameEngine-এর actual position
     * animation-এর সময় পরিবর্তন করছি না।
     *
     * এটি শুধু timing layer।
     */
    for (
        position in path
    ) {

        delay(120)
    }

    onEnd()
}

/*
 * ----------------------------------------------------
 * PLAYER TURN CARD
 * ----------------------------------------------------
 */

@Composable
private fun PlayerTurnCard(
    player: Player,
    gameMode: GameMode,
    isComputer: Boolean
) {

    val text =
        if (
            gameMode ==
            GameMode.VS_COMPUTER
        ) {

            if (isComputer) {

                "🤖 COMPUTER'S TURN"

            } else {

                "👤 YOUR TURN"
            }

        } else {

            "${player.color.name} PLAYER'S TURN"
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
                text = text,
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