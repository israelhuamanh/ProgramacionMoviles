package com.huaman.registrodenotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huaman.registrodenotas.model.RegistroAcademico
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
    
    // Instancia del modelo POO
    val registroAcademico = remember { RegistroAcademico() }
    
    var notaFP by remember { mutableFloatStateOf(0f) }
    var notaPOO by remember { mutableFloatStateOf(0f) }
    var notaPM by remember { mutableFloatStateOf(0f) }
    var notaBD by remember { mutableFloatStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }

    var mostrarResultados by remember { mutableStateOf(false) }
    var promedioFinal by remember { mutableStateOf<String?>(null) }
    var promedioPonderadoTexto by remember { mutableStateOf<String?>(null) }
    var observacion by remember { mutableStateOf("") }
    var colorChip by remember { mutableStateOf(Color.Gray) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Notas (POO)", color = Color.White) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
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
                    }
                    
                    TextButton(onClick = {
                        notaFP = 0f
                        notaPOO = 0f
                        notaPM = 0f
                        notaBD = 0f
                        redondear = false
                        confirmado = false
                        mostrarResultados = false
                        // Reset en el modelo
                        registroAcademico.registrarNota(0, 0.0)
                        registroAcademico.registrarNota(1, 0.0)
                        registroAcademico.registrarNota(2, 0.0)
                        registroAcademico.registrarNota(3, 0.0)
                    }) {
                        Text("Limpiar", color = Color(0xFFD32F2F))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                CursoRow("Fundamentos de Programación", "20%", notaFP) { 
                    notaFP = it; mostrarResultados = false 
                    registroAcademico.registrarNota(0, it.toDouble())
                }
                CursoRow("Programación Orientada a Objetos", "25%", notaPOO) { 
                    notaPOO = it; mostrarResultados = false 
                    registroAcademico.registrarNota(1, it.toDouble())
                }
                CursoRow("Programación en Móviles", "30%", notaPM) { 
                    notaPM = it; mostrarResultados = false 
                    registroAcademico.registrarNota(2, it.toDouble())
                }
                CursoRow("Base de Datos", "25%", notaBD) { 
                    notaBD = it; mostrarResultados = false 
                    registroAcademico.registrarNota(3, it.toDouble())
                }
                
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
                        onCheckedChange = { redondear = it; mostrarResultados = false },
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
                    onClick = {
                        // Delegando la logica a la clase POO RegistroAcademico
                        val ponderado = registroAcademico.calcularPromedioPonderado()
                        promedioPonderadoTexto = String.format("%.2f", ponderado).replace(",", ".")
                        
                        val pFinal = registroAcademico.calcularPromedioFinal(redondear)
                        promedioFinal = if (redondear) "${pFinal.toInt()}" else String.format("%.2f", pFinal).replace(",", ".")
                        
                        val obs = registroAcademico.obtenerObservacion(pFinal)
                        observacion = obs.mensaje
                        colorChip = Color(obs.colorHex)
                        
                        mostrarResultados = true
                    },
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
                
                if (mostrarResultados) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Reto opcional: Aporte por curso usando POO
                            Text("Aportes por curso:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                            registroAcademico.cursos.forEach { curso ->
                                val aporteStr = String.format("%.2f", curso.calcularAporte()).replace(",", ".")
                                val pesoStr = "${(curso.peso * 100).toInt()}%"
                                Text("${curso.nombre.take(3)}: ${curso.nota.toInt()} × $pesoStr = $aporteStr", fontSize = 12.sp)
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = Color.LightGray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = "Promedio ponderado: $promedioPonderadoTexto", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(text = "Promedio final: ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF673AB7))
                                Text(text = promedioFinal ?: "", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF673AB7))
                            }
                            if (redondear) {
                                Text(text = "(redondeado)", fontSize = 12.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = colorChip.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = observacion,
                                    color = colorChip,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "✓ Promedio calculado correctamente",
                                color = Color(0xFF2E7D32),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Asigna las notas y confirma para calcular",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Text(
                text = "Desarrollado por: Israel Huaman (Versión POO)",
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun CursoRow(nombre: String, peso: String, nota: Float, onNotaChange: (Float) -> Unit) {
    val esAprobado = nota >= 13f
    val badgeColor = if (esAprobado) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val badgeTextColor = if (esAprobado) Color(0xFF2E7D32) else Color(0xFFD32F2F)

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
            Badge(containerColor = badgeColor, contentColor = badgeTextColor) {
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