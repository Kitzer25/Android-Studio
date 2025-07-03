package com.example.starlinkloginapp.ui.screens.galeria

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.ImageRequest.Builder
import coil.transform.CircleCropTransformation
import com.example.starlinkloginapp.ui.theme.AlegreyaSansFont
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun GalleryScreen(navController: NavController) {
    val context = LocalContext.current
    var images by remember { mutableStateOf<List<File>>(emptyList()) }
    var selectedImage by remember { mutableStateOf<File?>(null) }

    fun loadImages() {
        val dir = context.getExternalFilesDir("galeria") ?: context.filesDir
        val files = dir.listFiles()
            ?.filter { it.extension.equals("jpg", ignoreCase = true) || it.extension.equals("png", ignoreCase = true) }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
        images = files
    }

    LaunchedEffect(Unit) { loadImages() }

    // Pantalla completa
    selectedImage?.let { file ->
        FullScreenImage(
            file = file,
            onDismiss = { selectedImage = null }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Galería de Muestras",
                        fontFamily = AlegreyaSansFont,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF096D98),
                    titleContentColor = Color.White.copy(1f)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White.copy(1f)
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (images.isEmpty()) {
                EmptyGalleryMessage()
            } else {
                GalleryGrid(
                    images = images,
                    onImageClick = { file -> selectedImage = file },
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
private fun GalleryGrid(
    images: List<File>,
    onImageClick: (File) -> Unit,
    paddingValues: PaddingValues
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(images) { file ->
            GalleryImageItem(file = file, onClick = { onImageClick(file) })
        }
    }
}

@Composable
private fun GalleryImageItem(file: File, onClick: () -> Unit) {
    val painter =
        rememberAsyncImagePainter(
            Builder(LocalContext.current).data(data = file).apply(
            block = { -> crossfade(true) }).build())

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Image(
            painter = painter,
            contentDescription = "Imagen de muestra",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun FullScreenImage(file: File, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            val painter = rememberAsyncImagePainter(
                Builder(LocalContext.current).data(
                data = file
            ).apply(block = { ->
                crossfade(true)
                allowHardware(false)
            }).build())

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun EmptyGalleryMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.PhotoLibrary,
            contentDescription = "Galería vacía",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No hay imágenes disponibles",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}