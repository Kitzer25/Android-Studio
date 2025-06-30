package com.example.pruebas_mqtt.Pruebas

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pruebas_mqtt.MQTT.MQTTmanager

@Composable
fun PruebasMQTT() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var mensajeRecibido by remember { mutableStateOf("") }
    var conectado by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        MQTTmanager.connect(
            context,
            "tcp://161.132.48.224:1883",
            username = "esp32",
            password = "tecsup123",
            onConnected = {
                conectado = true
                MQTTmanager.subscribe("Pruebas/Data")
            },
            onMessage = { topic, message ->
                Log.d("MQTT", "Mensaje en $topic: $message")
                if (message == "data") {
                    mensajeRecibido = "Recibido correctamente"
                    MQTTmanager.publish("Pruebas/Mensaje", "Publicar Ejecutado Correctamente")
                }
            },
            onError = {
                mensajeRecibido = "Error: ${it.message}"
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Estado MQTT: ${if (conectado) "Conectado" else "Desconectado"}")
        Spacer(Modifier.height(8.dp))
        Text("Último mensaje: $mensajeRecibido")
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (conectado) {
                MQTTmanager.publish("Pruebas/Mensaje", "Mensaje manual desde Android")
            }
        }) {
            Text("Publicar mensaje manual")
        }
    }
}
