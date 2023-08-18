package com.naonworks.mqtt

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    @PostConstruct
    fun init() {
        val options = MqttConnectOptions()
        options.isCleanSession = true
        options.keepAliveInterval = 30

        val persistence = MemoryPersistence()

        client = MqttClient(props.serverUri, props.clientId, persistence)
        client.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {}

            override fun messageArrived(topic: String, message: MqttMessage) {
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

    fun publish(topic: String, payload: String): Boolean {
        log.debug("publish topic : ${topic}, payload :${payload}")

        val message = MqttMessage(payload.toByteArray())
        message.qos = props.qos

        try {
            client.publish(topic, message)
        } catch (e: Exception) {
            e.printStackTrace()

            return false
        }

        return true
    }

    fun subscribe(topic: String): Flow<MqttMessage> {
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
            .flowOn(Dispatchers.IO)
            .filter { it.first.equals(topic, false) }
            .map { it.second }
            .onCompletion {
                unsubscribe(topic)
            }
    }

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