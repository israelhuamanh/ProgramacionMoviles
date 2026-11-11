package com.tecsup.publicaciones.ui.detalle

import androidx.compose.foundation.Image
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

    val screenBg = if (isDark) Color(0xFF141821) else Color(0xFFEDF2FA)
    val primaryText = if (isDark) Color(0xFFF3F6FC) else Color(0xFF162030)
    val activePillBg = if (isDark) Color(0xFFE2B86B) else Color(0xFF1565C0)
    val activePillText = if (isDark) Color(0xFF1A1612) else Color.White
    val inactivePillBg = if (isDark) Color(0xFF232B3A) else Color(0xFFFFFFFF)
    val contentBoxBg = if (isDark) Color(0xFF1E2533) else Color(0xFFFFFFFF)

    Scaffold(
        containerColor = screenBg,
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Bitácora",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = primaryText
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ExpressivePillChip(
                            text = "POST #${item.id}",
                            selected = true,
                            selectedBg = activePillBg,
                            selectedText = activePillText,
                            unselectedBg = inactivePillBg,
                            unselectedText = primaryText
                        )
                        ExpressivePillChip(
                            text = "AUTOR #${item.autorId}",
                            selected = false,
                            selectedBg = activePillBg,
                            selectedText = activePillText,
                            unselectedBg = inactivePillBg,
                            unselectedText = primaryText
                        )
                        ExpressivePillChip(
                            text = if (item.esFavorito) "FAVORITO ★" else "LECTURA",
                            selected = false,
                            selectedBg = activePillBg,
                            selectedText = activePillText,
                            unselectedBg = inactivePillBg,
                            unselectedText = primaryText
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

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
                        fontSize = 44.sp,
                        lineHeight = 46.sp,
                        letterSpacing = (-1.2).sp,
                        color = primaryText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(14.dp))

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

                        Surface(
                            color = if (item.esFavorito) Color(0xFFFFDAD6) else Color(0xEBFFFFFF),
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
                                    tint = if (item.esFavorito) Color(0xFFD32F2F) else Color(0xFF1565C0)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

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
                            label = "Autor #${item.autorId}",
                            value = "Lectura verificada",
                            textColor = primaryText,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Surface(
                        color = contentBoxBg,
                        shape = RoundedCornerShape(22.dp),
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
                                color = primaryText.copy(alpha = 0.85f),
                                lineHeight = 23.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.alternarFavorito() },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = activePillBg,
                            contentColor = activePillText
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
    selectedText: Color,
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
            color = if (selected) selectedText else unselectedText,
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
            color = textColor.copy(alpha = 0.7f)
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
