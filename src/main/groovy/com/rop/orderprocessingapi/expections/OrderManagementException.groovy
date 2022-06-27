package com.rop.orderprocessingapi.expections

import groovy.util.logging.Slf4j

@Slf4j
class OrderManagementException extends Exception {

    OrderManagementException(Exception e, String message) {
        super(e.message)
        log.error(message)
    }

    OrderManagementException(Exception e) {
        super(e.message)
        log.error(e.message)
    }

    OrderManagementException(String message, Exception e) {
        super(message)
        log.error(message + " = " + e.message)
    }

    OrderManagementException(String message) {
        super(message)
        log.error(message)
    }
}
