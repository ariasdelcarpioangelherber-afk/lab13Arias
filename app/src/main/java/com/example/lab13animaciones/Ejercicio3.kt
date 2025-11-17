package com.example.lab13animaciones

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
@Composable
fun Ejercicio3() {
    var expanded by remember { mutableStateOf(false) }

    val size by animateDpAsState(
        targetValue = if (expanded) 200.dp else 100.dp,
        animationSpec = spring(stiffness = 300f),
        label = "size"
    )

    val offsetX by animateDpAsState(
        targetValue = if (expanded) 120.dp else 0.dp,
        animationSpec = spring(stiffness = 300f),
        label = "offset"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { expanded = !expanded }) {
            Text("Mover y Agrandar")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .size(size)
                .background(Color(0xFFFF5722))
        )
    }
}