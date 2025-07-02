package com.example.starlinkloginapp.MQTT

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.MqttTopic
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.nio.charset.Charset



class MqttClientManager(
    private val serverUri: String,
    private val defaultTopic: String,
    private val onMessageReceived: (String) -> Unit
) {

    private lateinit var mqttClient: MqttClient
    private val topicCallbacks = mutableMapOf<String, (String) -> Unit>()

    init {
        connect()
    }

    fun connect() {
        try {
            mqttClient = MqttClient(serverUri, MqttClient.generateClientId(), MemoryPersistence())

            val options = MqttConnectOptions().apply {
                isAutomaticReconnect = true
                isCleanSession = false
                userName = "esp32"
                password = "tecsup123".toCharArray()
            }

            mqttClient.connect(options)

            mqttClient.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    println("MQTT conexión perdida: ${cause?.message}")
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val payload = message?.toString() ?: return
                    val utf8 = payload.toByteArray(Charset.forName("UTF-8"))
                    val content = String(utf8, Charset.forName("UTF-8"))

                    // Verifica si hay un callback específico para ese topic
                    topic?.let {
                        topicCallbacks[it]?.invoke(content) ?: onMessageReceived(content)
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    // No se necesita nada aquí por ahora
                }
            })

            // Subscribir al tópico por defecto
            mqttClient.subscribe(defaultTopic)

        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String, callback: (String) -> Unit) {
        try {
            topicCallbacks[topic] = callback
            if (!mqttClient.isConnected) connect()
            mqttClient.subscribe(topic, 1)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray(Charset.forName("UTF-8")))
            mqttMessage.qos = 1
            mqttClient.publish(topic, mqttMessage)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun isConnected(): Boolean = this::mqttClient.isInitialized && mqttClient.isConnected

    fun disconnect() {
        try {
            if (mqttClient.isConnected) {
                mqttClient.disconnect()
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}