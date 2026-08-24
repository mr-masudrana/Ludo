package com.rana.ludo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
                    colors = listOf(
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
                    vertical = 24.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            /*
             * Ludo Logo
             */
            Box(
                modifier = Modifier
                    .size(125.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .background(
                        color = LudoColors.Panel,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = LudoColors.GoldDark,
                        shape = RoundedCornerShape(32.dp)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "🎲",
                    fontSize = 70.sp
                )
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            /*
             * Title
             */
            Text(
                text = "LUDO",
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                color = LudoColors.Gold
            )

            Text(
                text = "CLASSIC",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = LudoColors.TextPrimary,
                letterSpacing = 5.sp
            )

            Spacer(
                modifier = Modifier.height(42.dp)
            )

            /*
             * PLAY BUTTON
             */
            Button(
                onClick = onNewGame,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),

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
                        defaultElevation = 8.dp,
                        pressedElevation = 2.dp
                    )
            ) {

                Text(
                    text = "🎮  PLAY",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            /*
             * EXIT BUTTON
             *
             * OutlinedButton ব্যবহার করছি না।
             * তাই OutlinedButtonDefaults-এর কোনো
             * dependency/import দরকার নেই।
             */
            Button(
                onClick = onExit,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .border(
                        width = 2.dp,
                        color = LudoColors.GoldDark,
                        shape = RoundedCornerShape(20.dp)
                    ),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            LudoColors.Panel,

                        contentColor =
                            LudoColors.TextPrimary
                    ),

                elevation =
                    ButtonDefaults.buttonElevation(
                        defaultElevation = 3.dp,
                        pressedElevation = 1.dp
                    )
            ) {

                Text(
                    text = "🚪  EXIT",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            /*
             * Footer
             */
            Text(
                text = "OFFLINE • NO INTERNET REQUIRED",
                fontSize = 12.sp,
                color = LudoColors.TextSecondary,
                letterSpacing = 1.sp
            )
        }
    }
}