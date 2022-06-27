package com.rop.orderprocessingapi.controller

import com.rop.orderprocessingapi.config.ApiProperties
import com.rop.orderprocessingapi.constants.ApplicationConstants
import com.rop.orderprocessingapi.dto.OrderRequest
import com.rop.orderprocessingapi.dto.Responses
import com.rop.orderprocessingapi.service.OrderService
import com.rop.orderprocessingapi.util.QueryValidator
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Contains REST endpoints for retail order processing management tasks
 */

@RestController
@RequestMapping(ApplicationConstants.APP_CONTEXT_ROOT)
@Slf4j
class OSPController {

    @Autowired
    OrderService orderService

    /**
     * Fetches all orders
     * @param pageSize
     * @param page
     * @return
     */
    @RequestMapping(value = '/orders', method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getOrders(
            @RequestParam(value = 'per_page', required = false) Integer pageSize,
            @RequestParam(value = 'page', required = false) Integer page
    ) {
        pageSize = pageSize ? pageSize : ApiProperties.perPageDefault
        page = page ? page : ApiProperties.pageNumberDefault
        QueryValidator.validatePageParameters(pageSize, page, ApiProperties.perPageMax)

        Responses<OrderRequest> orderResponse = orderService.getOrders()
        ResponseEntity.status(HttpStatus.OK).body(orderResponse)
    }

    /**
     * Process the order and push the order message kafka
     * @param orderItem
     * @return
     */
    @RequestMapping(value = '/order', method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity sendOrderMessage(@RequestBody OrderRequest orderRequest) {
        if (null == orderRequest.customerId) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing customer_id")
        }
        orderService.sendOrder(orderRequest)
        ResponseEntity.status(HttpStatus.ACCEPTED).body()
    }

    /**
     * Fetches order details using customer id
     * @param customerId
     * @return
     */
    @RequestMapping(value = '/order/{customer_id}', method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getOrder(@PathVariable(value = 'customer_id', required = true) int customerId) {
        Responses<OrderRequest> orderResponse = orderService.getOrder(customerId)
        ResponseEntity.status(HttpStatus.OK).body(orderResponse)
    }
}
