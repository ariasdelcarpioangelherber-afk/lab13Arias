package com.example.lab13animaciones


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp      // ESTE IMPORT FALTABA
import androidx.compose.ui.unit.sp

// SEALED CLASS: debe estar al inicio del archivo (top-level)
sealed class AppState {
    object Loading : AppState()
    object Content : AppState()
    object Error : AppState()
}

@Composable
fun Ejercicio4() {
    var state by remember { mutableStateOf<AppState>(AppState.Loading) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),  // dp ahora funciona
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "stateTransition"
        ) { currentState ->
            when (currentState) {
                is AppState.Loading -> Text("Cargando datos...", fontSize = 28.sp)
                is AppState.Content -> Text("¡Todo listo!", fontSize = 28.sp)
                is AppState.Error -> Text("Error de conexión", fontSize = 28.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { state = AppState.Loading }) {
                Text("Cargando")
            }
            Button(onClick = { state = AppState.Content }) {
                Text("Contenido")
            }
            Button(onClick = { state = AppState.Error }) {
                Text("Error")
            }
        }
    }
}