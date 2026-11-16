package uniquindio.proyectoalertalocal.core.component

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import uniquindio.proyectoalertalocal.core.util.createTempImageUri
import uniquindio.proyectoalertalocal.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ImagePickerBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onImagePicked: (Uri) -> Unit,
    onCameraPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { captured ->
        val uri = pendingCameraUri
        pendingCameraUri = null
        if (captured && uri != null) onImagePicked(uri)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = pendingCameraUri ?: createTempImageUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            pendingCameraUri = null
            onCameraPermissionDenied()
        }
        onDismiss()
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onImagePicked(uri)
    }

    if (isVisible) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.image_picker_camera)) },
                    leadingContent = {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        pendingCameraUri = createTempImageUri(context)
                        if (
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            cameraLauncher.launch(checkNotNull(pendingCameraUri))
                            onDismiss()
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                )
                ListItem(
                    headlineContent = { Text(stringResource(R.string.image_picker_gallery)) },
                    leadingContent = {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                        onDismiss()
                    }
                )
            }
        }
    }
}
