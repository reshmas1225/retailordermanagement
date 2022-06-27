package com.rop.orderprocessingapi.config


import com.rop.orderprocessingapi.dto.OrderResponse
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

/**
 * Holds kafka service specific properties
 */
@Configuration
@EnableKafka
@ConfigurationProperties(prefix = 'kafka')
class KafkaProperties {

    String maxPollRecords
    String sessionTimeoutMs
    String autoCommitIntervalMs
    String autoOffset
    String kafkaConnect
    String topicName
    String groupId

    @Bean
    ProducerFactory<String, OrderResponse> producerFactory() {
        Map<String, Object> producerMap = new HashMap<>()
        producerMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConnect)
        producerMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
        producerMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
        new DefaultKafkaProducerFactory(producerMap)
    }

    @Bean
    KafkaTemplate<String, OrderResponse> kafkaTemplate() {
        new KafkaTemplate<String, OrderResponse>(producerFactory())
    }

    @Bean
    ConsumerFactory<String, OrderResponse> opmConsumer() {
        Map<String, Object> consumerMap = new HashMap<>()
        //hostip
        consumerMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConnect)
        consumerMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffset)
        consumerMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
        consumerMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
        consumerMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class)
        consumerMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs)
        consumerMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs)
        consumerMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords)

        // return message in JSON format
        return new DefaultKafkaConsumerFactory<>(consumerMap, new StringDeserializer(),
                new JsonDeserializer<>(OrderResponse.class))
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, OrderResponse> opmListner() {
        ConcurrentKafkaListenerContainerFactory<String, OrderResponse> factory = new ConcurrentKafkaListenerContainerFactory<>()
        factory.setConsumerFactory(opmConsumer())
        factory
    }
}
