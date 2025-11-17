package com.example.lab13animaciones


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp      // ESTE IMPORT FALTABA
import kotlinx.coroutines.delay

@Composable
fun EjercicioFinal() {
    var playerX by remember { mutableStateOf(0.dp) }
    var playerSize by remember { mutableStateOf(60.dp) }
    var score by remember { mutableIntStateOf(0) }  // MEJOR: mutableIntStateOf
    var enemyVisible by remember { mutableStateOf(false) }
    var gameOver by remember { mutableStateOf(false) }

    val animatedX by animateDpAsState(targetValue = playerX, spring(stiffness = 400f))
    val animatedSize by animateDpAsState(targetValue = playerSize, spring(stiffness = 500f))
    val enemyY by animateDpAsState(
        targetValue = if (enemyVisible) 600.dp else (-100).dp,
        animationSpec = tween(1200),
        label = "enemyFall"
    )

    LaunchedEffect(Unit) {
        while (!gameOver) {
            delay(1800)
            enemyVisible = true
            delay(1300)
            if (enemyVisible && kotlin.math.abs(animatedX.value - 150.dp.value) < 60) {
                score++  // Ahora funciona bien
                playerSize = 80.dp
                delay(150)
                playerSize = 60.dp
            } else if (enemyVisible) {
                gameOver = true
            }
            enemyVisible = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
    ) {
        // Jugador (azul claro)
        Box(
            modifier = Modifier
                .offset(x = animatedX, y = 500.dp)
                .size(animatedSize)
                .clip(CircleShape)
                .background(Color.Cyan)
                .clickable {
                    playerX = if (playerX == 0.dp) 300.dp else 0.dp
                }
        )

        // Enemigo (rojo)
        AnimatedVisibility(
            visible = enemyVisible,
            enter = slideInVertically { -it },
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .offset(x = 150.dp, y = enemyY)
                    .size(50.dp)
                    .background(Color.Red, CircleShape)
            )
        }

        // Puntaje
        Text(
            text = "Puntaje: $score",
            color = Color.White,
            fontSize = 24.sp,  // sp ahora funciona
            modifier = Modifier.align(Alignment.TopCenter).padding(24.dp)
        )

        // Game Over
        if (gameOver) {
            Text(
                text = "¡GAME OVER!",
                color = Color.Red,
                fontSize = 36.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}