package com.rana.ludo.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DiceView(
    value: Int,
    rolling: Boolean,
    modifier: Modifier = Modifier
) {

    val rotation =
        animateFloatAsState(
            targetValue =
                if (rolling) {
                    720f
                } else {
                    0f
                },

            animationSpec =
                tween(
                    durationMillis = 500
                ),

            label = "dice_rotation"
        )

    Box(
        modifier = modifier
            .size(72.dp)
            .rotate(rotation.value),

        contentAlignment =
            Alignment.Center
    ) {

        Text(
            text = diceFace(value),
            fontSize = 48.sp
        )
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