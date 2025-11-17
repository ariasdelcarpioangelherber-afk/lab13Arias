package com.example.lab13animaciones

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape // <-- IMPORTACIÓN AÑADIDA
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.math.abs // Importado para usar abs sin prefijo kotlin.math

// ENEMIGO (FUERA DE COMPOSABLE)
data class Enemy(var x: Float, var y: Float, val id: Int = Random.nextInt())

@Composable
fun EjercicioFinal() {
    // ESTADOS
    var shipX by remember { mutableStateOf(180.dp) }
    var score by remember { mutableIntStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    val enemies = remember { mutableStateListOf<Enemy>() }
    var leftPressed by remember { mutableStateOf(false) }
    var rightPressed by remember { mutableStateOf(false) }

    // ESTADO DE LA BALA (Ahora usamos una posición (x, y) en lugar de solo un booleano)
    // Pair<Float, Float>? -> (Posición X de disparo, Posición Y actual)
    var bulletPosition by remember { mutableStateOf<Pair<Float, Float>?>(null) }

    // ANIMACIONES
    val animatedShipX by animateDpAsState(
        targetValue = shipX,
        animationSpec = spring(stiffness = 1200f, dampingRatio = 0.7f)
    )

    // La animación de la bala sigue la posición Y de nuestro estado 'bulletPosition'
    val animatedBulletY by animateDpAsState(
        targetValue = if (bulletPosition != null) bulletPosition!!.second.dp else 600.dp,
        animationSpec = tween(700, easing = FastOutSlowInEasing)
    )

    //---------------------------------------------------------------------------------
    // GAME LOOP PRINCIPAL (Consolida movimiento de Nave, Enemigos y Puntuación)
    //---------------------------------------------------------------------------------
    LaunchedEffect(gameOver) {
        while (!gameOver) {
            val frameTime = 16L // Aproximadamente 60 FPS

            // 1. MOVIMIENTO DE NAVE (Se ejecuta 60 veces por segundo)
            val shipSpeed = 14f
            if (leftPressed) shipX = (shipX.value - shipSpeed).coerceAtLeast(30f).dp
            if (rightPressed) shipX = (shipX.value + shipSpeed).coerceAtMost(330f).dp

            // 2. MOVER ENEMIGOS + COLISIÓN CON NAVE
            val enemySpeed = 3.2f
            val toRemove = mutableListOf<Enemy>()
            enemies.forEach { enemy ->
                enemy.y += enemySpeed

                // Si el enemigo se sale de la pantalla
                if (enemy.y > 750f) toRemove.add(enemy)

                // Colisión con la Nave
                if (abs(shipX.value - enemy.x) < 58f && enemy.y > 480f) {
                    gameOver = true
                }
            }
            enemies.removeAll(toRemove)

            // 3. PUNTAJE
            score++

            delay(frameTime)
        }
    }

    //---------------------------------------------------------------------------------
    // LÓGICA DE BALA: MOVIMIENTO + COLISIÓN CON ENEMIGOS
    //---------------------------------------------------------------------------------
    LaunchedEffect(bulletPosition, enemies) {
        // Solo si hay una bala activa
        if (bulletPosition != null) {
            var (startX, currentY) = bulletPosition!!
            val bulletSpeed = 20f

            // Mientras la bala esté visible y activa
            while (currentY > -100f && bulletPosition != null) {
                currentY -= bulletSpeed
                bulletPosition = Pair(startX, currentY)

                // --- DETECCIÓN DE COLISIÓN ---
                val hitEnemy = enemies.find { enemy ->
                    // Colisión: Ambos centros deben estar dentro de un rango
                    val dx = abs(startX - enemy.x)
                    val dy = abs(currentY - enemy.y)

                    // Ajustamos el umbral para las dimensiones (nave: 86, enemigo: 54, bala: 18)
                    dx < 40f && dy < 40f
                }

                if (hitEnemy != null) {
                    enemies.remove(hitEnemy)
                    score += 100 // Puntos por destruir enemigo
                    bulletPosition = null // Destruir bala
                    break
                }
                // ------------------------------

                delay(16) // Mueve la bala 60 veces por segundo
            }
            // Si la bala sale de la pantalla sin chocar
            if (currentY <= -100f) {
                bulletPosition = null
            }
        }
    }

    //---------------------------------------------------------------------------------
    // SPAWN ENEMIGOS (Dificultad Progresiva)
    //---------------------------------------------------------------------------------
    LaunchedEffect(gameOver) {
        var spawnDelay = 2800L
        val minDelay = 500L
        val difficultyStep = 50L

        while (!gameOver) {
            delay(spawnDelay)

            // El juego se hace más difícil reduciendo el tiempo de aparición
            if (spawnDelay > minDelay) {
                spawnDelay -= difficultyStep
            }

            val x = Random.nextFloat() * 280f + 40f
            enemies.add(Enemy(x, -100f))
        }
    }

    //---------------------------------------------------------------------------------
    // INTERFAZ DE USUARIO (UI)
    //---------------------------------------------------------------------------------
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A))) {
        // ESTRELLAS DE FONDO (EFECTO VISUAL)
        repeat(30) {
            val x = remember { Random.nextFloat() * 360f }
            val y = remember { Random.nextFloat() * 800f }
            val size = remember { Random.nextFloat() * 3f + 1f }
            Box(
                modifier = Modifier
                    .offset(x.dp, y.dp)
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
            )
        }

        // ENEMIGOS (CON SOMBRA)
        enemies.forEach { enemy ->
            Box(
                modifier = Modifier
                    .offset(enemy.x.dp, enemy.y.dp)
                    .size(54.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFFFF3B30))
            )
        }

        // BALA (BRILLO AZUL)
        if (bulletPosition != null || animatedBulletY.value < 599f) {
            Box(
                modifier = Modifier
                    .offset(animatedShipX, animatedBulletY)
                    .size(18.dp)
                    .shadow(12.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFF00D9FF))
            )
        }

        // NAVE (CON SOMBRA Y DETALLE)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = animatedShipX - 180.dp, y = (-100).dp) // Usar offset relativo
                .size(86.dp)
                .shadow(12.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFF00FF88))
        ) {
            // DETALLE NAVE
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 12.dp)
                    .size(20.dp, 30.dp)
                    .background(Color(0xFF00CC66), CircleShape)
            )
        }

        // ZONA IZQUIERDA (CONTROL)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxHeight(0.32f)
                .fillMaxWidth(0.42f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            leftPressed = true
                            tryAwaitRelease()
                            leftPressed = false
                        }
                    )
                }
        )

        // ZONA DERECHA (CONTROL)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxHeight(0.32f)
                .fillMaxWidth(0.42f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            rightPressed = true
                            tryAwaitRelease()
                            rightPressed = false
                        }
                    )
                }
        )

        // BOTÓN DISPARAR
        Button(
            onClick = {
                // Permite disparar solo si no hay una bala activa (enfriamiento implícito)
                if (!gameOver && bulletPosition == null) {
                    bulletPosition = Pair(shipX.value, 500f)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 130.dp, y = (-48).dp)
                .shadow(6.dp, RoundedCornerShape(12.dp))
        ) {
            Text("DISPARAR", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        // PUNTAJE
        Text(
            text = "SCORE: $score",
            color = Color(0xFF00FFAA),
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(28.dp)
                .shadow(8.dp)
        )

        // GAME OVER (CON ANIMACIÓN)
        if (gameOver) {
            AnimatedVisibility(
                visible = gameOver,
                enter = scaleIn(spring(stiffness = 600f)) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                        .background(Color.Black.copy(alpha = 0.8f))
                        .fillMaxSize()
                ) {
                    Spacer(Modifier.height(200.dp)) // Espacio para centrar visualmente
                    Text(
                        "GAME OVER",
                        color = Color(0xFFFF3B30),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.shadow(12.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                    Text("Puntaje Final: $score", color = Color.White, fontSize = 28.sp)
                    Spacer(Modifier.height(36.dp))
                    Button(
                        onClick = {
                            // Reiniciar todos los estados para empezar de nuevo
                            score = 0
                            gameOver = false
                            enemies.clear()
                            shipX = 180.dp
                            bulletPosition = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("REINTENTAR", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}