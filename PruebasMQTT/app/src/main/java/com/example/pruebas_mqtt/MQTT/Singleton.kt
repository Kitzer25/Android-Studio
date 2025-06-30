package com.example.pruebas_mqtt.MQTT

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

import android.widget.Toast

import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.Error

object MQTTmanager {

    private var mqttClient: MqttAndroidClient? = null

    fun connect(
        context: Context,
        serverUri: String,
        username: String,
        password: String,
        clientId: String = MqttClient.generateClientId(),
        onConnected: () -> Unit = {},
        onMessage: ((topic: String, message: String) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        if (mqttClient?.isConnected == true) return

        mqttClient = MqttAndroidClient(context, serverUri, clientId).apply {
            setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    Log.e("MQTT", "Conexión perdida", cause)
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val msg = message?.payload?.toString(Charsets.UTF_8) ?: return
                    if (topic != null) {
                        onMessage?.invoke(topic, msg)
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })
        }

        val options = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = true
            this.userName = username
            this.password = password.toCharArray()
        }

        mqttClient?.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "Conectado exitosamente")
                onConnected()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e("MQTT", "Fallo conexión: ${exception?.message}", exception)
                onError?.invoke(exception ?: Exception("Fallo desconocido"))
            }
        })
    }

    fun subscribe(topic: String) {
        mqttClient?.let {
            if (it.isConnected) {
                it.subscribe(topic, 1)
            } else {
                Log.w("MQTT", "No conectado para suscribirse a $topic")
            }
        }
    }

    fun publish(topic: String, message: String) {
        mqttClient?.let {
            if (it.isConnected) {
                it.publish(topic, MqttMessage(message.toByteArray()))
            } else {
                Log.w("MQTT", "No conectado para publicar en $topic")
            }
        }
    }

    fun isConnected(): Boolean = mqttClient?.isConnected ?: false
}
