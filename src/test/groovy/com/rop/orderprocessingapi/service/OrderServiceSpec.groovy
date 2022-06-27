package com.rop.orderprocessingapi.service

import com.rop.orderprocessingapi.dto.OrderItem
import com.rop.orderprocessingapi.dto.OrderRequest
import com.rop.orderprocessingapi.repository.OrderRepository
import org.mockito.Mock
import org.springframework.kafka.core.KafkaTemplate
import spock.lang.Specification

/**
 * Tests the {@link OrderService}
 */
class OrderServiceSpec extends Specification{
    OrderService mockOrderService
    @Mock
    KafkaTemplate mockKafkaTemplate
    @Mock
    OrderRepository mockOrderRepository

    def 'setup'() {
        mockKafkaTemplate = Mock(KafkaTemplate)
        mockOrderRepository = Mock(OrderRepository)
        mockOrderService = new OrderService(
               kafkaTemplate: mockKafkaTemplate,
                orderRepository: mockOrderRepository
        )
    }

    def 'sendOrder - Topic does not exist'() {
        given:
        OrderItem input = new OrderItem(
                itemId: 2,
                itemQuantity: 1,
                itemName: "Lenovo",
        )
        List<OrderItem> orderItems = new ArrayList<OrderItem>()
        orderItems.add(input)
        OrderRequest orderRequest = new OrderRequest(
                customerId: 1,
                orderItems: orderItems
        )

        List<OrderRequest> orderRequests = new ArrayList<OrderItem>()
        orderRequests.add(orderRequest)

        when:
        def result = mockOrderService.getOrders()

        then:
        mockOrderRepository.findAll() >> {
            orderRequests
        }

        result.resources[0].customerId == 1
    }
}
