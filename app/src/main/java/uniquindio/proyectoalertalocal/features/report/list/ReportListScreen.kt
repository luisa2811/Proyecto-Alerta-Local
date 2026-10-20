package uniquindio.proyectoalertalocal.features.report.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion
import uniquindio.proyectoalertalocal.features.report.PublicacionUiModel
import androidx.compose.ui.layout.ContentScale

@Composable
fun ReportListScreen(
    onOpenPublication: (String) -> Unit,
    onOpenNotifications: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReportListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ReportListHeader(
            onOpenNotifications = onOpenNotifications,
            onShowMessage = onShowMessage
        )
        ReportFilters(
            selectedCategory = uiState.selectedCategory,
            onSelectCategory = viewModel::selectCategory
        )
        ReportControls(
            proximity = uiState.proximity,
            onSelectProximity = viewModel::selectProximity
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.filteredPublications, key = { it.id }) { publication ->
                PublicationCard(
                    publication = publication,
                    onClick = { onOpenPublication(publication.id) }
                )
            }
        }
    }
}

@Composable
private fun ReportListHeader(
    onOpenNotifications: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Logo de Alerta Local",
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Alerta Local",
                    modifier = Modifier.padding(start = 9.dp),
                    color = DesignSystem.Colors.HeaderText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable { onShowMessage("La ubicación es solo demostrativa.") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Ubicación simulada",
                    tint = DesignSystem.Colors.ReportMutedText,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Barrio La Esperanza",
                    modifier = Modifier.padding(start = 4.dp),
                    color = DesignSystem.Colors.ReportMutedText,
                    fontSize = 12.sp
                )
            }
        }
        Box {
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .clickable(onClick = onOpenNotifications),
                shape = CircleShape,
                color = DesignSystem.Colors.FilterSurface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.NotificationsNone,
                        contentDescription = "Abrir notificaciones",
                        tint = DesignSystem.Colors.PrimaryDark,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(8.dp)
                    .background(Color(0xFFEF5350), CircleShape)
            )
        }
    }
}

@Composable
private fun ReportFilters(
    selectedCategory: CategoriaPublicacion?,
    onSelectCategory: (CategoriaPublicacion?) -> Unit
) {
    val categories = listOf(null) + CategoriaPublicacion.entries
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onSelectCategory(category) },
                shape = CircleShape,
                color = if (isSelected) DesignSystem.Colors.PrimaryDark else DesignSystem.Colors.FilterSurface
            ) {
                Text(
                    text = category?.etiqueta ?: "Todos",
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    color = if (isSelected) DesignSystem.Colors.White else Color(0xFF505357),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun ReportControls(
    proximity: String,
    onSelectProximity: (String) -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            OutlinedButton(
                onClick = { isMenuOpen = true },
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 5.dp
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "Filtrar proximidad",
                    modifier = Modifier.size(15.dp),
                    tint = DesignSystem.Colors.ReportMutedText
                )
                Text(
                    text = proximity,
                    modifier = Modifier.padding(start = 6.dp),
                    color = Color(0xFF53575A),
                    fontSize = 12.sp
                )
            }
            DropdownMenu(
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false }
            ) {
                listOf("Cerca de mí", "En mi zona", "Toda la ciudad").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelectProximity(option)
                            isMenuOpen = false
                        }
                    )
                }
            }
        }
        Text(
            text = "Lista",
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(horizontal = 15.dp, vertical = 7.dp),
            color = DesignSystem.Colors.PrimaryDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PublicationCard(
    publication: PublicacionUiModel,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = DesignSystem.Colors.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PublicationTag(
                    text = publication.categoria.etiqueta,
                    background = publication.categoria.color.copy(alpha = 0.12f),
                    textColor = publication.categoria.color
                )
                PublicationTag(
                    text = publication.estado,
                    background = reportStatusColor(publication.estado).copy(alpha = 0.13f),
                    textColor = reportStatusColor(publication.estado),
                    modifier = Modifier.padding(start = 6.dp)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${publication.distancia} · ${publication.antiguedad}",
                    color = DesignSystem.Colors.ReportMutedText,
                    fontSize = 10.sp
                )
            }
            Text(
                text = publication.titulo,
                modifier = Modifier.padding(top = 8.dp),
                color = DesignSystem.Colors.ReportTitleText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = publication.descripcion,
                modifier = Modifier.padding(top = 4.dp),
                color = DesignSystem.Colors.ReportDescriptionText,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PublicationCounter(
                    icon = Icons.Filled.ThumbUp,
                    count = publication.votosImportante,
                    description = "Me gusta"
                )
                PublicationCounter(
                    icon = Icons.Filled.ChatBubbleOutline,
                    count = publication.cantidadComentarios,
                    description = "Comentarios"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(27.dp),
                    shape = CircleShape,
                    color = DesignSystem.Colors.PrimaryDark
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = publication.inicialesReportante,
                            color = DesignSystem.Colors.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = publication.nombreReportante,
                        color = DesignSystem.Colors.ReportAuthorText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Residente",
                        color = DesignSystem.Colors.ReportMutedText,
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = publication.ubicacion,
                    modifier = Modifier.weight(1f),
                    color = DesignSystem.Colors.ReportMutedText,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PublicationCounter(
    icon: ImageVector,
    count: Int,
    description: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = DesignSystem.Colors.ReportMutedText,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = count.toString(),
            modifier = Modifier.padding(start = 4.dp),
            color = DesignSystem.Colors.ReportCounterText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = description,
            modifier = Modifier.padding(start = 4.dp),
            color = DesignSystem.Colors.ReportMutedText,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun PublicationTag(
    text: String,
    background: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .clip(CircleShape)
            .background(background)
            .padding(horizontal = 7.dp, vertical = 3.dp),
        color = textColor,
        fontSize = 9.sp,
        fontWeight = FontWeight.SemiBold
    )
}

private fun reportStatusColor(status: String): Color = when (status) {
    "Verificado" -> Color(0xFF25A56A)
    "Pendiente" -> Color(0xFFC68A1A)
    "Resuelto" -> Color(0xFF3F819C)
    else -> DesignSystem.Colors.ReportMutedText
}
