package com.rana.ludo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rana.ludo.ui.LudoColors

@Composable
fun HomeScreen(
    onNewGame: () -> Unit,
    onExit: () -> Unit
) {

    Box(
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
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 30.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            /*
             * Logo
             */
            Box(
                modifier = Modifier
                    .size(125.dp)
                    .shadow(
                        elevation = 18.dp,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .background(
                        color = LudoColors.Panel,
                        shape = RoundedCornerShape(32.dp)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "🎲",
                    fontSize = 72.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Text(
                text = "LUDO",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = LudoColors.Gold
            )

            Text(
                text = "CLASSIC",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = LudoColors.TextPrimary,
                letterSpacing = 5.sp
            )

            Spacer(
                modifier =
                    Modifier.height(45.dp)
            )

            /*
             * PLAY
             */
            Button(
                onClick = onNewGame,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            LudoColors.Green,
                        contentColor =
                            LudoColors.TextPrimary
                    ),

                elevation =
                    ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    )
            ) {

                Text(
                    text = "🎮  PLAY",
                    fontSize = 21.sp,
                    fontWeight =
                        FontWeight.Black
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            /*
             * EXIT
             */
            OutlinedButton(
                onClick = onExit,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    OutlinedButtonDefaults
                        .colors(
                            contentColor =
                                LudoColors.TextPrimary
                        )
            ) {

                Text(
                    text = "🚪  EXIT",
                    fontSize = 18.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(32.dp)
            )

            Text(
                text = "OFFLINE • NO INTERNET REQUIRED",
                fontSize = 12.sp,
                color = LudoColors.TextSecondary,
                letterSpacing = 1.sp
            )
        }
    }
}