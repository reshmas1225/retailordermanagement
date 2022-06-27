package com.rop.orderprocessingapi.service

import com.mongodb.MongoException
import com.rop.orderprocessingapi.config.KafkaProperties
import com.rop.orderprocessingapi.constants.ApplicationConstants
import com.rop.orderprocessingapi.dto.OrderRequest
import com.rop.orderprocessingapi.dto.OrderResponse
import com.rop.orderprocessingapi.dto.Responses
import com.rop.orderprocessingapi.repository.OrderRepository
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFuture

/**
 * Contains business operations associated with the order processing endpoints resource.
 */
@Service
@Slf4j
class OrderService {

    @Autowired
    KafkaTemplate<String, OrderResponse> kafkaTemplate

    @Autowired
    OrderRepository orderRepository

    @Autowired
    KafkaProperties kafkaProperties

    /**
     * Listening and Receiving the messages of kafka topic
     * @param orderResponse
     */
    @KafkaListener(topics = '${kafka.topicName}',
            groupId = '${kafka.groupId}',
            containerFactory = "opmListner")
    void listener(OrderResponse orderResponse) {
        OrderRequest order = orderRepository.findByObjectId(new ObjectId(orderResponse.objectId))
        order.setStatus(ApplicationConstants.PROCESSED)
        log.info("Received messages for topic: ${kafkaProperties.topicName}, custometId: ${orderResponse.customerId} with objectIds: ${orderResponse.objectId}")
        orderRepository.save(order)
    }

    /**
     * Public message to the specified kafka topic
     * @param order
     */
    void sendOrder(OrderRequest order) {
        order.setStatus(ApplicationConstants.PLACED)
        orderRepository.save(order)

        OrderResponse orderResponse = new OrderResponse(
                objectId: order.id.toHexString(),
                customerId: order.customerId
        )
        log.info("Sending messages to kafka queue for topic: ${kafkaProperties.topicName}, custometId: ${orderResponse.customerId} with objectIds: ${orderResponse.objectId}")

        try {
            kafkaTemplate.send(kafkaProperties.topicName, orderResponse)
        } catch (Exception e) {
            log.error(ApplicationConstants.LOGGER_EXCEPTION_PATTERN, order.customerId, e)
        }

    }

    /**
     * Fetch all order details
     * @return
     */
    Responses getOrders() {
        List<OrderRequest> orderRequests = orderRepository.findAll()

        log.info("Retrieved all order requests")
        Responses<OrderRequest> responses = new Responses<>(
                resources: orderRequests
        )
        return responses
    }

    /**
     * Fetch order details
     * @param custId
     * @return
     */
    Responses getOrder(int custId) {
        List<OrderRequest> orderRequests = new ArrayList<>()
        try {
            orderRequests = orderRepository.findOrderByCustId(custId)
        } catch (MongoException me) {
            log.error(ApplicationConstants.LOGGER_EXCEPTION_PATTERN, custId, me)
        }
        log.info("Retrieved order requests for Customer ID ${custId}")
        Responses<OrderRequest> responses = new Responses<>(
                resources: orderRequests
        )
        return responses
    }
}
