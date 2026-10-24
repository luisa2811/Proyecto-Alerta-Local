package uniquindio.proyectoalertalocal.core.component

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSeverity
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

fun getAuthorInitials(authorName: String): String {
    val words = authorName.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(1).uppercase()
        else -> (words[0].take(1) + words[1].take(1)).uppercase()
    }
}

fun ProximityFilter.label(): String = when (this) {
    ProximityFilter.NEARBY -> "Cerca de mí"
    ProximityFilter.ZONE -> "En mi zona"
    ProximityFilter.CITY -> "Toda la ciudad"
}

@Composable
fun ReportHeader(
    onOpenNotifications: () -> Unit,
    modifier: Modifier = Modifier,
    onEditLocation: (() -> Unit)? = null,
    neighborhoodText: String = "Los Rosales",
    addressText: String = "Cra 15 #8-42"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 16.dp, top = 10.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Logo de Alerta Local",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Alerta Local",
                    modifier = Modifier.padding(start = 10.dp),
                    color = DesignSystem.Colors.HeaderText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .then(
                        if (onEditLocation != null) {
                            Modifier.clickable(onClick = onEditLocation)
                        } else {
                            Modifier
                        }
                    )
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Ubicación simulada",
                    tint = DesignSystem.Colors.HomeAccent,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = neighborhoodText,
                    color = Color(0xFF393B3D),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                if (addressText.isNotEmpty()) {
                    Text(
                        text = " · $addressText",
                        color = DesignSystem.Colors.HomeSecondaryText,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
                if (onEditLocation != null) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar ubicación",
                        tint = DesignSystem.Colors.PrimaryDark,
                        modifier = Modifier
                            .padding(start = 7.dp)
                            .size(15.dp)
                    )
                }
            }
        }

        Box {
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .clickable(onClick = onOpenNotifications),
                shape = CircleShape,
                color = DesignSystem.Colors.NotificationSurface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.NotificationsNone,
                        contentDescription = "Abrir notificaciones",
                        tint = DesignSystem.Colors.PrimaryDark,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(8.dp)
                    .background(DesignSystem.Colors.HomeAccent, CircleShape)
            )
        }
    }
}

@Composable
fun ReportCategoryFilters(
    selectedCategory: ReportCategory?,
    onCategorySelect: (ReportCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(null) + ReportCategory.entries
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            val label = category?.label() ?: "Todos"
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onCategorySelect(category) },
                shape = CircleShape,
                color = if (isSelected) DesignSystem.Colors.PrimaryDark else DesignSystem.Colors.FilterSurface
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    color = if (isSelected) DesignSystem.Colors.White else Color(0xFF55585B),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun ProximitySelector(
    proximity: ProximityFilter,
    onProximitySelect: (ProximityFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { isMenuOpen = true },
            contentPadding = PaddingValues(horizontal = 11.dp, vertical = 5.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "Filtrar por proximidad",
                modifier = Modifier.size(16.dp),
                tint = DesignSystem.Colors.HomeSecondaryText
            )
            Text(
                text = proximity.label(),
                modifier = Modifier.padding(start = 6.dp),
                color = Color(0xFF4F5356),
                fontSize = 12.sp
            )
        }
        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = { isMenuOpen = false }
        ) {
            ProximityFilter.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label()) },
                    onClick = {
                        onProximitySelect(option)
                        isMenuOpen = false
                    }
                )
            }
        }
    }
}

@Composable
fun Tag(
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
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = textColor,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun ReportCategoryTag(
    category: ReportCategory,
    modifier: Modifier = Modifier
) {
    Tag(
        text = category.label(),
        background = category.color().copy(alpha = 0.12f),
        textColor = category.color(),
        modifier = modifier
    )
}

@Composable
fun ReportSeverityTag(
    severity: ReportSeverity,
    modifier: Modifier = Modifier
) {
    Tag(
        text = severity.label(),
        background = severity.color().copy(alpha = 0.12f),
        textColor = severity.color(),
        modifier = modifier
    )
}

@Composable
fun ReportStatusTag(
    status: ReportStatus,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(status.color().copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (status == ReportStatus.VERIFIED) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = status.color(),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
        }
        Text(
            text = status.label(),
            color = status.color(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ReportEmptyState(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAction,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DesignSystem.Colors.PrimaryDark,
                    contentColor = DesignSystem.Colors.White
                )
            ) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
fun ReportCard(
    summary: ReportSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFeatured: Boolean = false
) {
    val report = summary.report

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = DesignSystem.Colors.White,
        shadowElevation = if (isFeatured) 4.dp else 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReportCategoryTag(category = report.category)
                Spacer(modifier = Modifier.width(6.dp))
                ReportSeverityTag(severity = summary.severity)
                Spacer(modifier = Modifier.width(6.dp))
                ReportStatusTag(status = report.status)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${report.distanceMeters} m · ${report.createdAt.toRelativeTime()}",
                    color = DesignSystem.Colors.HomeSecondaryText,
                    fontSize = 10.sp
                )
            }

            Text(
                text = report.title,
                modifier = Modifier.padding(top = 8.dp),
                color = DesignSystem.Colors.ReportTitleText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = report.description,
                modifier = Modifier.padding(top = 4.dp),
                color = DesignSystem.Colors.ReportDescriptionText,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReportCounter(
                    icon = if (summary.supportedByCurrentUser) Icons.Filled.Star else Icons.Filled.StarBorder,
                    count = summary.supportCount,
                    description = "Es importante"
                )
                ReportCounter(
                    icon = Icons.Filled.ChatBubbleOutline,
                    count = summary.commentCount,
                    description = "Comentarios"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val initials = getAuthorInitials(summary.authorName)
                Box(
                    modifier = Modifier
                        .size(27.dp)
                        .background(DesignSystem.Colors.PrimaryDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = DesignSystem.Colors.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = summary.authorName,
                        color = DesignSystem.Colors.ReportAuthorText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Residente",
                        color = DesignSystem.Colors.HomeSecondaryText,
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = report.location,
                    modifier = Modifier.weight(1f),
                    color = DesignSystem.Colors.HomeSecondaryText,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ReportCounter(
    icon: ImageVector,
    count: Int,
    description: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = DesignSystem.Colors.HomeSecondaryText,
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
            color = DesignSystem.Colors.HomeSecondaryText,
            fontSize = 10.sp
        )
    }
}
