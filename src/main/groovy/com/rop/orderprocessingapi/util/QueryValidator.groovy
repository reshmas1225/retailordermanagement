package com.rop.orderprocessingapi.util

import com.rop.orderprocessingapi.expections.OrderManagementException

/**
 * Validates input from users
 */

class QueryValidator {
    static void validatePageParameters(int pageSize, int page, int perPageMax) {
        if (pageSize > perPageMax) {
            throw new OrderManagementException("Requested page size $pageSize is too large. Maximum is ${perPageMax}.")
        }
        if (pageSize < 0 || page < 0) {
            throw new OrderManagementException('Requested page parameters must be positive integers.')
        }
    }
}
