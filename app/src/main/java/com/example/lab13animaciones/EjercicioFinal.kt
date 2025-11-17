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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch  // ESTE IMPORT FALTABA

@Composable
fun EjercicioFinal() {
    // Estado del juego
    var shipX by remember { mutableStateOf(150.dp) }
    var score by remember { mutableIntStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var enemyY by remember { mutableStateOf(-100.dp) }
    var bulletY by remember { mutableStateOf(600.dp) }
    var canShoot by remember { mutableStateOf(true) }

    // Animaciones
    val animatedShipX by animateDpAsState(targetValue = shipX, spring(stiffness = 400f))
    val animatedEnemyY by animateDpAsState(targetValue = enemyY, tween(2000))
    val animatedBulletY by animateDpAsState(targetValue = bulletY, tween(800))

    // Generar enemigo
    LaunchedEffect(Unit) {
        while (!gameOver) {
            delay(2000)
            enemyY = -100.dp
            delay(2000)
            if (!gameOver) gameOver = true
        }
    }

    // Scope para launch
    val scope = rememberCoroutineScope()  // CORRECTO

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A23))
    ) {
        // Enemigo
        AnimatedVisibility(visible = enemyY > -100.dp, enter = slideInVertically { -it }, exit = fadeOut()) {
            Box(
                modifier = Modifier
                    .offset(x = 150.dp, y = animatedEnemyY)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
        }

        // Bala
        if (!canShoot && bulletY < 600.dp) {
            Box(
                modifier = Modifier
                    .offset(x = animatedShipX, y = animatedBulletY)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Cyan)
            )
        }

        // Nave
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-80).dp)
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFF00FF88))
                .clickable {
                    if (!gameOver && canShoot) {
                        shipX = if (shipX == 100.dp) 200.dp else 100.dp
                        bulletY = 600.dp
                        canShoot = false

                        // CORRECTO: usar scope.launch
                        scope.launch {
                            delay(1000)
                            canShoot = true
                            bulletY = 600.dp
                        }

                        // Colisión
                        if (kotlin.math.abs(animatedEnemyY.value - 300f) < 100f) {
                            score++
                            enemyY = -100.dp
                        }
                    }
                }
        )

        // Puntaje
        Text(
            text = "Puntaje: $score",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.TopCenter).padding(24.dp)
        )

        // Game Over
        AnimatedVisibility(visible = gameOver, enter = fadeIn() + scaleIn(), exit = fadeOut()) {
            Text(
                text = "¡GAME OVER!",
                color = Color.Red,
                fontSize = 40.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}