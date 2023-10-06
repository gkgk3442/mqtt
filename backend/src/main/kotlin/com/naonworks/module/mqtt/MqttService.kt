package com.naonworks.module.mqtt

import jakarta.annotation.PreDestroy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks

@Service
class MqttService(
    private val props: MqttProperties,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    private val subscribeMap = mutableMapOf<String, Int>()
    private val subscribeSinks = Sinks.many().multicast().directAllOrNothing<Pair<String, MqttMessage>>()

    private lateinit var client: MqttClient

    init {
        val options = MqttConnectOptions()
        options.isCleanSession = true
        options.connectionTimeout = 1
        options.keepAliveInterval = 30

        client = MqttClient(props.serverUri, props.clientId, MemoryPersistence())

        client.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {}

            override fun messageArrived(topic: String, message: MqttMessage) {
                log.debug(
                    "subscribe topic : {}, id : {}, payload : {}",
                    topic,
                    message.id,
                    String(message.payload)
                )

                subscribeSinks.tryEmitNext(topic to message)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {}
        })

        client.connect(options)
    }

    @PreDestroy
    fun close() {
        client.disconnect()
        client.close()
    }

    suspend fun publish(topic: String, payload: ByteArray): Boolean {
        log.debug("publish topic : ${topic}, payload :${String(payload)}")

        val message = MqttMessage(payload)
        message.qos = props.qos

        try {
            client.publish(topic, message)
        } catch (e: Exception) {
            log.error("", e)

            return false
        }

        return true
    }

    fun startsWithTopicSubscribe(topic: String): Flow<Pair<String, MqttMessage>> {
        val cnt = subscribeMap.getOrDefault(topic, 0)

        if (cnt == 0) {
            subscribeMap[topic] = 1
            client.subscribe(topic, props.qos)
            log.debug("subscribe topic : ${topic}")
        } else {
            subscribeMap[topic] = cnt + 1
        }

        return subscribeSinks.asFlux()
            .asFlow()
            .filter { it.first.startsWith(topic, false) }
            .map { it }
            .onCompletion {
                unsubscribe(topic)
            }
    }

    fun subscribe(topic: String): Flow<MqttMessage> =
        startsWithTopicSubscribe(topic).filter { it.first.equals(topic, false) }.map { it.second }

    private fun unsubscribe(topic: String) {
        val cnt = subscribeMap.getOrDefault(topic, 0)

        if (cnt <= 1) {
            subscribeMap.remove(topic)
            client.unsubscribe(topic)

            log.debug("unsubscribe topic : ${topic}")
        } else {
            subscribeMap[topic] = cnt - 1
        }
    }
}