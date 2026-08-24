package com.rana.ludo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rana.ludo.model.AiDifficulty
import com.rana.ludo.model.GameMode
import com.rana.ludo.ui.LudoColors

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
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        LudoColors.BackgroundDark,
                        LudoColors.Background,
                        LudoColors.BackgroundDark
                    )
                )
            )
            .padding(
                horizontal = 18.dp,
                vertical = 16.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        /*
         * HEADER
         */

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Button(
                onClick = onBack,
                modifier =
                    Modifier.size(48.dp),

                shape =
                    RoundedCornerShape(16.dp),

                contentPadding =
                    androidx.compose.foundation.layout
                        .PaddingValues(0.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            LudoColors.Panel
                    )
            ) {

                Text(
                    text = "←",
                    fontSize = 25.sp
                )
            }

            Text(
                text = "LUDO",
                modifier =
                    Modifier.weight(1f),

                textAlign =
                    androidx.compose.ui.text.style
                        .TextAlign.Center,

                fontSize = 28.sp,

                fontWeight =
                    FontWeight.Black,

                color =
                    LudoColors.Gold
            )

            Box(
                modifier =
                    Modifier.size(48.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        SectionTitle(
            text = "SELECT MODE"
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            ModeCard(
                modifier =
                    Modifier.weight(1f),

                title = "VS COMPUTER",
                icon = "🤖",

                selected =
                    selectedMode ==
                            GameMode.VS_COMPUTER,

                onClick = {
                    onModeSelected(
                        GameMode.VS_COMPUTER
                    )
                }
            )

            ModeCard(
                modifier =
                    Modifier.weight(1f),

                title = "LOCAL PLAYER",
                icon = "👥",

                selected =
                    selectedMode ==
                            GameMode.LOCAL,

                onClick = {
                    onModeSelected(
                        GameMode.LOCAL
                    )
                }
            )
        }

        Spacer(
            modifier =
                Modifier.height(22.dp)
        )

        SectionTitle(
            text = "SELECT PLAYERS"
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            listOf(2, 3, 4).forEach { count ->

                SelectionBox(
                    modifier =
                        Modifier.weight(1f),

                    text =
                        count.toString(),

                    selected =
                        selectedPlayers == count,

                    onClick = {
                        onPlayersSelected(count)
                    }
                )
            }
        }

        if (
            selectedMode ==
            GameMode.VS_COMPUTER
        ) {

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            SectionTitle(
                text = "AI DIFFICULTY"
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                AiDifficulty.values()
                    .forEach { difficulty ->

                        SelectionBox(
                            modifier =
                                Modifier.weight(1f),

                            text =
                                difficulty.name
                                    .lowercase()
                                    .replaceFirstChar {
                                        it.uppercase()
                                    },

                            selected =
                                selectedDifficulty ==
                                        difficulty,

                            onClick = {
                                onDifficultySelected(
                                    difficulty
                                )
                            }
                        )
                    }
            }
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        SectionTitle(
            text = "SELECT PAWN"
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceEvenly
        ) {

            PawnPreview(
                color = LudoColors.Red,
                emoji = "🔴"
            )

            PawnPreview(
                color = LudoColors.Green,
                emoji = "🟢"
            )

            PawnPreview(
                color = LudoColors.Yellow,
                emoji = "🟡"
            )

            PawnPreview(
                color = LudoColors.Blue,
                emoji = "🔵"
            )
        }

        Spacer(
            modifier =
                Modifier.weight(1f)
        )

        Button(
            onClick = onStartGame,

            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),

            shape =
                RoundedCornerShape(20.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        LudoColors.Green
                )
        ) {

            Text(
                text = "🎮  PLAY",
                fontSize = 23.sp,
                fontWeight =
                    FontWeight.Black
            )
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )
    }
}

@Composable
private fun SectionTitle(
    text: String
) {

    Text(
        text = text,

        fontSize = 18.sp,

        fontWeight =
            FontWeight.Black,

        color =
            LudoColors.Gold
    )
}

@Composable
private fun ModeCard(
    modifier: Modifier,
    title: String,
    icon: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,

        modifier = modifier
            .height(145.dp)
            .shadow(
                if (selected) 10.dp else 4.dp,
                RoundedCornerShape(20.dp)
            )
            .border(
                width =
                    if (selected) 3.dp else 1.dp,

                color =
                    if (selected)
                        LudoColors.Gold
                    else
                        LudoColors.GoldDark,

                shape =
                    RoundedCornerShape(20.dp)
            ),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    if (selected)
                        LudoColors.PanelLight
                    else
                        LudoColors.Panel
            )
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                text = icon,
                fontSize = 48.sp
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.Black,
                color =
                    LudoColors.TextPrimary
            )

            if (selected) {

                Text(
                    text = "✓",
                    color =
                        LudoColors.Gold,
                    fontSize = 20.sp,
                    fontWeight =
                        FontWeight.Black
                )
            }
        }
    }
}

@Composable
private fun SelectionBox(
    modifier: Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,

        modifier = modifier
            .height(64.dp)
            .border(
                width =
                    if (selected) 3.dp else 1.dp,

                color =
                    if (selected)
                        LudoColors.Gold
                    else
                        LudoColors.GoldDark,

                shape =
                    RoundedCornerShape(16.dp)
            ),

        shape =
            RoundedCornerShape(16.dp),

        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    if (selected)
                        LudoColors.Red
                    else
                        LudoColors.Panel
            )
    ) {

        Text(
            text = text,

            fontSize =
                if (text.length > 5)
                    13.sp
                else
                    25.sp,

            fontWeight =
                FontWeight.Black,

            color =
                LudoColors.TextPrimary
        )
    }
}

@Composable
private fun PawnPreview(
    color: Color,
    emoji: String
) {

    Box(
        modifier = Modifier
            .size(70.dp)
            .shadow(
                6.dp,
                RoundedCornerShape(16.dp)
            )
            .background(
                color = color.copy(
                    alpha = 0.25f
                ),
                shape =
                    RoundedCornerShape(16.dp)
            )
            .border(
                2.dp,
                color,
                RoundedCornerShape(16.dp)
            ),

        contentAlignment =
            Alignment.Center
    ) {

        Text(
            text = emoji,
            fontSize = 38.sp
        )
    }
}