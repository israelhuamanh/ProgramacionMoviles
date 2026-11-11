package com.tecsup.publicaciones.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.publicaciones.domain.model.Item
import com.tecsup.publicaciones.domain.model.Rol
import com.tecsup.publicaciones.ui.UiState
import com.tecsup.publicaciones.ui.components.Cargando
import com.tecsup.publicaciones.ui.components.ErrorView

@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    rol: Rol,
    onItemClick: (Int) -> Unit,
    onIrAFavoritos: () -> Unit,
    onIrAAdmin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val totalFavoritos by viewModel.totalFavoritos.collectAsStateWithLifecycle()

    val isDark = isSystemInDarkTheme()

    val pageBg = if (isDark) Color(0xFF141318) else Color(0xFFF3F1F8)
    val heroGradient = if (isDark) {
        Brush.linearGradient(
            colors = listOf(Color(0xFF2B2440), Color(0xFF1F2B3D))
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color(0xFFE5DEFF), Color(0xFFD8E6FF))
        )
    }
    val heroTextColor = if (isDark) Color(0xFFF4EFFA) else Color(0xFF1A162B)
    val pillBgColor = if (isDark) Color(0xFF3E355B) else Color(0xFFFFFFFF).copy(alpha = 0.78f)
    val searchPillBg = if (isDark) Color(0xFF25232C) else Color(0xFFE8E3EE)

    Scaffold(
        containerColor = pageBg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Cabecera Material 3 Expressive (estilo Pixel Drop)
            // El fondo cubre la barra de estado y contrasta nítidamente con el reloj y batería
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                    .background(heroGradient)
            ) {
                // Formas decorativas orgánicas estilo Material Expressive
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .offset(x = (-35).dp, y = (-25).dp)
                        .clip(CircleShape)
                        .background(
                            if (isDark) Color(0xFF4F378B).copy(alpha = 0.28f)
                            else Color(0xFFB69DF8).copy(alpha = 0.28f)
                        )
                )
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 30.dp, y = 20.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDark) Color(0xFF386A8B).copy(alpha = 0.25f)
                            else Color(0xFF90CAF9).copy(alpha = 0.32f)
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Barra de acciones superior con píldora de rol y botones circulares
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = pillBgColor,
                            shape = RoundedCornerShape(50),
                            tonalElevation = 2.dp
                        ) {
                            Text(
                                text = if (rol == Rol.ADMIN) "Bitácora · Administrador" else "Bitácora · Usuario",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = heroTextColor,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ExpressiveActionIcon(
                                bgColor = pillBgColor,
                                onClick = { viewModel.refrescar() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Sincronizar",
                                    tint = heroTextColor
                                )
                            }

                            ExpressiveActionIcon(
                                bgColor = pillBgColor,
                                onClick = onIrAFavoritos
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (totalFavoritos > 0) {
                                            Badge(
                                                containerColor = Color(0xFFD32F2F),
                                                contentColor = Color.White
                                            ) {
                                                Text("$totalFavoritos")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Ver Favoritos",
                                        tint = Color(0xFFE53935)
                                    )
                                }
                            }

                            if (rol == Rol.ADMIN) {
                                ExpressiveActionIcon(
                                    bgColor = pillBgColor,
                                    onClick = onIrAAdmin
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = "Panel Admin",
                                        tint = heroTextColor
                                    )
                                }
                            }

                            ExpressiveActionIcon(
                                bgColor = pillBgColor,
                                onClick = onCerrarSesion
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Cerrar sesión",
                                    tint = heroTextColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Letras grandes expresivas "Publicaciones" estilo Pixel Drop
                    Text(
                        text = "Publicaciones",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = heroTextColor,
                        letterSpacing = (-1.2).sp,
                        lineHeight = 44.sp,
                        textAlign = Alignment.CenterHorizontally.let { TextAlign.Center }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Explora tus lecturas y notas sincronizadas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = heroTextColor.copy(alpha = 0.75f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            if (aviso != null) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = aviso ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Barra de búsqueda estilo Material You (cápsula suave sin bordes duros)
            TextField(
                value = busqueda,
                onValueChange = { viewModel.actualizarBusqueda(it) },
                placeholder = {
                    Text(
                        text = "Buscar publicaciones...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.actualizarBusqueda("") },
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = searchPillBg,
                    unfocusedContainerColor = searchPillBg,
                    disabledContainerColor = searchPillBg,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            when (val estadoActual = estado) {
                is UiState.Cargando -> {
                    Cargando()
                }
                is UiState.Error -> {
                    ErrorView(
                        mensaje = estadoActual.mensaje,
                        onReintentar = { viewModel.refrescar() }
                    )
                }
                is UiState.Exito -> {
                    val lista = estadoActual.datos
                    if (lista.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Article,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (busqueda.isNotEmpty()) {
                                        "No se encontraron resultados para \"$busqueda\"."
                                    } else {
                                        "No hay publicaciones guardadas."
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Pulsa el botón de sincronizar arriba para descargar las publicaciones.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(lista, key = { it.id }) { item ->
                                PublicacionCard(
                                    item = item,
                                    onClick = { onItemClick(item.id) },
                                    onAlternarFavorito = { viewModel.alternarFavorito(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpressiveActionIcon(
    bgColor: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        color = bgColor,
        shape = CircleShape,
        modifier = Modifier.size(40.dp)
    ) {
        IconButton(onClick = onClick) {
            content()
        }
    }
}

@Composable
fun PublicacionCard(
    item: Item,
    onClick: () -> Unit,
    onAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val palettes = if (isDark) {
        listOf(
            Color(0xFF282436),
            Color(0xFF212B36),
            Color(0xFF2C2529),
            Color(0xFF222C2A)
        )
    } else {
        listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF7F2FA),
            Color(0xFFEEF5FF),
            Color(0xFFFDF4F5)
        )
    }
    val boxColor = palettes[item.id % palettes.size]
    val accentChipBg = if (isDark) Color(0xFF3E355B) else Color(0xFFE8DEF8)
    val accentChipText = if (isDark) Color(0xFFEADDFF) else Color(0xFF21005D)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = boxColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Cabecera del box expresivo con chip redondeado y botón circular de favorito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = accentChipBg,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Publicación #${item.id} · Autor #${item.autorId}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = accentChipText,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Surface(
                    color = if (item.esFavorito) {
                        Color(0xFFFFDAD6)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(38.dp)
                ) {
                    IconButton(onClick = onAlternarFavorito) {
                        Icon(
                            imageVector = if (item.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Marcar favorito",
                            tint = if (item.esFavorito) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.titulo,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 19.sp,
                    lineHeight = 24.sp
                ),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.subtitulo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
