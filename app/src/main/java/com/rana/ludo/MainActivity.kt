package com.rana.ludo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MaterialTheme {

                var showGame by mutableStateOf(false)

                if (showGame) {

                    GameScreen(
                        onBack = {
                            showGame = false
                        }
                    )

                } else {

                    HomeScreen(
                        onNewGame = {
                            showGame = true
                        }
                    )
                }
            }
        }
    }
}
