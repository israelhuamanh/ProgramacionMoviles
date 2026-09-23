package com.huaman.lab5.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.huaman.lab5.data.DataProvider
import com.huaman.lab5.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // Tomamos al usuario principal
    val user = DataProvider.getStudentById(1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Perfil", fontWeight = FontWeight.Bold, color = Color(0xFF6A1B9A)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color(0xFF6A1B9A))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { padding ->
        if (user != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Background with Avatar
                val gradient = Brush.verticalGradient(listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC)))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(brush = gradient),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF6A1B9A), modifier = Modifier.size(50.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(text = "INFORMACIÓN PERSONAL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ProfileItem(icon = Icons.Default.Person, title = "Nombre Completo", value = user.name)
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileItem(icon = Icons.Default.Email, title = "Correo", value = user.email)
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileItem(icon = Icons.Default.Phone, title = "Teléfono", value = user.phone)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(text = "ACADÉMICO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ProfileItem(icon = Icons.Default.Info, title = "Carrera", value = user.career)
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileItem(icon = Icons.Default.DateRange, title = "Ciclo Actual", value = user.cycle)
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                OutlinedButton(
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

@Composable
fun ProfileItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF3E5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF6A1B9A))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, color = Color.Gray, fontSize = 12.sp)
            Text(text = value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }
}
