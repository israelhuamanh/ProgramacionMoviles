package com.tecsup.publicaciones.ui.detalle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.publicaciones.R
import com.tecsup.publicaciones.ui.UiState
import com.tecsup.publicaciones.ui.components.Cargando
import com.tecsup.publicaciones.ui.components.ErrorView

@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val isDark = isSystemInDarkTheme()

    val lavenderBg = if (isDark) Color(0xFF231738) else Color(0xFFC594FF)
    val primaryText = if (isDark) Color(0xFFF6EDFF) else Color(0xFF140726)
    val darkPurplePill = if (isDark) Color(0xFF4A257A) else Color(0xFF390B63)
    val lightPillBg = if (isDark) Color(0xFF352452) else Color(0xFFFFFFFF)
    val centerBadgeBg = if (isDark) Color(0xFF3B235E) else Color(0xFFB376FA)

    Scaffold(
        containerColor = lavenderBg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        when (val estadoActual = estado) {
            is UiState.Cargando -> {
                Cargando(
                    mensaje = "Cargando detalle de la publicación...",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            is UiState.Error -> {
                ErrorView(
                    mensaje = estadoActual.mensaje,
                    onReintentar = { viewModel.cargarDetalle() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            is UiState.Exito -> {
                val item = estadoActual.datos

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .statusBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    // 1. Barra superior estilo Events / Milan con foto de perfil circular a la derecha
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onVolver,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = primaryText
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Bitácora",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = primaryText
                            )
                        }

                        Surface(
                            color = centerBadgeBg,
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "✦ Post #${item.id}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = primaryText
                                )
                            }
                        }

                        // Foto de perfil circular por defecto en la esquina superior derecha
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEADDFF),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Perfil por defecto",
                                    tint = Color(0xFF390B63),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 2. Fila de píldoras estilo THURS / FRI / SAT / SUN
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ExpressivePillChip(
                            text = "POST #${item.id}",
                            selected = true,
                            selectedBg = darkPurplePill,
                            unselectedBg = lightPillBg,
                            unselectedText = primaryText
                        )
                        ExpressivePillChip(
                            text = "AUTOR #${item.autorId}",
                            selected = false,
                            selectedBg = darkPurplePill,
                            unselectedBg = lightPillBg,
                            unselectedText = primaryText
                        )
                        ExpressivePillChip(
                            text = if (item.esFavorito) "FAVORITO ★" else "LECTURA",
                            selected = false,
                            selectedBg = darkPurplePill,
                            unselectedBg = lightPillBg,
                            unselectedText = primaryText
                        )
                        ExpressivePillChip(
                            text = "JSON API",
                            selected = false,
                            selectedBg = darkPurplePill,
                            unselectedBg = lightPillBg,
                            unselectedText = primaryText
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 3. Título gigante editorial en Serif condensado/itálico estilo "ELENA"
                    val palabraPrincipal = item.titulo
                        .split(" ")
                        .take(2)
                        .joinToString(" ")
                        .uppercase()

                    Text(
                        text = palabraPrincipal,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Black,
                        fontSize = 46.sp,
                        lineHeight = 48.sp,
                        letterSpacing = (-1.5).sp,
                        color = primaryText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Imagen central grande redondeada con foto de perfil por defecto
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                            .clip(RoundedCornerShape(24.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar_editorial),
                            contentDescription = "Foto de perfil por defecto del autor",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Botón flotante de favorito en la esquina del retrato
                        Surface(
                            color = if (item.esFavorito) Color(0xFFFFDAD6) else Color(0xD9FFFFFF),
                            shape = CircleShape,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(14.dp)
                                .size(46.dp)
                        ) {
                            IconButton(onClick = { viewModel.alternarFavorito() }) {
                                Icon(
                                    imageVector = if (item.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Alternar favorito",
                                    tint = if (item.esFavorito) Color(0xFFD32F2F) else Color(0xFF390B63)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 5. Grilla de metadatos en 2 columnas x 2 filas estilo Where / Registration / When / Hosted By
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MetadataItem(
                            label = "Publicación",
                            value = "Artículo #${item.id} ↗",
                            textColor = primaryText,
                            modifier = Modifier.weight(1f)
                        )
                        MetadataItem(
                            label = "Estado",
                            value = if (item.esFavorito) "En Favoritos" else "Sincronizado",
                            textColor = primaryText,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MetadataItem(
                            label = "Sección",
                            value = "Bitácora Digital",
                            textColor = primaryText,
                            modifier = Modifier.weight(1f)
                        )
                        MetadataItem(
                            label = "Publicado por Autor #${item.autorId}",
                            value = "Lectura verificada",
                            textColor = primaryText,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 6. Contenedor con el título completo y cuerpo de la publicación (segundo endpoint)
                    Surface(
                        color = if (isDark) Color(0xFF31214C) else Color(0x55FFFFFF),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Text(
                                text = item.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.contenido,
                                style = MaterialTheme.typography.bodyLarge,
                                color = primaryText.copy(alpha = 0.88f),
                                lineHeight = 23.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón de acción principal para guardar/quitar de favoritos
                    Button(
                        onClick = { viewModel.alternarFavorito() },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = darkPurplePill,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Icon(
                            imageVector = if (item.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (item.esFavorito) "Quitar de Favoritos" else "Guardar en Favoritos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ExpressivePillChip(
    text: String,
    selected: Boolean,
    selectedBg: Color,
    unselectedBg: Color,
    unselectedText: Color
) {
    Surface(
        color = if (selected) selectedBg else unselectedBg,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color.White else unselectedText,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 7.dp)
        )
    }
}

@Composable
private fun MetadataItem(
    label: String,
    value: String,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = textColor.copy(alpha = 0.72f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
