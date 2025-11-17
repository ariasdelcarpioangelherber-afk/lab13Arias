package com.example.lab13animaciones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab13animaciones.ui.theme.Lab13AnimacionesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab13AnimacionesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Laboratorio 13: Animaciones",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // --- Ejercicio 1 ---
        item {
            Text(
                text = "Ejercicio 1: AnimatedVisibility",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Exercise1()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- Ejercicio 2 ---
        item {
            Text(
                text = "Ejercicio 2: animateColorAsState",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Exercise2()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- Ejercicio 3 (Nuevo) ---
        item {
            Text(
                text = "Ejercicio 3: Animación de Tamaño y Posición",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Exercise3()
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- Composable para el Ejercicio 1 ---
@Composable
fun Exercise1() {
    var visible by remember { mutableStateOf(true) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Ocultar" else "Mostrar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Red)
            )
        }
    }
}

// --- Composable para el Ejercicio 2 ---
@Composable
fun Exercise2() {
    var useBlue by remember { mutableStateOf(true) }
    val color by animateColorAsState(
        targetValue = if (useBlue) Color.Blue else Color.Green,
        label = "colorAnimation",
        animationSpec = spring(stiffness = spring.StiffnessLow)
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { useBlue = !useBlue }) {
            Text("Cambiar Color")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color)
        )
    }
}

// --- Composable para el Ejercicio 3 ---
@Composable
fun Exercise3() {
    // Estado para alternar entre movido/redimensionado
    var moved by remember { mutableStateOf(false) }

    // Animamos valores Dp para el tamaño
    val size by animateDpAsState(
        targetValue = if (moved) 150.dp else 100.dp,
        label = "sizeAnimation",
        animationSpec = spring()
    )

    // Animamos valores Dp para el offset (posición)
    val offsetX by animateDpAsState(
        targetValue = if (moved) 100.dp else 0.dp,
        label = "offsetXAnimation",
        animationSpec = spring()
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { moved = !moved }) {
            Text("Mover y Redimensionar")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .size(size)
                .background(Color.Cyan)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lab13AnimacionesTheme {
        MainScreen()
    }
}