@file:Suppress("DEPRECATION")

package com.example.starlinkloginapp.ui.cameraX

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.starlinkloginapp.MQTT.MqttClientManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CameraScreen(navController: NavController) {

    /* ---------- Contextos y estados ---------- */
    val context           = LocalContext.current
    val lifecycleOwner    = LocalLifecycleOwner.current
    val cameraProviderFut = remember { ProcessCameraProvider.getInstance(context) }
    val imageCaptureRef   = remember { mutableStateOf<ImageCapture?>(null) }
    var requestPermission by remember { mutableStateOf(true) }
    val coroutineScope    = rememberCoroutineScope()

    val mqttManagerState = remember { mutableStateOf<MqttClientManager?>(null) }

    val onMessageReceived: (String) -> Unit = { msg ->
        if (msg.equals("fotografiar", ignoreCase = true)) {
            mqttManagerState.value?.let { manager ->
                capturar(imageCaptureRef.value, context, manager)
            }
        }
    }

    LaunchedEffect(Unit) {
        if (mqttManagerState.value == null) {
            mqttManagerState.value = MqttClientManager(
                serverUri = "tcp://161.132.48.224:1883",
                topic     = "default",
                onMessageReceived = {}
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFut.addListener({
                    val provider = cameraProviderFut.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val capture = ImageCapture.Builder().build()
                    imageCaptureRef.value = capture

                    provider.unbindAll()
                    provider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        capture
                    )
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.weight(1f)
            ) { Text("Regresar") }

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        mqttManagerState.value?.publish("Android/IoT", "")
                        delay(1100)
                        mqttManagerState.value?.let {
                            capturar(imageCaptureRef.value, context, it)
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Capturar") }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_LONG).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (requestPermission) {
            launcher.launch(Manifest.permission.CAMERA)
            requestPermission = false
        }
    }

    DisposableEffect(Unit) {
        onDispose { mqttManagerState.value?.disconnect() }
    }
}


private fun capturar(
    imageCapture: ImageCapture?,
    ctx: Context,
    mqtt: MqttClientManager
) {
    if (imageCapture == null) {
        Toast.makeText(ctx, "Sin Captura", Toast.LENGTH_SHORT).show()
        return
    }

    val photoFile = File(
        ctx.externalMediaDirs.first(),
        "captura-${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(ctx),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(res: ImageCapture.OutputFileResults) {
                val base64 = Base64.encodeToString(photoFile.readBytes(), Base64.NO_WRAP)
                mqtt.publish("Android/Fotografia", base64)
                Toast.makeText(ctx, "Imagen enviada", Toast.LENGTH_SHORT).show()
            }
            override fun onError(exc: ImageCaptureException) {
                Toast.makeText(ctx, "Fallo al capturar: ${exc.message}", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

