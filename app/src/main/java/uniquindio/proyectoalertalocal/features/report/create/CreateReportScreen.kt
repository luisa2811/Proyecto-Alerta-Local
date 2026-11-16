package uniquindio.proyectoalertalocal.features.report.create

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.geojson.Point
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.component.MapBox
import uniquindio.proyectoalertalocal.core.component.ImagePickerBottomSheet
import uniquindio.proyectoalertalocal.core.component.ReportCategoryTag
import uniquindio.proyectoalertalocal.core.component.ReportImage
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.component.color
import uniquindio.proyectoalertalocal.core.component.labelRes
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import java.util.Locale

@Composable
fun CreateReportScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    onPublished: () -> Unit,
    onOpenReport: (String) -> Unit,
    onPickLocation: (GeoPoint?) -> Unit,
    selectedLocation: GeoPoint?,
    onLocationPickedHandled: () -> Unit,
    modifier: Modifier = Modifier,
    reportId: String? = null,
    viewModel: CreateReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showImagePicker by remember { mutableStateOf(false) }
    val isEditing = uiState.editingReportId != null
    val cameraPermissionMessage = stringResource(R.string.reports_camera_permission)
    val speechUnavailableMessage = stringResource(R.string.reports_speech_unavailable)
    val speechRecognitionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val transcript = activityResult.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (transcript != null) {
                viewModel.generateDraftFromVoice(
                    transcript = transcript,
                    languageTag = Locale.getDefault().toLanguageTag()
                )
            }
        }
    }
    val selectedMapPoint = uiState.latitude?.let { latitude ->
        uiState.longitude?.let { longitude -> Point.fromLngLat(longitude, latitude) }
    }

    LaunchedEffect(reportId) {
        if (reportId != null) {
            viewModel.loadForEdit(reportId)
        }
    }

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let {
            viewModel.onLocationPicked(it.latitude, it.longitude)
            onLocationPickedHandled()
        }
    }

    LaunchedEffect(uiState.result) {
        when (val result = uiState.result) {
            is RequestResult.Success -> {
                onShowMessage(result.message)
                viewModel.onResultShown()
                if (isEditing) {
                    onBack()
                }
            }
            is RequestResult.Failure -> {
                onShowMessage(result.errorMessage)
                viewModel.onResultShown()
            }
            else -> Unit
        }
    }

    LaunchedEffect(uiState.draftResult) {
        when (val result = uiState.draftResult) {
            is RequestResult.Failure -> {
                onShowMessage(result.errorMessage)
                viewModel.onDraftResultShown()
            }
            else -> Unit
        }
    }

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            is CreateReportNavigation.ToMyReports -> {
                onPublished()
                viewModel.onNavigationHandled()
            }
            is CreateReportNavigation.ToReportDetail -> {
                onOpenReport(navigation.reportId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    ImagePickerBottomSheet(
        isVisible = showImagePicker,
        onDismiss = { showImagePicker = false },
        onImagePicked = {
            viewModel.addImage(it.toString())
            showImagePicker = false
        },
        onCameraPermissionDenied = { onShowMessage(cameraPermissionMessage) }
    )

    if (uiState.similarReports.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelSimilar() },
            title = { Text(stringResource(R.string.reports_duplicate_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.similarReports.forEach { report ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, DesignSystem.Colors.Border)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = report.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = stringResource(report.category.labelRes()), fontSize = 10.sp)
                                    Text(text = report.createdAt.toRelativeTime(), fontSize = 10.sp, color = DesignSystem.Colors.TextSecondary)
                                    Text(
                                        text = stringResource(R.string.reports_support_count, report.supportedBy.size),
                                        fontSize = 10.sp,
                                        color = DesignSystem.Colors.TextSecondary
                                    )
                                }
                                if (report.authorId == uiState.currentUserId) {
                                    TextButton(onClick = { viewModel.openOwnReport(report.id) }) {
                                        Text(stringResource(R.string.reports_duplicate_own))
                                    }
                                } else {
                                    TextButton(onClick = { viewModel.supportSimilar(report.id) }) {
                                        Text(stringResource(R.string.reports_duplicate_support))
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.publishAnyway() }) {
                    Text(stringResource(R.string.reports_duplicate_publish_anyway))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelSimilar() }) {
                    Text(stringResource(R.string.reports_cancel))
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ScreenHeader(
            title = stringResource(if (isEditing) R.string.reports_edit_title else R.string.reports_new_title),
            onBack = onBack,
            titleColor = Color(0xFF282A2C),
            trailingContent = {
                androidx.compose.foundation.Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = stringResource(R.string.brand_logo_description),
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        )

        Column(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                if (!isEditing) {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                )
                                putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE,
                                    Locale.getDefault().toLanguageTag()
                                )
                            }
                            try {
                                speechRecognitionLauncher.launch(intent)
                            } catch (_: ActivityNotFoundException) {
                                onShowMessage(speechUnavailableMessage)
                            }
                        },
                        enabled = !uiState.isGeneratingDraft,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        shape = RoundedCornerShape(11.dp)
                    ) {
                        if (uiState.isGeneratingDraft) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = stringResource(R.string.reports_generating_draft),
                                modifier = Modifier.padding(start = 8.dp),
                                fontSize = 12.sp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Mic,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = stringResource(R.string.reports_dictate),
                                modifier = Modifier.padding(start = 8.dp),
                                fontSize = 12.sp
                            )
                        }
                    }
                    uiState.draftNotice?.let { notice ->
                        Text(
                            text = notice,
                            color = DesignSystem.Colors.TextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.reports_category),
                    color = Color(0xFF444648),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 9.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReportCategory.entries.forEach { category ->
                        val isSelected = category == uiState.category
                        Surface(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { viewModel.selectCategory(category) },
                            shape = CircleShape,
                            color = if (isSelected) category.color() else DesignSystem.Colors.FilterSurface
                        ) {
                            Text(
                                text = stringResource(category.labelRes()),
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp),
                                color = if (isSelected) DesignSystem.Colors.White else Color(0xFF4F5254),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }

                ReportField(
                    label = stringResource(R.string.reports_field_title),
                    value = uiState.title,
                    placeholder = stringResource(R.string.reports_title_example),
                    onValueChange = viewModel::onTitleChange,
                    error = viewModel.titleError(),
                    modifier = Modifier.padding(top = 17.dp)
                )
                ReportField(
                    label = stringResource(R.string.reports_field_description),
                    value = uiState.description,
                    placeholder = stringResource(R.string.reports_description_hint),
                    onValueChange = viewModel::onDescriptionChange,
                    multiline = true,
                    error = viewModel.descriptionError(),
                    modifier = Modifier.padding(top = 17.dp)
                )
                Text(
                    text = stringResource(R.string.reports_location),
                    color = Color(0xFF444648),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 17.dp)
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(140.dp),
                    shape = RoundedCornerShape(11.dp),
                    border = BorderStroke(1.dp, DesignSystem.Colors.Border)
                ) {
                    MapBox(
                        modifier = Modifier.fillMaxSize(),
                        center = selectedMapPoint ?: Point.fromLngLat(-75.6811, 4.5339),
                        zoom = 15.5,
                        selectedPoint = selectedMapPoint,
                        interactive = false,
                        showMyLocationButton = false
                    )
                }
                OutlinedButton(
                    onClick = {
                        onPickLocation(
                            selectedMapPoint?.let {
                                GeoPoint(it.latitude(), it.longitude())
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(42.dp),
                    shape = RoundedCornerShape(11.dp)
                ) {
                    Text(
                        text = stringResource(R.string.reports_choose_map),
                        color = DesignSystem.Colors.PrimaryDark,
                        fontSize = 12.sp
                    )
                }
                if (selectedMapPoint == null) {
                    Text(
                        text = stringResource(R.string.reports_select_map_location),
                        color = DesignSystem.Colors.ModeratorRed,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
                ReportField(
                    label = stringResource(R.string.reports_address_or_reference),
                    value = uiState.location,
                    placeholder = stringResource(R.string.reports_address_example),
                    onValueChange = viewModel::onLocationChange,
                    error = viewModel.locationError(),
                    modifier = Modifier.padding(top = 17.dp)
                )

                Text(
                    text = stringResource(R.string.reports_photos),
                    color = Color(0xFF444648),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 17.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    (uiState.retainedImageUrls + uiState.localImageUris).forEach { imageUri ->
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(11.dp))
                        ) {
                            ReportImage(
                                imageUrl = imageUri,
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(3.dp)
                                    .size(22.dp)
                                    .clickable { viewModel.removeImage(imageUri) },
                                shape = CircleShape,
                                color = DesignSystem.Colors.White
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = stringResource(R.string.reports_remove_photo),
                                        tint = DesignSystem.Colors.TextPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                    if (uiState.retainedImageUrls.size + uiState.localImageUris.size <
                        CreateReportViewModel.MAX_IMAGES
                    ) {
                        OutlinedButton(
                            onClick = { showImagePicker = true },
                            modifier = Modifier.height(72.dp),
                            shape = RoundedCornerShape(11.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = stringResource(R.string.reports_add_photo),
                                modifier = Modifier.padding(start = 5.dp),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
                if (uiState.retainedImageUrls.isEmpty() && uiState.localImageUris.isEmpty()) {
                    Text(
                        text = stringResource(R.string.reports_photo_required),
                        color = DesignSystem.Colors.ModeratorRed,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }

        Button(
            onClick = { if (isEditing) viewModel.saveChanges() else viewModel.publish() },
            enabled = uiState.isFormValid && uiState.result !is RequestResult.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.AccentDark, contentColor = DesignSystem.Colors.White)
        ) {
            if (uiState.result is RequestResult.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = DesignSystem.Colors.White, strokeWidth = 2.dp)
                Text(
                    text = uiState.loadingMessage,
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 13.sp
                )
            } else {
                Text(
                    text = stringResource(if (isEditing) R.string.reports_save_changes else R.string.reports_publish),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ReportField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    multiline: Boolean = false,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color(0xFF444648),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(if (multiline) 100.dp else 43.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(DesignSystem.Colors.White)
                .border(BorderStroke(1.dp, DesignSystem.Colors.Border), RoundedCornerShape(11.dp))
                .padding(horizontal = 13.dp, vertical = if (multiline) 12.dp else 0.dp),
            verticalAlignment = if (multiline) Alignment.Top else Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = !multiline,
                textStyle = TextStyle(color = Color(0xFF343638), fontSize = 13.sp, lineHeight = 19.sp),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = DesignSystem.Colors.Placeholder,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                maxLines = if (multiline) 2 else 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        if (error != null) {
            Text(text = error, color = DesignSystem.Colors.ModeratorRed, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}
