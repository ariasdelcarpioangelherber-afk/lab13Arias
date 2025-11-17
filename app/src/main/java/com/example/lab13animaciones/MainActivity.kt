package com.example.lab13animaciones


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F5F5)
                ) {
                    Lab13App()
                }
            }
        }
    }
}

@Composable
fun Lab13App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("main") {
            MainScreen(navController = navController)
        }
        composable("game") {
            EjercicioFinal()  // Pantalla completa del juego
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Título general
        Text(
            text = "Laboratorio 13: Animaciones",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        HorizontalDivider()

        // Ejercicio 1
        ExerciseSection(title = "Ejercicio 1: AnimatedVisibility") {
            Ejercicio1()
        }

        // Ejercicio 2
        ExerciseSection(title = "Ejercicio 2: animateColorAsState") {
            Ejercicio2()
        }

        // Ejercicio 3
        ExerciseSection(title = "Ejercicio 3: Tamaño y Posición") {
            Ejercicio3()
        }

        // Ejercicio 4
        ExerciseSection(title = "Ejercicio 4: AnimatedContent") {
            Ejercicio4()
        }

        // BOTÓN IR AL JUEGO
        Button(
            onClick = { navController.navigate("game") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(64.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text(
                text = "IR AL JUEGO",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun ExerciseSection(
    title: String,
    isFullWidth: Boolean = false,
    content: @Composable () -> Unit
) {
    Column(
        modifier = if (isFullWidth) Modifier.fillMaxWidth() else Modifier.fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6200EE),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(min = 200.dp)
            ) {
                content()
            }
        }
    }
}