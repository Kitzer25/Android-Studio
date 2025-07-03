@file:Suppress("DEPRECATION", "INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

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
import android.util.Log
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
import com.example.starlinkloginapp.Conexion.Models.ImagenPayload
import com.example.starlinkloginapp.MQTT.MqttClientManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java

@Composable
fun CameraScreen(
    navController: NavController,
    correoUsuario: String,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFut = remember { ProcessCameraProvider.getInstance(context) }
    val imageCaptureRef = remember { mutableStateOf<ImageCapture?>(null) }
    var requestPermission  by remember { mutableStateOf(true) }


    /* ViewModel de Imágenes */
    val navGalery = remember { mutableStateOf(false) }

    val mqttManager = remember {
        MqttClientManager(
            serverUri = "tcp://161.132.48.224:1883",
            defaultTopic = "default",
            onMessageReceived = {}
        )
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
                        mqttManager.publish("Android/IoT", "")
                        delay(1100)
                        capturar(
                            imageCaptureRef.value,
                            context,
                            mqttManager,
                            correoUsuario
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Capturar") }
        }
    }

    /* Suscriptor al Broker MQTT */
    LaunchedEffect(Unit) {
        mqttManager.subscribe("Android/Fotografia/Proceso")
        {
            base64 ->
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val bytes = Base64.decode(base64.trim(), Base64.DEFAULT)

                    val dir  = context.getExternalFilesDir("galeria") ?: context.filesDir
                    if (!dir.exists()) dir.mkdirs()

                    val file = File(dir, "img_${System.currentTimeMillis()}.jpg")
                    file.writeBytes(bytes)

                    withContext(Dispatchers.Main) {
                        navController.navigate("galery")
                    }

                } catch (e: Exception) {
                    Log.e("MQTT", "Error guardando imagen", e)
                }
            }
        }
    }

    LaunchedEffect(navGalery.value) {
        if (navGalery.value){
            navController.navigate("galery")
            navGalery.value = false
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        if (requestPermission) {
            launcher.launch(Manifest.permission.CAMERA)
            requestPermission = false
        }
    }

    DisposableEffect(Unit) {
        onDispose { mqttManager.disconnect() }
    }
}

private fun capturar(
    imageCapture: ImageCapture?,
    ctx: Context,
    mqtt: MqttClientManager,
    correoUsuario: String
) {
    if (imageCapture == null) {
        Toast.makeText(ctx, "Sin Captura", Toast.LENGTH_SHORT).show()
        return
    }

    val photoFile = File(
        ctx.externalMediaDirs.first(),
        "captura-${System.currentTimeMillis()}.jpg"
    )

    val outputOpts = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOpts,
        ContextCompat.getMainExecutor(ctx),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(res: ImageCapture.OutputFileResults) {

                /* ---- Construir payload JSON con Moshi ---- */
                val nombreImg = "img_${System.currentTimeMillis()}"
                val base64 = Base64.encodeToString(photoFile.readBytes(), Base64.NO_WRAP)
                val payload = ImagenPayload(
                    correoUsuario,
                    nombreImg,
                    base64)


                val moshi   = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val adapter = moshi.adapter(ImagenPayload::class.java)
                val json    = adapter.toJson(payload)

                mqtt.publish("Android/Fotografia", json)
                Toast.makeText(ctx, "Imagen enviada", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exc: ImageCaptureException) {
                Toast.makeText(ctx, "Fallo al capturar: ${exc.message}", Toast.LENGTH_SHORT).show()
            }
        }
    )
}
