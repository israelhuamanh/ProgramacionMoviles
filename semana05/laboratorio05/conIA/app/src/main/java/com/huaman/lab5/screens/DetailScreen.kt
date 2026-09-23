package com.huaman.lab5.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.huaman.lab5.data.DataProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    val student = DataProvider.getStudentById(itemId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expediente Académico", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A1B9A),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (student != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF3E5F5))
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            Color(0xFF6A1B9A),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .offset(y = 40.dp)
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF6A1B9A), modifier = Modifier.size(60.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(50.dp))
                
                Text(text = student.name, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF4A148C))
                Text(text = student.career, color = Color.Gray, fontSize = 16.sp)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailItem(icon = Icons.Default.Info, title = "ID Estudiante", value = "2024-000${student.id}")
                        Spacer(modifier = Modifier.height(16.dp))
                        DetailItem(icon = Icons.Default.Email, title = "Correo Electrónico", value = student.email)
                        Spacer(modifier = Modifier.height(16.dp))
                        DetailItem(icon = Icons.Default.Info, title = "Facultad", value = "Ingeniería y Tecnología")
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    Text(text = "Biografía", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF4A148C))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = student.bio, color = Color.DarkGray, fontSize = 14.sp)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Estudiante no encontrado")
            }
        }
    }
}

@Composable
fun DetailItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF9C27B0), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, color = Color.Gray, fontSize = 12.sp)
            Text(text = value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }
}
