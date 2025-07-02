package com.example.starlinkloginapp.ui.screens.galeria

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.starlinkloginapp.ui.ViewModel.ImageViewModel
import android.util.Base64
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    imageVM: ImageViewModel,
    navController: NavController
) {
    val imagenes by imageVM.imagenProcesada.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Galería de Imágenes") },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) { Text("←") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(imagenes) { base64 ->
                val bitmap = remember(base64) {
                    val bytes = Base64.decode(base64, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(240.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}