package com.rop.orderprocessingapi.controller

import com.rop.orderprocessingapi.dto.OrderItem
import com.rop.orderprocessingapi.dto.OrderRequest
import com.rop.orderprocessingapi.dto.Responses
import com.rop.orderprocessingapi.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

/**
 * Tests the {@link OSPController}
 */
class OSPControllerSpec extends Specification {

    OSPController ospController
    OrderService mockOrderService

    def 'setup'() {
        mockOrderService = Mock(OrderService)
        ospController = new OSPController(
                orderService: mockOrderService,
        )
    }

    def 'POST Order | Push order to message queue'() {
        given:
        OrderRequest orderRequest = generateOrder()

        when:
        ResponseEntity result = ospController.sendOrderMessage(orderRequest)

        then:
        1 * mockOrderService.sendOrder(_)

        result.statusCode == HttpStatus.ACCEPTED
    }

    def 'GET Order | Get the orders'() {
        given:
        OrderRequest orderRequest = generateOrder()
        List<OrderRequest> orders = new ArrayList()
        orders.add(orderRequest)
        Responses<OrderRequest> responses = new Responses<>(
                resources: orders
        )

        when:
        ResponseEntity result = ospController.getOrder(orderRequest.customerId)

        then:
        1*mockOrderService.getOrder(_) >> {responses}

        result ? result.statusCode == HttpStatus.OK : result.statusCode == HttpStatus.FORBIDDEN
    }

    private OrderRequest generateOrder() {
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
        orderRequest
    }
}

