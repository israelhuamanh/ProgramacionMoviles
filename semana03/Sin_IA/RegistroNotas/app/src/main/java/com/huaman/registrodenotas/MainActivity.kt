package com.huaman.registrodenotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huaman.registrodenotas.ui.theme.RegistroDeNotasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroDeNotasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistroNotasScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroNotasScreen() {
    val gradientColors = listOf(Color(0xFFEDE7F6), Color(0xFFFFFFFF))
    
    var notaFP by remember { mutableFloatStateOf(0f) }
    var notaPOO by remember { mutableFloatStateOf(0f) }
    var notaPM by remember { mutableFloatStateOf(0f) }
    var notaBD by remember { mutableFloatStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Notas", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF673AB7)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Notas del ciclo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Desliza para asignar cada nota (0 a 20)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            CursoRow("Fundamentos de Programación", "20%", notaFP) { notaFP = it }
            CursoRow("Programación Orientada a Objetos", "25%", notaPOO) { notaPOO = it }
            CursoRow("Programación en Móviles", "30%", notaPM) { notaPM = it }
            CursoRow("Base de Datos", "25%", notaBD) { notaBD = it }
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Redondear promedio final", fontSize = 14.sp)
                Switch(
                    checked = redondear,
                    onCheckedChange = { redondear = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF673AB7),
                        checkedTrackColor = Color(0xFFD1C4E9)
                    )
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = confirmado,
                    onCheckedChange = { confirmado = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF673AB7)
                    )
                )
                Text(text = "Confirmo que las notas son correctas", fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { /* Pendiente calcular */ },
                enabled = confirmado,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF673AB7),
                    disabledContainerColor = Color(0xFFB0BEC5)
                )
            ) {
                Text(text = "CALCULAR PROMEDIO")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Asigna las notas y confirma para calcular",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CursoRow(nombre: String, peso: String, nota: Float, onNotaChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "($peso)", fontSize = 12.sp, color = Color.Gray)
            }
            Badge(containerColor = Color(0xFFEDE7F6), contentColor = Color(0xFF673AB7)) {
                Text(text = nota.toInt().toString(), modifier = Modifier.padding(4.dp))
            }
        }
        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF673AB7),
                activeTrackColor = Color(0xFF673AB7)
            )
        )
    }
}