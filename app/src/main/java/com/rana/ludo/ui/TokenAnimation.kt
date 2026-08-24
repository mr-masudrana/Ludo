package com.rana.ludo.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun rememberTokenAnimation(
    position: Int
): Float {

    val progress =
        remember {
            Animatable(1f)
        }

    LaunchedEffect(position) {

        progress.animateTo(
            targetValue = 1f,
            animationSpec =
                tween(
                    durationMillis = 120
                )
        )
    }

    return progress.value
}